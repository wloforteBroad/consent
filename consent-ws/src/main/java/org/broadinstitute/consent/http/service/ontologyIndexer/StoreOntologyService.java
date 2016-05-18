package org.broadinstitute.consent.http.service.ontologyIndexer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import org.apache.commons.collections.MapUtils;
import org.broadinstitute.consent.http.cloudstore.CloudStore;
import org.broadinstitute.consent.http.models.ontology.StreamRec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SantiagoSaucedo on 3/11/2016.
 */
public class StoreOntologyService   {

    private final CloudStore store;
    private final String bucketSubdirectory;
    private final String configurationFileName;
    private final String jsonExtension = ".json";
    private static final Logger logger = LoggerFactory.getLogger("StoreOntologyService");
    private static final ObjectMapper mapper = new ObjectMapper();


    public StoreOntologyService(CloudStore store, String bucketSubdirectory, String configurationFileName) {
        this.store = store;
        this.bucketSubdirectory = bucketSubdirectory;
        this.configurationFileName = configurationFileName;
    }


    public void storeOntologyConfigurationFile(Map<String, HashMap> configurationMap)   {
        String type = MediaType.APPLICATION_JSON;
        try {
            String url_suffix =  bucketSubdirectory + configurationFileName + jsonExtension ;
            store.postStorageDocument(mapToStreamParser(configurationMap),
                    type,
                    url_suffix);
        }catch (IOException | GeneralSecurityException e) {
            logger.error("Exception thrown while saving configuration file.",e);
            throw new InternalServerErrorException("Problem with storage service.");
        }
    }

    public void updateOntologyConfigurationFile(StreamRec streamRec) throws IOException {
        Map<String, HashMap> configMap = this.retrieveConfigurationFile();
        if(MapUtils.isEmpty(configMap)){
            storeOntologyConfigurationFile(configurationMapBuilder(streamRec));
        }else {
            addFileData(configMap, streamRec);
        }
        storeOntologyConfigurationFile(configMap);
    }

    private Map<String, HashMap> configurationMapBuilder(StreamRec streamRec){
        Map<String, HashMap> configMap = new HashMap<>();
        addFileData(configMap, streamRec);
        return configMap;
    }


    private Map<String, HashMap> parseAsMap(String str) throws IOException {
        ObjectReader reader = mapper.readerFor(Map.class);
        return reader.readValue(str);
    }


    private void addFileData(Map<String, HashMap> json, StreamRec streamRec) {
        HashMap streamRecMap = new HashMap<String, String>();
        streamRecMap.put("fileName", streamRec.getFileName());
        streamRecMap.put("prefix", streamRec.getPrefix());
        streamRecMap.put("ontologyType", streamRec.getOntologyType());
        json.put(streamRec.getUrl(), streamRecMap);
    }

    private ByteArrayInputStream mapToStreamParser(Map<String, HashMap> json) throws JsonProcessingException {
        String content = new ObjectMapper().writeValueAsString(json);
        return new ByteArrayInputStream(content.getBytes());
    }

    public StreamRec storeOntologyFile(StreamRec srec){
        String url_suffix = bucketSubdirectory + "/" + srec.getFileName();
        try {
            srec.setUrl(store.postStorageDocument(srec.getStream(),
                    srec.getFileType(),
                    url_suffix));
        }catch (IOException | GeneralSecurityException e) {
            logger.error(String.format("Exception thrown while saving Ontology  file %s.", url_suffix),e);
            throw new InternalServerErrorException("Problem with storage service.");
        }
        return srec;
    }


    public Map<String, HashMap> retrieveConfigurationFile (){
        try {
            HttpResponse response = store.getStorageDocument(store.generateURLForDocument(bucketSubdirectory + configurationFileName + jsonExtension).toString());
            return parseAsMap(response.parseAsString());
        } catch (Exception e) {
            if (e instanceof HttpResponseException && ((HttpResponseException) e).getStatusCode() == 404) {
                return null;
            } else {
                logger.error("Exception thrown while retrieving Ontology configuration file.",e);
                throw new InternalError("Problem with storage service.");
            }
        }
    }

    public HttpResponse retrieveFile(String fileUrl){
        try {
            return store.getStorageDocument(fileUrl);
        } catch (Exception e) {
            logger.error(String.format("Exception thrown while retrieving file %s.", fileUrl),e);
            throw new InternalError("Problem with storage service.");
        }
    }

    public void  deleteFile(String fileUrl){
        try {
            store.deleteStorageDocument(fileUrl);
        }catch (Exception e) {
            logger.error(String.format("Exception thrown while file deletion %s.", fileUrl),e);
            throw new InternalError("Problem with storage service.");
        }
    }
}
