package org.ms.record.service;


import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import org.ms.record.entity.Record;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RecordService {
    private final PgPool client;

    public RecordService(PgPool client) {
        this.client = client;
    }

    // Create a new record
    public Uni<Long> create(Record record) {
        String sql = "INSERT INTO record (album_name, artist, year) VALUES ($1, $2, $3) RETURNING id";
        return client.preparedQuery(sql)
                .execute(Tuple.of(record.getAlbumName(), record.getArtist(), record.getYear()))
                .onItem().transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));

    }

    //Get a Record per ID
    public Uni<Record> findById(long id) {
        String sql = "SELECT * FROM record WHERE id = $1";
        return client.preparedQuery(sql)
                .execute(Tuple.of(id))
                .onItem().transform(pgRowSet -> {
                    if (pgRowSet.rowCount() == 0) {
                        return null;
                    }
                    Row row = pgRowSet.iterator().next();
                    Record record = new Record();
                    record.setId(row.getLong("id"));
                    record.setAlbumName(row.getString("album_name"));
                    record.setArtist(row.getString("artist"));
                    record.setYear(row.getInteger("year"));
                    //handle genre as list if necessary
                    return record;
                });
    }

    //Get all Records
    public Uni<List<Record>> findAll() {
        String sql = "SELECT * FROM record";
        return client.query(sql)
                .execute()
                .onItem().transform(pgRowSet -> {
                    List<Record> records = new ArrayList<>();
                    pgRowSet.forEach(row -> {
                        Record record = new Record();
                        record.setId(row.getLong("id"));
                        record.setAlbumName(row.getString("album_name"));
                        record.setArtist(row.getString("artist"));
                        record.setYear(row.getInteger("year"));
                        records.add(record);
                    });
                    return records;
                });
    }

    //Delete a Record
    public Uni<Boolean> delete(Long id) {
        String sql = "DELETE FROM record WHERE id = $1";
        return client.preparedQuery(sql)
                .execute(Tuple.of(id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() > 0);
    }

}
