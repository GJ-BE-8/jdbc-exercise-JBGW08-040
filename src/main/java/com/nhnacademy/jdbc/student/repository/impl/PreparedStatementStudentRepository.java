package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Optional;

@Slf4j
public class PreparedStatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student){
        //todo#1 학생 등록
        int result = 0;
        String sql = "INSERT INTO jdbc_students (id,name,gender,age,created_at) VALUES ?,?,?,?,?";
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setString(1,student.getId());
            preparedStatement.setString(2,student.getName());
            preparedStatement.setString(3,student.getGender().toString());
            preparedStatement.setString(4,student.getId());
            preparedStatement.setString(5,student.getId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Optional<Student> findById(String id){
        //todo#2 학생 조회
        return Optional.empty();
    }

    @Override
    public int update(Student student){
        //todo#3 학생 수정 , name 수정

        return 0;
    }

    @Override
    public int deleteById(String id){
        //todo#4 학생 삭제
        return 0;
    }

}
