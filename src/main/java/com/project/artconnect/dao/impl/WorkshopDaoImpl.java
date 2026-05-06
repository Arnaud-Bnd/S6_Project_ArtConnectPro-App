package com.project.artconnect.dao.impl;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkshopDaoImpl implements WorkshopDao {

    private static final String BASE_QUERY = """
        SELECT workshop_id, title, date_, price, level,
               duration_minutes, max_participants, location, description
        FROM Workshops
    """;


    @Override
    public Optional<Workshop> findById(int id) {

        String sql = BASE_QUERY + " WHERE workshop_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Workshop", e);
        }

        return Optional.empty();
    }


    @Override
    public List<Workshop> findAll() {

        List<Workshop> workshops = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(BASE_QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                workshops.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Workshop", e);
        }

        return workshops;
    }


    private Workshop mapRow(ResultSet rs) throws SQLException {

        Workshop workshop = new Workshop();

        workshop.setId(rs.getInt("workshop_id"));
        workshop.setTitle(rs.getString("title"));

        Date date = rs.getDate("date_");
        if (date != null) {
            workshop.setDate(date.toLocalDate().atStartOfDay());
        }

        double price = rs.getDouble("price");
        if (!rs.wasNull()) {
            workshop.setPrice(price);
        }

        workshop.setLevel(rs.getString("level"));

        int duration = rs.getInt("duration_minutes");
        if (!rs.wasNull()) {
            workshop.setDurationMinutes(duration);
        }

        int max = rs.getInt("max_participants");
        if (!rs.wasNull()) {
            workshop.setMaxParticipants(max);
        }

        workshop.setLocation(rs.getString("location"));
        workshop.setDescription(rs.getString("description"));

        return workshop;
    }
}