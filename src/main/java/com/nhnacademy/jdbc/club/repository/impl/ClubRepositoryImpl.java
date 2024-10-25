package com.nhnacademy.jdbc.club.repository.impl;

import com.nhnacademy.jdbc.club.domain.Club;
import com.nhnacademy.jdbc.club.repository.ClubRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class ClubRepositoryImpl implements ClubRepository {

    @Override
    public Optional<Club> findByClubId(Connection connection, String clubId) {
        //todo#3 club 조회
        Club club = null;
        String sql = "SELECT * FROM jdbc_club WHERE club_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, clubId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                club = new Club(
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name"),
                        resultSet.getTimestamp("club_created_at").toLocalDateTime()
                        );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(club);
    }

    @Override
    public int save(Connection connection, Club club) {
        //todo#4 club 생성, executeUpdate() 결과를 반환
        int result = 0;
        String sql = "INSERT INTO jdbc_club (club_id,club_name,club_created_at) VALUES (?,?,?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, club.getClubId());
            preparedStatement.setString(2, club.getClubName());
            preparedStatement.setString(3, club.getClubCreatedAt().toString());
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int update(Connection connection, Club club) {
        //todo#5 club 수정, clubName을 수정합니다. executeUpdate()결과를 반환
        int result = 0;
        String sql = "UPDATE jdbc_club SET club_id=?,club_name=?,club_created_at=? WHERE club_id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, club.getClubId());
            preparedStatement.setString(2, club.getClubName());
            preparedStatement.setString(3, club.getClubCreatedAt().toString());
            preparedStatement.setString(4, club.getClubId());
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int deleteByClubId(Connection connection, String clubId) {
        //todo#6 club 삭제, executeUpdate()결과 반환
        int result = 0;
        String sql = "DELETE FROM jdbc_club WHERE club_id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, clubId);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int countByClubId(Connection connection, String clubId) {
        //todo#7 clubId에 해당하는 club의 count를 반환
        int count = 0;
        String sql = "SELECT COUNT(*) FROM jdbc_club WHERE club_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, clubId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }
}
