package org.ms.record.api;

import io.smallrye.mutiny.Uni;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ms.record.entity.Record;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;


@Path("/records")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecordResource {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @GET
    public Uni<List<Record>> getAll() {
        return Record.findAll().list();
    }

    @GET
    @Path("/{id}")
    public Uni<Record> get(@PathParam("id") Long id) {
        return Record.findById(id);
    }

    @POST
    public Uni<Response> create (Record record) {
        return sessionFactory.withTransaction(session -> session.persist(record))
                .replaceWith(Response.ok(record).status(201).build());
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Uni<Record> update(@PathParam("id") Long id, Record record){
        return Record.<Record>findById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException("Record with id " + id + " not found", 404))
                .onItem().invoke(entity -> {
                    entity.setAlbumName(record.getAlbumName());
                    entity.setArtist(record.getArtist());
                    entity.setYear(record.getYear());
                    entity.setGenre(record.getGenre());
                })
                .call(entity -> entity.persistAndFlush());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Uni<Void> delete(@PathParam("id") Long id) {
        return Record.<Record>findById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException("Record with id " + id + " not found", 404))
                .call(entity -> entity.delete())
                .replaceWithVoid(); //
    }

}