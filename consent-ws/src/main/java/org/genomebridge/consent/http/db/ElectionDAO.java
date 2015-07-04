package org.genomebridge.consent.http.db;

import java.util.Date;

import org.genomebridge.consent.http.models.Election;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

@RegisterMapper({ ElectionMapper.class })
public interface ElectionDAO extends Transactional<ElectionDAO> {


   @SqlQuery("select electionId from election  where referenceId = :referenceId")
   Integer getElectionByReferenceId(@Bind("referenceId") String referenceId);
   
  @SqlUpdate("insert into election " +
            "(electionType, finalVote, finalRationale, status, createDate,referenceId) values " +
            "( :electionType, :finalVote, :finalRationale, :status, :createDate,:referenceId)")
  @GetGeneratedKeys
  Integer insertElection( @Bind("electionType") String electionType,
                       @Bind("finalVote") Boolean finalVote,
                       @Bind("finalRationale") String finalRationale,
                       @Bind("status") String status,
                       @Bind("createDate") Date createDate,
                       @Bind("referenceId") String referenceId);

   @SqlUpdate("delete  from election where referenceId = :referenceId")
   void deleteElectionByReferenceId(@Bind("referenceId") String referenceId);

    @SqlUpdate("update election set finalVote = :finalVote, finalRationale = :finalRationale, "
    	    	+ " status = :status where electionId = :electionId ")
   void updateElectionById(@Bind("electionId") Integer electionId,
    		            @Bind("finalVote") Boolean finalVote,
    		            @Bind("finalRationale") String finalRationale,
    		            @Bind("status") String status);
    
   
    @SqlQuery("select typeId from electiontype where type = :type")
    String findElectionTypeByType(@Bind("type") String type);

    
    @SqlQuery("select e.electionId,e.finalVote,e.status,e.createDate,e.referenceId, e.finalRationale,et.type electionType from election e "
    		+ " inner join electiontype et on e.electionType = et.typeId"
    		+ " and  e.referenceId = :referenceId")
    Election findElectionByReferenceId(@Bind("referenceId") String referenceId);
    
    @SqlQuery("select e.electionId,e.finalVote,e.status,e.createDate,e.referenceId, e.finalRationale,et.type electionType from election e "
    		+ " inner join electiontype et on e.electionType = et.typeId"
    		+ " and  e.electionId = :electionId")
    Election findElectionById(@Bind("electionId") Integer electionId);

   
}