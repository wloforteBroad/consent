package org.genomebridge.consent.http.db;

import org.genomebridge.consent.http.models.dto.DataSetDTO;
import org.genomebridge.consent.http.models.dto.DataSetPropertyDTO;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataSetPropertiesMapper implements ResultSetMapper<DataSetDTO> {

    Map<Integer, DataSetDTO> dataSets = new LinkedHashMap<>();
    static final String PROPERTY_KEY = "key";
    static final String PROPERTY_PROPERTYVALUE = "propertyValue";


    public DataSetDTO map(int index, ResultSet r, StatementContext ctx) throws SQLException {

        DataSetDTO dataSetDTO;
        Integer dataSetId = r.getInt("dataSetId");
        String consentId = r.getString("consentId");

        if (!dataSets.containsKey(dataSetId)) {
            dataSetDTO = new DataSetDTO( new ArrayList<>());
            dataSetDTO.setConsentId(consentId);
            DataSetPropertyDTO property = new DataSetPropertyDTO("Dataset Name",r.getString("name"));
            dataSetDTO.getProperties().add(property);
            property = new DataSetPropertyDTO(r.getString(PROPERTY_KEY),r.getString(PROPERTY_PROPERTYVALUE));
            dataSetDTO.getProperties().add(property);
            dataSets.put(dataSetId, dataSetDTO);
        } else {
            dataSetDTO = dataSets.get(dataSetId);
            DataSetPropertyDTO property = new DataSetPropertyDTO(r.getString(PROPERTY_KEY),r.getString(PROPERTY_PROPERTYVALUE));
            dataSetDTO.getProperties().add(property);
        }
        return dataSetDTO;
    }
}
