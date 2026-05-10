package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcArtworkDao implements ArtworkDao {

    private final JdbcArtistDao artistDao = new JdbcArtistDao();

    private static final String SELECT_ALL = """
        SELECT artwork_id, title, creation_year, medium, type,
               description, dimensions, price, status, artist_id
        FROM Artworks
    """;

    private static final String SELECT_BY_ID =
            SELECT_ALL + " WHERE artwork_id = ?";

    private static final String SELECT_BY_ARTIST_NAME = """
        SELECT aw.artwork_id, aw.title, aw.creation_year, aw.medium, aw.type,
               aw.description, aw.dimensions, aw.price, aw.status, aw.artist_id
        FROM Artworks aw
        JOIN Artists a ON aw.artist_id = a.artist_id
        WHERE a.name = ?
    """;

    private static final String INSERT = """
        INSERT INTO Artworks (title, creation_year, medium, type,
                              description, dimensions, price, status, artist_id)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String UPDATE = """
        UPDATE Artworks
        SET title = ?, creation_year = ?, medium = ?, type = ?,
            description = ?, dimensions = ?, price = ?, status = ?, artist_id = ?
        WHERE artwork_id = ?
    """;

    private static final String DELETE = """
        DELETE FROM Artworks
        WHERE artwork_id = ?
    """;


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
            throw new RuntimeException("Erreur DAO JDBC Artwork - ", e);
        }

        return artworks;
    }


    public Artwork findById(int artwork_id) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, artwork_id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO JDBC Artwork - ", e);
        }

        return null;
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
            throw new RuntimeException("Erreur DAO JDBC Artwork - ", e);
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
            throw new RuntimeException("Erreur DAO JDBC Artwork - ", e);
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
            throw new RuntimeException("Erreur DAO JDBC Artwork - ", e);
        }
    }


    @Override
    public void delete(int artwork_id) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE)) {

            ps.setInt(1, artwork_id);

            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new RuntimeException("Aucune œuvre trouvée pour id : " + artwork_id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO JDBC Artwork - ", e);
        }
    }


    private Artwork mapRow(ResultSet rs) throws SQLException {
        Artwork a = new Artwork();

        a.setId(rs.getInt("artwork_id"));
        a.setTitle(rs.getString("title"));

        int year = rs.getInt("creation_year");
        if (!rs.wasNull()) {
            a.setCreationYear(year);
        }

        a.setMedium(rs.getString("medium"));
        a.setType(rs.getString("type"));
        a.setDescription(rs.getString("description"));
        a.setDimensions(rs.getString("dimensions"));
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

        ps.setString(3, artwork.getMedium());
        ps.setString(4, artwork.getType());
        ps.setString(5, artwork.getDescription());
        ps.setString(6, artwork.getDimensions());
        ps.setDouble(7, artwork.getPrice());
        ps.setString(8, artwork.getStatus().name());

        if (artwork.getArtist() != null && artwork.getArtist().getId() != null) {
            ps.setInt(9, artwork.getArtist().getId());
        } else {
            ps.setNull(9, Types.INTEGER);
        }
    }
}