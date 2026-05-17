package com.project.artconnect.persistence;

import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcExhibitionDao implements ExhibitionDao {

    private static final String SELECT_ALL = """
        SELECT e.exhibition_id, e.title, e.start_date, e.end_date,
               e.description, e.curator_name, e.theme,
               g.gallery_id, g.name AS gallery_name
        FROM Exhibitions e
        LEFT JOIN Galleries g ON e.gallery_id = g.gallery_id
    """;

    private static final String SELECT_BY_ID =
            SELECT_ALL + " WHERE exhibition_id = ?";

    private static final String INSERT = """
        INSERT INTO Exhibitions (title, start_date, end_date,
                                description, gallery_id, curator_name, theme)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String UPDATE = """
        UPDATE Exhibitions
        SET title = ?, start_date = ?, end_date = ?,
            description = ?, gallery_id = ?, curator_name = ?, theme = ?
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
            throw new RuntimeException("Erreur DAO JDBC Exhibition - ", e);
        }

        return exhibitions;
    }


    @Override
    public void save(Exhibition e) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            bind(ps, e);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    e.setId(rs.getInt(1));
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Erreur DAO JDBC Exhibition - ", ex);
        }
    }


    @Override
    public void update(Exhibition e) {

        if (e.getId() == null) {
            throw new IllegalArgumentException("ID exhibition manquant");
        }

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {

            bind(ps, e);
            ps.setInt(8, e.getId());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Erreur DAO JDBC Exhibition - ", ex);
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
                throw new RuntimeException("Aucune exposition trouvée pour id : " + exhibition_id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO JDBC Exhibition - ", e);
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
            g.setName(rs.getString("gallery_name"));
            e.setGallery(g);
        }

        return e;
    }


    private void bind(PreparedStatement ps, Exhibition e) throws SQLException {

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

        if (e.getGallery() != null && e.getGallery().getId() != null) {
            ps.setInt(5, e.getGallery().getId());
        } else {
            ps.setNull(5, Types.INTEGER);
        }

        ps.setString(6, e.getCuratorName());
        ps.setString(7, e.getTheme());
    }
}