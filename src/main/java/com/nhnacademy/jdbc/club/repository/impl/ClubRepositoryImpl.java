package com.nhnacademy.jdbc.club.repository.impl;

import com.nhnacademy.jdbc.club.domain.Club;
import com.nhnacademy.jdbc.club.repository.ClubRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ClubRepositoryImpl implements ClubRepository {

    @Override
    public Optional<Club> findByClubId(Connection connection, String clubId) {
        String sql ="select club_id,club_name, club_created_at from jdbc_club where club_id=? ";
        ResultSet rs = null;
        try(PreparedStatement psmt = connection.prepareStatement(sql)){
            psmt.setString(1,clubId);
            rs = psmt.executeQuery();
            if(rs.next()){
                return Optional.of(
                        new Club(rs.getString("club_id"),
                                rs.getString("club_name"),
                                rs.getTimestamp("club_created_at").toLocalDateTime()
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }

    @Override
    public int save(Connection connection, Club club) {
        String sql = "insert into jdbc_club set club_id=?, club_name=? ";

        try(PreparedStatement psmt = connection.prepareStatement(sql)){
            psmt.setString(1,club.getClubId());
            psmt.setString(2,club.getClubName());
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Connection connection, Club club) {

        String sql = "update jdbc_club set  club_name=? where club_id=?";

        try(PreparedStatement psmt = connection.prepareStatement(sql)){
            psmt.setString(1,club.getClubName());
            psmt.setString(2,club.getClubId());
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByClubId(Connection connection, String clubId) {

        String sql = "delete from jdbc_club where club_id=?";

        try(PreparedStatement psmt = connection.prepareStatement(sql)){
            psmt.setString(1,clubId);
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countByClubId(Connection connection, String clubId) {
        String sql = "select count(*) from jdbc_club where club_id=?";
        ResultSet rs = null;

        try(PreparedStatement psmt = connection.prepareStatement(sql)){
            psmt.setString(1,clubId);
            rs = psmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }
}
