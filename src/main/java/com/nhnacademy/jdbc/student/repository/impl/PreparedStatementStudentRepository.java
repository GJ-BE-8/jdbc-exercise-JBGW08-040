package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
public class PreparedStatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student) {
        //todo#1 학생 등록
        int result = 0;
        String sql = "INSERT INTO jdbc_students (id, name, gender, age, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DbUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, student.getId());
            preparedStatement.setString(2, student.getName());
            preparedStatement.setString(3, student.getGender().toString());
            preparedStatement.setInt(4, student.getAge());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
            preparedStatement.setTimestamp(5, Timestamp.valueOf(student.getCreatedAt().format(formatter)));

            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Optional<Student> findById(String id) {
        //todo#2 학생 조회
        Optional<Student> student = Optional.empty();
        String sql = "SELECT * FROM jdbc_students WHERE id=?";
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1,id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()){
                log.debug("test: {}",result.getString("id"));
                student = Optional.of(new Student(
                        result.getString("id"),
                        result.getString("name"),
                        Student.GENDER.valueOf(result.getString("gender")),
                        result.getInt("age")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return student;

    }

    @Override
    public int update(Student student) {
        //todo#3 학생 수정 , name 수정

        int result = 0;
        String sql = "UPDATE jdbc_students SET id=?, name=?,gender=?,age=?,created_at=? WHERE id=?";
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1,student.getId());
            preparedStatement.setString(2,student.getName());
            preparedStatement.setString(3,student.getGender().toString());
            preparedStatement.setInt(4,student.getAge());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
            preparedStatement.setTimestamp(5,Timestamp.valueOf(student.getCreatedAt().format(formatter)));
            preparedStatement.setString(6,student.getId());

            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    @Override
    public int deleteById(String id) {
        //todo#4 학생 삭제
        int result = 0;
        String sql = "DELETE FROM jdbc_students WHERE id = ?";
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1,id);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
