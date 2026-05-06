package com.project.artconnect.dao.impl;

import com.project.artconnect.dao.GalleryDao;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GalleryDaoImpl implements GalleryDao {

    private static final String SELECT_ALL = """
        SELECT gallery_id, name, address, owner_name,
               opening_hours, contact_phone, rating, website
        FROM Galleries
    """;

    private static final String SELECT_BY_ID =
            SELECT_ALL + " WHERE gallery_id = ?";


    @Override
    public List<Gallery> findAll() {

        List<Gallery> galleries = new ArrayList<>();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                galleries.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Gallery", e);
        }

        return galleries;
    }


    @Override
    public Optional<Gallery> findById(int id) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Gallery", e);
        }

        return Optional.empty();
    }


    private Gallery mapRow(ResultSet rs) throws SQLException {

        Gallery g = new Gallery();

        g.setId(rs.getInt("gallery_id"));
        g.setName(rs.getString("name"));
        g.setAddress(rs.getString("address"));
        g.setOwnerName(rs.getString("owner_name"));
        g.setOpeningHours(rs.getString("opening_hours"));
        g.setContactPhone(rs.getString("contact_phone"));
        g.setWebsite(rs.getString("website"));

        double rating = rs.getDouble("rating");
        if (!rs.wasNull()) {
            g.setRating(rating);
        }

        return g;
    }
}