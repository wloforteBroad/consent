package org.broadinstitute.consent.http.service.ontologyIndexer;

import org.broadinstitute.consent.http.models.ontology.StreamRec;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface IndexerService {
    
    Response saveAndIndex(List<StreamRec> streamRecList) throws IOException, OWLOntologyCreationException;

    List<HashMap> getIndexedFiles() throws IOException;

    Response deleteOntologiesByType(String fileURL) throws IOException;

}
