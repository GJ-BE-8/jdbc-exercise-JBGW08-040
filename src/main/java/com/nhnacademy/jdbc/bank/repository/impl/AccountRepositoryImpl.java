package com.nhnacademy.jdbc.bank.repository.impl;

import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {

    public Optional<Account> findByAccountNumber(Connection connection, long accountNumber){
        //todo#1 계좌-조회
        Account account = null;
        String sql = "SELECT * FROM jdbc_account WHERE account_number=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setLong(1,accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                account = new Account(
                        resultSet.getLong("account_number"),
                        resultSet.getString("name"),
                        resultSet.getLong("balance")
                        );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(account);
    }

    @Override
    public int save(Connection connection, Account account) {
        //todo#2 계좌-등록, executeUpdate() 결과를 반환 합니다.
        int result = 0;
        String sql = "INSERT INTO jdbc_account (account_number, name, balance) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setLong(1,account.getAccountNumber());
            preparedStatement.setString(2,account.getName());
            preparedStatement.setLong(3,account.getBalance());
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int countByAccountNumber(Connection connection, long accountNumber){
        //todo#3 select count(*)를 이용해서 계좌의 개수를 count해서 반환
        int count = 0;
        String sql = "SELECT COUNT(*) FROM jdbc_account WHERE account_number=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setLong(1,accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    @Override
    public int deposit(Connection connection, long accountNumber, long amount){
        //todo#4 입금, executeUpdate() 결과를 반환 합니다.
        int result = 0;
        Optional<Account> account = findByAccountNumber(connection, accountNumber);
        if (!account.isPresent()){
            return result;
        }

        String sql = "UPDATE jdbc_account SET balance= ? WHERE account_number=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setLong(1,account.get().getBalance() + amount);
            preparedStatement.setLong(2,accountNumber);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int withdraw(Connection connection, long accountNumber, long amount){
        //todo#5 출금, executeUpdate() 결과를 반환 합니다.
        int result = 0;
        String sql = "UPDATE jdbc_account SET balance = balance - ? WHERE account_number=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setLong(1,amount);
            preparedStatement.setLong(2,accountNumber);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int deleteByAccountNumber(Connection connection, long accountNumber) {
        //todo#6 계좌 삭제, executeUpdate() 결과를 반환 합니다.
        int result = 0;
        String sql = "DELETE FROM jdbc_account WHERE account_number=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setLong(1,accountNumber);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
