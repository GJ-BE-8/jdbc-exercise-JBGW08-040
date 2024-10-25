package com.nhnacademy.jdbc.bank.service.impl;

import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.exception.AccountAreadyExistException;
import com.nhnacademy.jdbc.bank.exception.AccountNotFoundException;
import com.nhnacademy.jdbc.bank.exception.BalanceNotEnoughException;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;
import com.nhnacademy.jdbc.bank.repository.impl.AccountRepositoryImpl;
import com.nhnacademy.jdbc.bank.service.BankService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository;

    public BankServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccount(Connection connection, long accountNumber) {
        //todo#11 계좌-조회
        return accountRepository.findByAccountNumber(connection, accountNumber).get();
    }

    @Override
    public void createAccount(Connection connection, Account account) {
        //todo#12 계좌-등록
        if (!isExistAccount(connection, account.getAccountNumber())) {
            accountRepository.save(connection, account);
        } else {
            throw new AccountAreadyExistException(account.getAccountNumber());
        }
    }

    @Override
    public boolean depositAccount(Connection connection, long accountNumber, long amount) {
        //todo#13 예금, 계좌가 존재하는지 체크 -> 예금실행 -> 성공 true, 실패 false;
        if (isExistAccount(connection, accountNumber)) {
            if (accountRepository.deposit(connection, accountNumber, amount) == 1) {
                return true;
            }
        } else {
            throw new AccountNotFoundException(accountNumber);
        }
        return false;
    }

    @Override
    public boolean withdrawAccount(Connection connection, long accountNumber, long amount) {
        //todo#14 출금, 계좌가 존재하는지 체크 ->  출금가능여부 체크 -> 출금실행, 성공 true, 실폐 false 반환
        if (!isExistAccount(connection, accountNumber)) throw new AccountNotFoundException(accountNumber);

        Optional<Account> account = accountRepository.findByAccountNumber(connection, accountNumber);

        if (account.get().getBalance() < amount) throw new BalanceNotEnoughException(account.get().getAccountNumber());

        accountRepository.withdraw(connection, accountNumber, amount);
        return true;
    }

    @Override
    public void transferAmount(Connection connection, long accountNumberFrom, long accountNumberTo, long amount) {
        //todo#15 계좌 이체 accountNumberFrom -> accountNumberTo 으로 amount만큼 이체
        if (!isExistAccount(connection, accountNumberFrom)) throw new AccountNotFoundException(accountNumberFrom);
        if (!isExistAccount(connection, accountNumberTo)) throw new AccountNotFoundException(accountNumberTo);

        Optional<Account> fromAccount = accountRepository.findByAccountNumber(connection, accountNumberFrom);
        Optional<Account> toAccount = accountRepository.findByAccountNumber(connection, accountNumberTo);

        if (fromAccount.get().getBalance() < amount) {
            throw new BalanceNotEnoughException(fromAccount.get().getAccountNumber());
        }

        accountRepository.withdraw(connection, accountNumberFrom, amount);
        accountRepository.deposit(connection, accountNumberTo, amount);
    }

    @Override
    public boolean isExistAccount(Connection connection, long accountNumber) {
        //todo#16 Account가 존재하면 true , 존재하지 않다면 false
        return accountRepository.countByAccountNumber(connection, accountNumber) == 1;
    }

    @Override
    public void dropAccount(Connection connection, long accountNumber) {
        //todo#17 account 삭제
        if (!isExistAccount(connection, accountNumber))
        accountRepository.deleteByAccountNumber(connection, accountNumber);

        if (accountRepository.deleteByAccountNumber(connection, accountNumber) ==0){
            throw new AccountNotFoundException(accountNumber);
        }
    }
}