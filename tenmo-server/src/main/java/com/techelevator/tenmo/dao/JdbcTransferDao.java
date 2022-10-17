package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getTransferHistory(int id) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer.transfer_type_id, transfer_type_desc, transfer.transfer_status_id, transfer_status_desc, fuser.username as username_from, fuser.user_id as user_id_from, account_from, tuser.username as username_to, tuser.user_id as user_id_to, account_to, amount " +
                "FROM transfer " +
                "JOIN transfer_type using(transfer_type_id) " +
                "JOIN transfer_status using(transfer_status_id) " +
                "JOIN account faccount ON transfer.account_from = faccount.account_id " +
                "JOIN tenmo_user fuser on fuser.user_id = faccount.user_id " +
                "JOIN account taccount ON transfer.account_to = taccount.account_id " +
                "JOIN tenmo_user tuser on tuser.user_id = taccount.user_id " +
                "WHERE fuser.user_id = ? OR tuser.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public boolean createSend(Transfer transfer) {
        String sql = "INSERT INTO transfer(transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES(DEFAULT, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, transfer.getTransfer_type_id(), transfer.getTransfer_status_id(), transfer.getAccount_from(), transfer.getAccount_to(), transfer.getAmount());

        sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccount_to());

        sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccount_from());
        return true;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {

        Transfer transfer = new Transfer();

        transfer.setTransfer_id(rs.getInt("transfer_id"));

        transfer.setTransfer_type_id(rs.getInt("transfer_type_id"));
        transfer.setTransfer_status_id(rs.getInt("transfer_status_id"));

        transfer.setTransfer_type_desc(rs.getString("transfer_type_desc"));
        transfer.setTransfer_status_desc(rs.getString("transfer_status_desc"));

        transfer.setUsername_from(rs.getString("username_from"));
        transfer.setUser_id_from(rs.getInt("user_id_from"));
        transfer.setAccount_from(rs.getInt("account_from"));

        transfer.setUsername_to(rs.getString("username_to"));
        transfer.setUser_id_to(rs.getInt("user_id_to"));
        transfer.setAccount_to(rs.getInt("account_to"));

        transfer.setAmount(rs.getDouble("amount"));

        return transfer;
    }
}
