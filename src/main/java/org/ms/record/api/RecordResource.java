package org.ms.record.api;

import io.smallrye.mutiny.Uni;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.ms.record.entity.Record;

import java.util.List;

@Path("/records")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecordResource {

    @GET
    public Uni<List<Record>> getAll() {
        return Record.listAll();
    }

    @GET
    @Path("/{id}")
    public Uni<Record> get(@PathParam("id") Long id) {
        return Record.<Record>findById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException("Record not found", 404));
    }

    @POST
    @Transactional
    public Uni<Record> create(Record record) {
        return Uni.createFrom().item(record)
                .onItem().transformToUni(r -> r.persistAndFlush())
                .onItem().transform(r -> r);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Uni<Record> update(@PathParam("id") Long id, Record record) {
        return Record.<Record>findById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException("Record with id " + id + " not found", 404))
                .onItem().transform(entity ->{
                    entity.setAlbumName(record.getAlbumName());
                    entity.setArtist(record.getArtist());
                    entity.setYear(record.getYear());
                    entity.setGenre(record.getGenre());
                    return entity;
                });
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Uni<Void> delete(@PathParam("id") Long id) {
        return Record.<Record>findById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException("Record with id " + id + " not found", 404))
                .onItem().transformToUni(entity -> {
                    entity.delete();
                    return Uni.createFrom().voidItem();
                });
    }
}