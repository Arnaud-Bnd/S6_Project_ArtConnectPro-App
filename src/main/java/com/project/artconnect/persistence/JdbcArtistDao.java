package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcArtistDao implements ArtistDao {

    private static final String SELECT_ALL = """
        SELECT 
            a.artist_id,
            a.name,
            a.bio,
            a.birth_year,
            a.contact_email,
            a.phone,
            a.city,
            a.website,
            a.social_media,
            a.is_active,
            
            d.discipline_id,
            d.name AS discipline_name
    
        FROM artists a
        LEFT JOIN pratiques p ON a.artist_id = p.artist_id
        LEFT JOIN disciplines d ON p.discipline_id = d.discipline_id
    """;

    private static final String SELECT_BY_ID =
            SELECT_ALL + " WHERE a.artist_id = ?";

    private static final String SELECT_BY_CITY =
            SELECT_ALL + " WHERE a.city = ?";

    private static final String INSERT = """
        INSERT INTO artists (name, bio, birth_year, contact_email,
                             phone, city, website, social_media, is_active)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String UPDATE = """
        UPDATE artists
        SET name = ?, bio = ?, birth_year = ?, contact_email = ?,
            phone = ?, city = ?, website = ?, social_media = ?, is_active = ?
        WHERE artist_id = ?
    """;

    private static final String DELETE = """
        DELETE FROM artists
        WHERE artist_id = ?
    """;

    @Override
    public List<Artist> findAll() {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            return extractArtists(rs);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO JDBC Artist", e);
        }
    }


    @Override
    public Artist findById(int artist_id) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, artist_id);

            try (ResultSet rs = ps.executeQuery()) {

                List<Artist> list = extractArtists(rs);
                return list.isEmpty() ? null : list.get(0);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO JDBC Artist", e);
        }
    }


    @Override
    public List<Artist> findByCity(String city) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_CITY)) {

            ps.setString(1, city);

            try (ResultSet rs = ps.executeQuery()) {
                return extractArtists(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO JDBC Artist", e);
        }
    }


    @Override
    public void save(Artist artist) {
        Connection con = null;

        try {
            con = ConnectionManager.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            bindArtist(ps, artist);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                artist.setId(keys.getInt(1));
            }
            if (artist.getDisciplines() != null) {
                for (Discipline d : artist.getDisciplines()) {

                    PreparedStatement ps2 = con.prepareStatement("INSERT INTO pratiques (artist_id, discipline_id) VALUES (?, ?)");

                    ps2.setInt(1, artist.getId());
                    ps2.setInt(2, d.getId());

                    ps2.executeUpdate();
                }
            }

            con.commit();

        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erreur DAO JDBC Artist", e);

        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void update(Artist artist) {

        Connection con = null;

        try {
            con = ConnectionManager.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(UPDATE);
            bindArtist(ps, artist);
            ps.setInt(10, artist.getId());
            ps.executeUpdate();

            PreparedStatement del = con.prepareStatement("DELETE FROM pratiques WHERE artist_id = ?");
            del.setInt(1, artist.getId());
            del.executeUpdate();

            if (artist.getDisciplines() != null) {
                for (Discipline d : artist.getDisciplines()) {

                    PreparedStatement ins = con.prepareStatement("INSERT INTO pratiques (artist_id, discipline_id) VALUES (?, ?)");

                    ins.setInt(1, artist.getId());
                    ins.setInt(2, d.getId());

                    ins.executeUpdate();
                }
            }

            con.commit();

        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erreur DAO JDBC Artist - update", e);

        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void delete(int artist_id) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE)) {

            ps.setInt(1, artist_id);

            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new IllegalStateException(
                        "Aucun artiste trouvé pour id : " + artist_id);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur suppression artiste : " + e.getMessage(), e);
        }
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

        a.setDisciplines(new ArrayList<>());
        int disciplineId = rs.getInt("discipline_id");
        if (!rs.wasNull()) {
            Discipline d = new Discipline();

            d.setId(disciplineId);
            d.setName(rs.getString("discipline_name"));
            a.getDisciplines().add(d);
        }

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

    private List<Artist> extractArtists(ResultSet rs) throws SQLException {
        Map<Integer, Artist> map = new HashMap<>();

        while (rs.next()) {

            int id = rs.getInt("artist_id");

            Artist a = map.get(id);

            if (a == null) {
                a = new Artist();

                a.setId(id);
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

                a.setDisciplines(new ArrayList<>());

                map.put(id, a);
            }

            int disciplineId = rs.getInt("discipline_id");

            if (!rs.wasNull()) {
                Discipline d = new Discipline();
                d.setId(disciplineId);
                d.setName(rs.getString("discipline_name"));
                a.getDisciplines().add(d);
            }
        }

        return new ArrayList<>(map.values());
    }
}