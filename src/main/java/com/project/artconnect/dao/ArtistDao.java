package com.project.artconnect.dao;

import com.project.artconnect.model.Artist;

import java.util.List;

/**
 * Data Access Object for Artist entity.
 */
public interface ArtistDao {
    List<Artist> findAll();

    void save(Artist artist);

    void update(Artist artist);

    void delete(int artist_id);

    List<Artist> findByCity(String city);

    Artist findById(int artist_id);
}
