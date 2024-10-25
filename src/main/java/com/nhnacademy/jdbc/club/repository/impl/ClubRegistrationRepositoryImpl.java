package com.nhnacademy.jdbc.club.repository.impl;

import com.nhnacademy.jdbc.club.domain.ClubStudent;
import com.nhnacademy.jdbc.club.repository.ClubRegistrationRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class ClubRegistrationRepositoryImpl implements ClubRegistrationRepository {

    @Override
    public int save(Connection connection, String studentId, String clubId) {
        //todo#11 - 핵생 -> 클럽 등록, executeUpdate() 결과를 반환
        int result = 0;
        String sql = "INSERT INTO jdbc_club_registrations (student_id,club_id) VALUES (?,?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, clubId);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int deleteByStudentIdAndClubId(Connection connection, String studentId, String clubId) {
        //todo#12 - 핵생 -> 클럽 탈퇴, executeUpdate() 결과를 반환
        int result = 0;
        String sql = "DELETE FROM jdbc_club_registrations WHERE student_id = ? AND club_id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, clubId);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public List<ClubStudent> findClubStudentsByStudentId(Connection connection, String studentId) {
        //todo#13 - 핵생 -> 클럽 등록, executeUpdate() 결과를 반환
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql = "SELECT s.id, s.name, c.club_id, c.club_name " +
                "FROM jdbc_club_registrations cr " +
                "JOIN jdbc_students s ON s.id = cr.student_id " +
                "JOIN jdbc_club c ON cr.club_id = c.club_id " +
                "WHERE cr.student_id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                clubStudents.add(new ClubStudent(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clubStudents;
    }

    @Override
    public List<ClubStudent> findClubStudents(Connection connection) {
        //todo#21 - join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql = "SELECT s.id, s.name, c.club_id, c.club_name " +
                "FROM jdbc_club_registrations cr " +
                "INNER JOIN jdbc_students s ON s.id = cr.student_id " +
                "INNER JOIN jdbc_club c ON cr.club_id = c.club_id";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                clubStudents.add(new ClubStudent(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clubStudents;
    }

    @Override
    public List<ClubStudent> findClubStudents_left_join(Connection connection) {
        //todo#22 - left join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql = "SELECT s.id, s.name, c.club_id, c.club_name " +
                "FROM jdbc_students s " +
                "LEFT JOIN jdbc_club_registrations cr ON s.id = cr.student_id " +
                "LEFT JOIN jdbc_club c ON cr.club_id = c.club_id";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                clubStudents.add(new ClubStudent(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clubStudents;
    }

    @Override
    public List<ClubStudent> findClubStudents_right_join(Connection connection) {
        //todo#23 - right join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql = "SELECT s.id, s.name, c.club_id, c.club_name " +
                "FROM jdbc_club_registrations cr " +
                "RIGHT JOIN jdbc_students s ON s.id = cr.student_id " +
                "RIGHT JOIN jdbc_club c ON cr.club_id = c.club_id";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                clubStudents.add(new ClubStudent(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clubStudents;
    }

    @Override
    public List<ClubStudent> findClubStudents_full_join(Connection connection) {
        //todo#24 - full join = left join union right join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql = "SELECT s.id, s.name, c.club_id, c.club_name " +
                "FROM jdbc_students s " +
                "LEFT JOIN jdbc_club_registrations cr ON s.id = cr.student_id " +
                "LEFT JOIN jdbc_club c ON cr.club_id = c.club_id " +
                "union " +
                "SELECT s.id, s.name, c.club_id, c.club_name " +
                "FROM jdbc_students s " +
                "RIGHT JOIN jdbc_club_registrations cr ON s.id = cr.student_id " +
                "RIGHT JOIN jdbc_club c ON cr.club_id = c.club_id ";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                clubStudents.add(new ClubStudent(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clubStudents;
    }

    @Override
    public List<ClubStudent> findClubStudents_left_excluding_join(Connection connection) {
        //todo#25 - left excluding join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql = "SELECT s.id, s.name, c.club_id, c.club_name " +
                "FROM jdbc_students s " +
                "LEFT JOIN jdbc_club_registrations cr ON s.id = cr.student_id " +
                "LEFT JOIN jdbc_club c ON cr.club_id = c.club_id " +
                "WHERE cr.club_id IS NULL";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                clubStudents.add(new ClubStudent(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clubStudents;
    }

    @Override
    public List<ClubStudent> findClubStudents_right_excluding_join(Connection connection) {
        //todo#26 - right excluding join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql = "SELECT s.id, s.name, c.club_id, c.club_name " +
                "FROM jdbc_club_registrations cr " +
                "RIGHT JOIN jdbc_students s ON s.id = cr.student_id " +
                "RIGHT JOIN jdbc_club c ON cr.club_id = c.club_id " +
                "WHERE cr.club_id IS NULL";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                clubStudents.add(new ClubStudent(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clubStudents;
    }

    @Override
    public List<ClubStudent> findClubStudents_outher_excluding_join(Connection connection) {
        //todo#27 - outher_excluding_join = left excluding join union right excluding join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql = "SELECT s.id, s.name, c.club_id, c.club_name " +
                "FROM jdbc_students s " +
                "LEFT JOIN jdbc_club_registrations cr ON s.id = cr.student_id " +
                "LEFT JOIN jdbc_club c ON c.club_id = cr.club_id " +
                "WHERE c.club_id IS NULL " +
                "UNION " +
                "SELECT s.id, s.name, c.club_id, c.club_name " +
                "FROM jdbc_students s " +
                "RIGHT JOIN jdbc_club_registrations cr ON cr.student_id = s.id " +
                "RIGHT JOIN jdbc_club c ON c.club_id = cr.club_id " +
                "WHERE s.id IS NULL";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                clubStudents.add(new ClubStudent(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clubStudents;
    }
}