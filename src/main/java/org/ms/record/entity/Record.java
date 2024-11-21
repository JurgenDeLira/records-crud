package org.ms.record.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Record extends PanacheEntity {



    @NotBlank
    private String albumName;

    @NotBlank
    private String artist;

    @Min(1900)
    @Max(2100)
    private int year;

    @NotBlank
    private String genre;

    @Enumerated(EnumType.STRING)
    private Format format;

    // Getters and setters

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

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }
}

enum Format {
    VINYL, CD, DIGITAL, CASSETTE, EIGHT_TRACK
}