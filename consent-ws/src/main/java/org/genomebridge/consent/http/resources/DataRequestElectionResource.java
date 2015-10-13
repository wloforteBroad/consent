package org.genomebridge.consent.http.resources;

import org.genomebridge.consent.http.models.Election;
import org.genomebridge.consent.http.service.AbstractElectionAPI;
import org.genomebridge.consent.http.service.AbstractVoteAPI;
import org.genomebridge.consent.http.service.ElectionAPI;
import org.genomebridge.consent.http.service.VoteAPI;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.net.URI;

@Path("{api : (api/)?}dataRequest/{requestId}/election")
public class DataRequestElectionResource extends Resource {

    private final ElectionAPI api;
    private final VoteAPI voteAPI;

    public DataRequestElectionResource() {
        this.api = AbstractElectionAPI.getInstance();
        this.voteAPI = AbstractVoteAPI.getInstance();
    }

    @POST
    @Consumes("application/json")
    public Response createDataRequestElection(@Context UriInfo info, Election rec,
                                              @PathParam("requestId") String requestId) {
        URI uri;
        Election election;
        try {
            election = api.createElection(rec, requestId.toString(), false);
            voteAPI.createVotes(election.getElectionId(), false);
            uri = info.getRequestUriBuilder().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (NotFoundException e){
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        return Response.created(uri).entity(election).build();
    }


    @GET
    @Produces("application/json")
    public Election describe(@PathParam("requestId") String requestId) {
        try {
            return api.describeDataRequestElection(requestId);
        } catch (Exception e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response deleteElection(@PathParam("requestId") String requestId, @PathParam("id") Integer id, @Context UriInfo info) {
        try {
            api.deleteElection(requestId, id);
            return Response.status(Response.Status.OK).entity("Election was deleted").build();
        } catch (Exception e) {
            throw new NotFoundException(e.getMessage());
        }
    }

}
