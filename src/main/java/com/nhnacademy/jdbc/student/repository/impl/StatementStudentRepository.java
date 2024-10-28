package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.html.Option;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
public class StatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student){
        //todo#1 insert student
        String sql = String.format("insert into jdbc_students (id, name, gender,age,created_at) values ('%s','%s','%s','%d','%s')",
                student.getId(),
                student.getName(),
                student.getGender(),
                student.getAge(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ){
            int result = statement.executeUpdate(sql);
            log.debug("save-result:{}", result);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Student> findById(String id){
        //todo#2 student 조회
        String sql = String.format("select id, name, gender,age from jdbc_students where id='%s'", id);

        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
        ) {
            if(rs.next()){
                return Optional.of(
                        new Student(
                                rs.getString("id"),
                                rs.getString("name"),
                                Student.GENDER.valueOf(rs.getString("gender")),
                                rs.getInt("age")
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int update(Student student){
        //todo#3 student 수정, name <- 수정합니다.
        String sql = String.format("update jdbc_students set id='%s', name='%s', gender='%s', age='%d',created_at='%s' where id='%s'",
                student.getId(),
                student.getName(),
                student.getGender().toString(),
                student.getAge(),
                student.getCreatedAt().toString(),
                student.getId()
                );
        log.debug("sql:{}",sql);
        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ){
            int result = statement.executeUpdate(sql);
            log.debug("updateUserPasswordByUserId : {}", result);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(String id){
       //todo#4 student 삭제
        String sql = String.format("delete from jdbc_students where id='%s'", id);
        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ){
            int result = statement.executeUpdate(sql);
            log.debug("result:{}", result);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
