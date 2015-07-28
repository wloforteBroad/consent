package org.genomebridge.consent.http.resources;

import org.genomebridge.consent.http.models.ElectionReview;
import org.genomebridge.consent.http.service.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("electionReview")
public class ElectionReviewResource {

    private ReviewResultsAPI api;

    public ElectionReviewResource() {
        this.api = AbstractReviewResultsAPI.getInstance();
    }

    @GET
    @Path("/consent/{consentId}")
    @Produces("application/json")
    public ElectionReview getConsentPendingCases(@PathParam("consentId") String consentId) {
        return api.describeElectionReview(consentId);
    }
}