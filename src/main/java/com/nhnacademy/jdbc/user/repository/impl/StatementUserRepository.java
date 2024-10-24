package com.nhnacademy.jdbc.user.repository.impl;

import com.nhnacademy.jdbc.user.domain.User;
import com.nhnacademy.jdbc.user.repository.UserRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Optional;

@Slf4j
public class StatementUserRepository implements UserRepository {

    @Override
    public Optional<User> findByUserIdAndUserPassword(String userId, String userPassword) {
        //todo#1 아이디, 비밀번호가 일치하는 User 조회
        String sql = "SELECT * FROM jdbc_users WHERE user_id = ? AND user_password = ?";
        User user = null;
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, userPassword);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    user = new User(resultSet.getString("user_id"),
                            resultSet.getString("user_name"),
                            resultSet.getString("user_password"));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findById(String userId) {
        //#todo#2-아이디로 User 조회
        String sql = "SELECT * FROM jdbc_users WHERE suer_id =?";
        User user = null;
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, userId);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    user = new User(resultSet.getString("user_id"),
                            resultSet.getString("user_name"),
                            resultSet.getString("user_password"));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public int save(User user) {
        //todo#3- User 저장
        int result = 0;

        String sql = "INSERT INTO jdbc_users (user_id, user_name,user_password) " +
                "VALUES (?,?,?)";
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1,user.getUserId());
            preparedStatement.setString(2,user.getUserName());
            preparedStatement.setString(3,user.getUserPassword());
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int updateUserPasswordByUserId(String userId, String userPassword) {
        //todo#4-User 비밀번호 변경
        int result = 0;
        String sql = "UPDATE jdbc_users SET user_password=? WHERE id = ?";
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1,userPassword);
            preparedStatement.setString(2,userId);

            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int deleteByUserId(String userId) {
        //todo#5 - User 삭제
        int result = 0;
        String sql = "DELETE FROM jdbc_users WHERE id = ?";
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1,userId);

            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
