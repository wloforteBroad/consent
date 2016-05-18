package org.broadinstitute.consent.http.service.ontologyIndexer;

import com.google.api.client.http.HttpResponse;
import org.apache.commons.collections.MapUtils;
import org.broadinstitute.consent.http.models.ontology.StreamRec;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

/**
 * Created by SantiagoSaucedo on 3/11/2016.
 */
public class IndexerServiceImpl implements IndexerService {

    private final StoreOntologyService storeOntologyService;
    private final IndexOntologyService indexService;


    public IndexerServiceImpl(StoreOntologyService storeService, IndexOntologyService indexService)
    {
        this.storeOntologyService = storeService;
        this.indexService = indexService;
    }


    @Override
    public Response saveAndIndex(List<StreamRec> streamRecList) throws IOException, OWLOntologyCreationException {
        boolean atLeastOneIndexed = false;
        try{
            for(StreamRec sr : streamRecList){
                OWLOntology model  = indexService.validateAndCreateOWLOntology(sr);
                if(Objects.nonNull(model)){
                    storeOntologyService.storeOntologyFile(sr);
                    indexService.indexOntology(sr,model);
                    storeOntologyService.updateOntologyConfigurationFile(sr);
                    atLeastOneIndexed = true;
                 }
            }
        }catch (Exception e){
            throw e;
        }
        if(atLeastOneIndexed){
            return okResponseBuilder(streamRecList);
        }
        return Response.notModified().build();
    }


    @Override
    public Response deleteOntologiesByType(String fileURL) throws  IOException{
        Map<String, HashMap> configurationMap = storeOntologyService.retrieveConfigurationFile();
        if(MapUtils.isEmpty(configurationMap)) return Response.status(Response.Status.BAD_REQUEST).build();
        HttpResponse r = storeOntologyService.retrieveFile(fileURL);

        //Delete ontologies from Index.
        if(indexService.deleteOntologiesByFile( r.getContent(),
                (String) configurationMap.get(fileURL).get("prefix"))){

            //Update configuration file
            deleteFileFromMap(configurationMap,fileURL);
            storeOntologyService.storeOntologyConfigurationFile(configurationMap);

            //Delete file from CloudStorage
            storeOntologyService.deleteFile(fileURL);
            return Response.ok().build();
        }
        return Response.notModified().build();
    }

    private void deleteFileFromMap(Map<String, HashMap> configMap,String fileUrl) {
        configMap.remove(fileUrl);
    }

    @Override
    public List<HashMap> getIndexedFiles() throws IOException {
        List<HashMap> indexedFilesList = new ArrayList<>();
        Map<String, HashMap> configurationMap = storeOntologyService.retrieveConfigurationFile();
        if(MapUtils.isEmpty(configurationMap)) return null;
        for(Map.Entry<String, HashMap> e : configurationMap.entrySet()){
            HashMap m = new HashMap<String,String>();
            m.put("fileUrl",e.getKey());
            for(Object k : e.getValue().keySet()){
                m.put(k.toString(),e.getValue().get(k));
            }
            indexedFilesList.add(m);
        }
        return indexedFilesList;
    }

    private Response okResponseBuilder(List<StreamRec> streamRecList) {
        Map<String,HashMap> entityMap = new HashMap<>();
        for(StreamRec s : streamRecList){
            HashMap<String,Boolean> m = new HashMap<>();
            m.put("saveAndIndexed",s.getAtLeastOneOntologyIndexed());
            entityMap.put(s.getFileName(),m);
        }
        return  Response.ok().entity(entityMap).build();
    }
}

