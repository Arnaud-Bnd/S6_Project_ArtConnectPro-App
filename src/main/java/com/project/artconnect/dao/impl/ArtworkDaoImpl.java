package com.project.artconnect.dao.impl;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtworkDaoImpl implements ArtworkDao {

    private static final String SELECT_ALL = """
        SELECT a.artwork_id, a.title, a.creation_year, a.medium, a.type,
               a.description, a.dimensions, a.price, a.status, a.artist_id
        FROM Artworks a
    """;

    private static final String SELECT_BY_ARTIST_NAME = """
        SELECT a.artwork_id, a.title, a.creation_year, a.medium, a.type,
               a.description, a.dimensions, a.price, a.status, a.artist_id
        FROM Artworks a
        JOIN Artists ar ON a.artist_id = ar.artist_id
        WHERE ar.name = ?
    """;

    private static final String INSERT = """
        INSERT INTO Artworks (title, creation_year, type, medium,
                              dimensions, description, price, status, artist_id)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String UPDATE = """
        UPDATE Artworks
        SET title = ?, creation_year = ?, type = ?, medium = ?,
            dimensions = ?, description = ?, price = ?, status = ?, artist_id = ?
        WHERE artwork_id = ?
    """;

    private static final String DELETE_BY_ID = """
        DELETE FROM Artworks
        WHERE artwork_id = ?
    """;

    private final ArtistDao artistDao = new ArtistDaoImpl();


    @Override
    public List<Artwork> findAll() {
        List<Artwork> artworks = new ArrayList<>();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                artworks.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Artwork - findAll", e);
        }

        return artworks;
    }


    @Override
    public void save(Artwork artwork) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            bindArtwork(ps, artwork);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    artwork.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Artwork - save", e);
        }
    }


    @Override
    public void update(Artwork artwork) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {

            bindArtwork(ps, artwork);
            ps.setInt(10, artwork.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Artwork - update", e);
        }
    }


    @Override
    public void delete(int artwork_id) {
        if (artwork_id <= 0) {
            throw new IllegalArgumentException("ID invalide");
        }

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_BY_ID)) {

            ps.setInt(1, artwork_id);

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new IllegalStateException("Aucune œuvre trouvée pour id : " + artwork_id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Artwork - delete", e);
        }
    }


    @Override
    public List<Artwork> findByArtistName(String artistName) {
        List<Artwork> artworks = new ArrayList<>();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ARTIST_NAME)) {

            ps.setString(1, artistName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    artworks.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Artwork - findByArtistName", e);
        }

        return artworks;
    }


    private Artwork mapRow(ResultSet rs) throws SQLException {
        Artwork a = new Artwork();

        a.setId(rs.getInt("artwork_id"));
        a.setTitle(rs.getString("title"));

        int year = rs.getInt("creation_year");
        if (!rs.wasNull()) {
            a.setCreationYear(year);
        }

        a.setType(rs.getString("type"));
        a.setMedium(rs.getString("medium"));
        a.setDimensions(rs.getString("dimensions"));
        a.setDescription(rs.getString("description"));
        a.setPrice(rs.getDouble("price"));

        String status = rs.getString("status");
        a.setStatus(Artwork.Status.valueOf(status));

        int artistId = rs.getInt("artist_id");
        if (!rs.wasNull()) {
            Artist artist = artistDao.findById(artistId);
            a.setArtist(artist);
        }

        return a;
    }


    private void bindArtwork(PreparedStatement ps, Artwork artwork) throws SQLException {

        ps.setString(1, artwork.getTitle());

        if (artwork.getCreationYear() != null) {
            ps.setInt(2, artwork.getCreationYear());
        } else {
            ps.setNull(2, Types.INTEGER);
        }

        ps.setString(3, artwork.getType());
        ps.setString(4, artwork.getMedium());
        ps.setString(5, artwork.getDimensions());
        ps.setString(6, artwork.getDescription());
        ps.setDouble(7, artwork.getPrice());
        ps.setString(8, artwork.getStatus().name());

        if (artwork.getArtist() != null && artwork.getArtist().getId() != null) {
            ps.setInt(9, artwork.getArtist().getId());
        } else {
            ps.setNull(9, Types.INTEGER);
        }
    }
}