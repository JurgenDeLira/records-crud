package org.ms.record.api;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ms.record.entity.Record;
import org.ms.record.service.RecordService;

import java.util.List;

@Path("/records")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecordResource {

    @Inject
    RecordService recordService;

    @POST
    public Uni<Response> create(Record record) {
        return recordService.create(record)
                .onItem().transform(id -> Response.ok(id).status(Response.Status.CREATED).build());
    }

    @GET
    @Path("/{id}")
    public Uni<Record> getById(@PathParam("id") Long id){
        return recordService.findById(id);
    }

    @GET
    public Uni<List<Record>> getAll(){
        return recordService.findAll();
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete (@PathParam("id") Long id) {
        return recordService.delete(id)
                .onItem().transform(deleted -> {
                    if (deleted) {
                        return Response.noContent().build();
                    } else {
                        return Response.status(Response.Status.NOT_FOUND).build();
                    }
                });
    }
}