package org.ms.record.entity;

import io.smallrye.mutiny.Uni;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import java.util.List;

@Entity
public class Record extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String albumName;
    public String artist;
    public int year;
    public String genre;

    // Buscar todos los registros de forma reactiva
    public static Uni<List<Record>> findAllRecords() {
        return findAll().list(); // Devuelve una lista reactiva de registros
    }

    // Persistir el registro de forma reactiva
    public Uni<Void> persistRecord() {
        return persist().replaceWithVoid(); // Devuelve Uni<Void> para indicar que la persistencia ha terminado
    }

    // Buscar un registro por su id de forma reactiva
    public static Uni<Record> findById(Long id) {
        return find("id", id).firstResult(); // Devuelve Uni<Record>, puede ser null si no existe
    }

    // Eliminar el registro de forma reactiva
    public Uni<Void> deleteRecord() {
        return delete().replaceWithVoid(); // Devuelve Uni<Void> para indicar que la eliminación ha terminado
    }

    // Métodos setters y getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
