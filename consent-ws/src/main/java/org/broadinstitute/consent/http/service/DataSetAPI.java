package org.broadinstitute.consent.http.service;

import org.broadinstitute.consent.http.models.DataSet;
import org.broadinstitute.consent.http.models.Dictionary;
import org.broadinstitute.consent.http.models.dto.DataSetDTO;

import java.io.File;
import java.util.Collection;
import java.util.List;

public interface DataSetAPI {

    ParseResult create(File dataSetFile);

    ParseResult overwrite(File dataSetFile);

    Collection<DataSetDTO> describeDataSets(Integer dacUserId) ;

    List<DataSet> getDataSetsForConsent(String consentId);

    Collection<DataSetDTO> describeDataSets(List<String> objectIds) ;

    Collection<Dictionary> describeDictionary();
    
    List<String> autoCompleteDataSets(String partial);

    void deleteDataset(String datasetObjectId);

    void disableDataset(String datasetObjectId, Boolean active);
}