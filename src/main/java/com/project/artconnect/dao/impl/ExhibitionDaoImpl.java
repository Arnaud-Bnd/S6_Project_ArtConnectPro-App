package com.project.artconnect.dao.impl;

import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExhibitionDaoImpl implements ExhibitionDao {

    private static final String SELECT_ALL = """
        SELECT exhibition_id, title, start_date, end_date,
               description, curator_name, theme, gallery_id
        FROM Exhibitions
    """;

    private static final String SELECT_BY_ID =
            SELECT_ALL + " WHERE exhibition_id = ?";

    private static final String INSERT = """
        INSERT INTO Exhibitions (title, start_date, end_date,
                                 description, curator_name, theme, gallery_id)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String UPDATE = """
        UPDATE Exhibitions
        SET title = ?, start_date = ?, end_date = ?,
            description = ?, curator_name = ?, theme = ?, gallery_id = ?
        WHERE exhibition_id = ?
    """;

    private static final String DELETE = """
        DELETE FROM Exhibitions
        WHERE exhibition_id = ?
    """;


    @Override
    public List<Exhibition> findAll() {
        List<Exhibition> exhibitions = new ArrayList<>();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                exhibitions.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Exhibition", e);
        }

        return exhibitions;
    }


    public Optional<Exhibition> findById(int id) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Exhibition", e);
        }

        return Optional.empty();
    }


    @Override
    public void save(Exhibition exhibition) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            bindExhibition(ps, exhibition);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    exhibition.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Exhibition", e);
        }
    }


    @Override
    public void update(Exhibition exhibition) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {

            bindExhibition(ps, exhibition);
            ps.setInt(8, exhibition.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Exhibition", e);
        }
    }


    @Override
    public void delete(int exhibition_id) {

        if (exhibition_id <= 0) {
            throw new IllegalArgumentException("ID invalide");
        }

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE)) {

            ps.setInt(1, exhibition_id);

            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new IllegalStateException(
                        "Aucune exposition trouvée pour id : " + exhibition_id
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Exhibition", e);
        }
    }


    private Exhibition mapRow(ResultSet rs) throws SQLException {

        Exhibition e = new Exhibition();

        e.setId(rs.getInt("exhibition_id"));
        e.setTitle(rs.getString("title"));

        Date start = rs.getDate("start_date");
        if (start != null) e.setStartDate(start.toLocalDate());

        Date end = rs.getDate("end_date");
        if (end != null) e.setEndDate(end.toLocalDate());

        e.setDescription(rs.getString("description"));
        e.setCuratorName(rs.getString("curator_name"));
        e.setTheme(rs.getString("theme"));

        int galleryId = rs.getInt("gallery_id");
        if (!rs.wasNull()) {
            Gallery g = new Gallery();
            g.setId(galleryId);
            e.setGallery(g);
        }

        return e;
    }


    private void bindExhibition(PreparedStatement ps, Exhibition e) throws SQLException {

        ps.setString(1, e.getTitle());

        if (e.getStartDate() != null) {
            ps.setDate(2, Date.valueOf(e.getStartDate()));
        } else {
            ps.setNull(2, Types.DATE);
        }

        if (e.getEndDate() != null) {
            ps.setDate(3, Date.valueOf(e.getEndDate()));
        } else {
            ps.setNull(3, Types.DATE);
        }

        ps.setString(4, e.getDescription());
        ps.setString(5, e.getCuratorName());
        ps.setString(6, e.getTheme());

        if (e.getGallery() != null && e.getGallery().getId() != null) {
            ps.setInt(7, e.getGallery().getId());
        } else {
            ps.setNull(7, Types.INTEGER);
        }
    }
}