package org.ms.record.api;

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
    public List<Record> getAll() {
        return Record.listAll();
    }

    @GET
    @Path("/{id}")
    public Record get(@PathParam("id") Long id) {
        return Record.findById(id);
    }

    @POST
    @Transactional
    public Record create(Record record) {
        record.persist();
        return record;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Record update(@PathParam("id") Long id, Record record) {
        Record entity = Record.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Record with id " + id + " not found", 404);
        }
        entity.setAlbumName(record.getAlbumName());
        entity.setArtist(record.getArtist());
        entity.setYear(record.getYear());
        entity.setGenre(record.getGenre());
        entity.setFormat(record.getFormat());
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Record entity = Record.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Record with id " + id + " not found", 404);
        }
        entity.delete();
    }
}