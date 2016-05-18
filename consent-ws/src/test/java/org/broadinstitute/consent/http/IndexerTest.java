package org.broadinstitute.consent.http;


import com.google.common.io.Resources;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.broadinstitute.consent.http.configurations.ConsentConfiguration;
import org.broadinstitute.consent.http.enumeration.DACUserRoles;
import org.broadinstitute.consent.http.models.DACUser;
import org.broadinstitute.consent.http.models.DACUserRole;
import org.broadinstitute.consent.http.models.ontology.StreamRec;
import org.broadinstitute.consent.http.service.ontologyIndexer.IndexOntologyService;
import org.broadinstitute.consent.http.service.ontologyIndexer.IndexerService;
import org.broadinstitute.consent.http.service.ontologyIndexer.IndexerServiceImpl;
import org.broadinstitute.consent.http.service.ontologyIndexer.StoreOntologyService;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexerTest extends IndexerServiceTest {

    public static final int CREATED = Response.Status.CREATED
            .getStatusCode();
    public static final int OK = Response.Status.OK.getStatusCode();


    public static IndexerService indexerService;


    @ClassRule
    public static final DropwizardAppRule<ConsentConfiguration> RULE = new DropwizardAppRule<>(
            ConsentApplication.class, resourceFilePath("consent-config.yml"));

    @Override
    public DropwizardAppRule<ConsentConfiguration> rule() {
        return RULE;
    }


    @Before
    public void initialize() throws GeneralSecurityException, IOException {
        StoreOntologyService storeService = getStorageServiceMock();
        IndexOntologyService indexOntologyService = new IndexOntologyService(null,null);
        indexerService = new IndexerServiceImpl(storeService,indexOntologyService);

    }


    @After
    public void remove() {
    }


   /* @Test
    public void saveAndIndexFile() throws IOException, OWLOntologyCreationException {
        List<StreamRec> streamRecList = new ArrayList<>();
        StreamRec streamRec = new StreamRec(retrieveResource("diseases.owl"),"organization","DURPO",".owl","diseases.owl");
        streamRecList.add(streamRec);
        indexerService.saveAndIndex(streamRecList);
    }*/

    public InputStream retrieveResource(String fileName) throws IOException {
        URL urlDiseasesOntologyFile = Thread.currentThread().getContextClassLoader().getResource(fileName);
        return  Resources.asByteSource(urlDiseasesOntologyFile).openStream();

    }



}
