package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal getBalance(int id) {
        Account account = new Account();
        String url = "SELECT * FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(url, id);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account.getBalance();
    }

    public int getAccount(int id) {
        Account account = new Account();
        String sql = "SELECT * FROM account WHERE user_id = ?";
        SqlRowSet rowset = jdbcTemplate.queryForRowSet(sql, id);
        if (rowset.next()) {
            account = mapRowToAccount(rowset);
        }
        return account.getAccount_id();
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccount_id(rs.getInt("account_id"));
        account.setUser_id(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }

}
