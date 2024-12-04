package org.ms.record.service;

import io.smallrye.mutiny.Uni;
import org.ms.record.entity.Record;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RecordService {

    // Obtener todos los registros de manera reactiva
    public Uni<List<Record>> getAllRecords() {
        return Record.findAllRecords();  // Método reactivo en la entidad Record
    }

    // Obtener un registro por ID de manera reactiva
    public Uni<Record> getRecordById(Long id) {
        return Record.findById(id);  // Devuelve un Uni con el registro o null si no existe
    }

    // Crear un registro de manera reactiva
    public Uni<Record> createRecord(Record record) {
        return record.persistRecord()  // Persistir el registro de manera reactiva
                .replaceWith(record);  // Devuelve el mismo objeto persistido
    }

    // Actualizar un registro por ID de manera reactiva
    public Uni<Record> updateRecord(Long id, Record record) {
        return Record.findById(id)
                .onItem().ifNull().failWith(new WebApplicationException("Record with id " + id + " not found", 404))
                .onItem().transform(entity -> {
                    entity.setAlbumName(record.getAlbumName());
                    entity.setArtist(record.getArtist());
                    entity.setYear(record.getYear());
                    entity.setGenre(record.getGenre());
                    return entity;
                })
                .call(Record::persistRecord);  // Persistir los cambios de manera reactiva
    }

    // Eliminar un registro por ID de manera reactiva
    public Uni<Void> deleteRecord(Long id) {
        return Record.findById(id)
                .onItem().ifNull().failWith(new WebApplicationException("Record with id " + id + " not found", 404))
                .call(Record::delete);  // Eliminar el registro de manera reactiva
    }
}
