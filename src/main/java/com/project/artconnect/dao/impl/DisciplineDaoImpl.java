package com.project.artconnect.dao.impl;

import com.project.artconnect.model.Discipline;
import com.project.artconnect.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisciplineDaoImpl {

    private static final String SELECT_ALL = """
        SELECT discipline_id, name
        FROM Disciplines
    """;

    public List<Discipline> findAll() {
        List<Discipline> disciplines = new ArrayList<>();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                disciplines.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO Discipline - findAll", e);
        }

        return disciplines;
    }

    private Discipline mapRow(ResultSet rs) throws SQLException {
        Discipline d = new Discipline();

        d.setId(rs.getInt("discipline_id"));
        d.setName(rs.getString("name"));

        return d;
    }
}