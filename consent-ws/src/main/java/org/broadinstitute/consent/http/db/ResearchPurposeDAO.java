package org.broadinstitute.consent.http.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

public interface ResearchPurposeDAO extends Transactional<ResearchPurposeDAO> {

    @SqlQuery("select purposeId from researchpurpose where purposeId = :purposeId")
    Integer checkResearchPurposebyId(@Bind("purposeId") Integer purposeId);

}