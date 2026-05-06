package com.project.artconnect.dao.impl;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistDaoImpl implements ArtistDao {

    private static final String SELECT_ALL = """
        SELECT artist_id, name, bio, birth_year, contact_email,
               phone, city, website, social_media, is_active
        FROM Artists
    """;

    private static final String SELECT_BY_ID =
            SELECT_ALL + " WHERE artist_id = ?";

    private static final String SELECT_BY_CITY =
            SELECT_ALL + " WHERE city = ?";

    private static final String INSERT = """
        INSERT INTO Artists (name, bio, birth_year, contact_email,
                             phone, city, website, social_media, is_active)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String UPDATE = """
        UPDATE Artists
        SET name = ?, bio = ?, birth_year = ?, contact_email = ?,
            phone = ?, city = ?, website = ?, social_media = ?, is_active = ?
        WHERE artist_id = ?
    """;

    private static final String DELETE = """
        DELETE FROM Artists
        WHERE artist_id = ?
    """;


    @Override
    public List<Artist> findAll() {
        List<Artist> artists = new ArrayList<>();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                artists.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Artist - findAll", e);
        }

        return artists;
    }


    @Override
    public void save(Artist artist) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            bindArtist(ps, artist);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    artist.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Artist - save", e);
        }
    }


    @Override
    public void update(Artist artist) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {

            bindArtist(ps, artist);
            ps.setInt(10, artist.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Artist - update", e);
        }
    }


    @Override
    public void delete(int artist_id) {
        if (artist_id <= 0) {
            throw new IllegalArgumentException("ID invalide");
        }

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE)) {

            ps.setInt(1, artist_id);

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new IllegalStateException("Aucun artiste trouvé pour id : " + artist_id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Artist - delete", e);
        }
    }


    @Override
    public List<Artist> findByCity(String city) {
        List<Artist> artists = new ArrayList<>();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_CITY)) {

            ps.setString(1, city);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    artists.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Artist - findByCity", e);
        }

        return artists;
    }


    @Override
    public Artist findById(int artist_id) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, artist_id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Artist - findById", e);
        }

        throw new RuntimeException("Artist not found");
    }


    private Artist mapRow(ResultSet rs) throws SQLException {
        Artist a = new Artist();

        a.setId(rs.getInt("artist_id"));
        a.setName(rs.getString("name"));
        a.setBio(rs.getString("bio"));

        int birthYear = rs.getInt("birth_year");
        if (!rs.wasNull()) {
            a.setBirthYear(birthYear);
        }

        a.setContactEmail(rs.getString("contact_email"));
        a.setPhone(rs.getString("phone"));
        a.setCity(rs.getString("city"));
        a.setWebsite(rs.getString("website"));
        a.setSocialMedia(rs.getString("social_media"));
        a.setActive(rs.getBoolean("is_active"));

        return a;
    }


    private void bindArtist(PreparedStatement ps, Artist artist) throws SQLException {

        ps.setString(1, artist.getName());
        ps.setString(2, artist.getBio());

        if (artist.getBirthYear() != null) {
            ps.setInt(3, artist.getBirthYear());
        } else {
            ps.setNull(3, Types.INTEGER);
        }

        ps.setString(4, artist.getContactEmail());
        ps.setString(5, artist.getPhone());
        ps.setString(6, artist.getCity());
        ps.setString(7, artist.getWebsite());
        ps.setString(8, artist.getSocialMedia());
        ps.setBoolean(9, artist.isActive());
    }
}