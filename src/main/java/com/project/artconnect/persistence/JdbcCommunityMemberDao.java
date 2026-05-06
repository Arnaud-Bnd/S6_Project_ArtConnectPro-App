package com.project.artconnect.persistence;

import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCommunityMemberDao implements CommunityMemberDao {

    private static final String SELECT_ALL = """
        SELECT member_id, name, email, birth_year,
               phone, city, membership_type
        FROM Community_members
    """;

    private static final String SELECT_BY_ID = """
        SELECT member_id, name, email, birth_year,
               phone, city, membership_type
        FROM Community_members
        WHERE member_id = ?
    """;


    @Override
    public List<CommunityMember> findAll() {

        List<CommunityMember> members = new ArrayList<>();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                members.add(mapMember(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO JDBC CommunityMember - ", e);
        }

        return members;
    }


    @Override
    public Optional<CommunityMember> findById(Integer id) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapMember(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DAO JDBC CommunityMember - ", e);
        }

        return Optional.empty();
    }


    private CommunityMember mapMember(ResultSet rs) throws SQLException {

        CommunityMember member = new CommunityMember();

        member.setId(rs.getInt("member_id"));
        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));

        int birthYear = rs.getInt("birth_year");
        if (!rs.wasNull()) {
            member.setBirthYear(birthYear);
        }

        member.setPhone(rs.getString("phone"));
        member.setCity(rs.getString("city"));
        member.setMembershipType(rs.getString("membership_type"));

        return member;
    }
}