package com.project.artconnect.persistence;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcWorkshopDao implements WorkshopDao {

    private static final String SELECT_ALL = """
        SELECT w.workshop_id, w.title, w.date_, w.duration_minutes,
               w.max_participants, w.price, w.location, w.description, w.level,
               a.artist_id, a.name, a.bio, a.birth_year, a.contact_email,
               a.phone, a.city, a.website, a.social_media, a.is_active
        FROM Workshops w
        LEFT JOIN Artists a ON w.instructor_id = a.artist_id
    """;

    private static final String SELECT_BY_ID =
            SELECT_ALL + " WHERE w.workshop_id = ?";


    @Override
    public List<Workshop> findAll() {

        List<Workshop> workshops = new ArrayList<>();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                workshops.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO JDBC Workshop - ", e);
        }

        return workshops;
    }


    @Override
    public Optional<Workshop> findById(int id) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO JDBC Workshop - ", e);
        }

        return Optional.empty();
    }


    private Workshop mapRow(ResultSet rs) throws SQLException {

        Workshop w = new Workshop();

        w.setId(rs.getInt("workshop_id"));
        w.setTitle(rs.getString("title"));

        Date date = rs.getDate("date_");
        if (date != null) {
            w.setDate(date.toLocalDate().atStartOfDay());
        }

        int duration = rs.getInt("duration_minutes");
        if (!rs.wasNull()) w.setDurationMinutes(duration);

        int max = rs.getInt("max_participants");
        if (!rs.wasNull()) w.setMaxParticipants(max);

        double price = rs.getDouble("price");
        if (!rs.wasNull()) w.setPrice(price);

        w.setLocation(rs.getString("location"));
        w.setDescription(rs.getString("description"));
        w.setLevel(rs.getString("level"));

        int artistId = rs.getInt("artist_id");

        if (!rs.wasNull()) {

            Artist a = new Artist();
            a.setId(artistId);
            a.setName(rs.getString("name"));
            a.setBio(rs.getString("bio"));

            int birthYear = rs.getInt("birth_year");
            if (!rs.wasNull()) a.setBirthYear(birthYear);

            a.setContactEmail(rs.getString("contact_email"));
            a.setPhone(rs.getString("phone"));
            a.setCity(rs.getString("city"));
            a.setWebsite(rs.getString("website"));
            a.setSocialMedia(rs.getString("social_media"));
            a.setActive(rs.getBoolean("is_active"));

            w.setInstructor(a);
        }

        return w;
    }
}