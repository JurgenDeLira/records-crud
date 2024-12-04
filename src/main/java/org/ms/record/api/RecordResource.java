package org.ms.record.api;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.ms.record.entity.Record;
import org.ms.record.service.RecordService;

import javax.inject.Inject;

@Path("/records")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecordResource {

    @Inject
    RecordService recordService; // Inyectamos el servicio

    @GET
    public Uni<List<Record>> getAll() {
        return recordService.getAllRecords(); // Delegamos la operación al servicio
    }

    @GET
    @Path("/{id}")
    public Uni<Record> get(@PathParam("id") Long id) {
        return recordService.getRecordById(id); // Delegamos la operación al servicio
    }

    @POST
    @Transactional
    public Uni<Record> create(Record record) {
        return recordService.createRecord(record); // Delegamos la creación al servicio
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Uni<Record> update(@PathParam("id") Long id, Record record) {
        return recordService.updateRecord(id, record); // Delegamos la actualización al servicio
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Uni<Void> delete(@PathParam("id") Long id) {
        return recordService.deleteRecord(id); // Delegamos la eliminación al servicio
    }
}
