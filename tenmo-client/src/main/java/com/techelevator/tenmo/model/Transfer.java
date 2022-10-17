package com.techelevator.tenmo.model;

public class Transfer {
    private int transfer_id;
    private int transfer_type_id;
    private int transfer_status_id;
    private String transfer_type_desc;
    private String transfer_status_desc;
    private int account_from;
    private int account_to;
    private String username_from;
    private String username_to;
    private int user_id_from;
    private int user_id_to;
    private double amount;

    public Transfer() {
    }

    public Transfer(int transfer_id, int transfer_type_id, int transfer_status_id, String transfer_type_desc, String transfer_status_desc, int account_from, int account_to, String username_from, String username_to, int user_id_from, int user_id_to, double amount) {

        this.transfer_id = transfer_id;

        this.transfer_type_id = transfer_type_id;
        this.transfer_status_id = transfer_status_id;

        this.transfer_type_desc = transfer_type_desc;


        this.username_from = username_from;
        this.user_id_from = user_id_from;
        this.account_from = account_from;

        this.username_to = username_to;
        this.user_id_to = user_id_to;
        this.account_to = account_to;

        this.amount = amount;

    }

    public int getTransfer_id() {
        return transfer_id;
    }

    public int getTransfer_type_id() {
        return transfer_type_id;
    }

    public int getTransfer_status_id() {
        return transfer_status_id;
    }

    public String getTransfer_type_desc() {
        return transfer_type_desc;
    }

    public String getTransfer_status_desc() {
        return transfer_status_desc;
    }

    public int getAccount_from() {
        return account_from;
    }

    public int getAccount_to() {
        return account_to;
    }

    public String getUsername_from() {
        return username_from;
    }

    public String getUsername_to() {
        return username_to;
    }

    public int getUser_id_from() {
        return user_id_from;
    }

    public int getUser_id_to() {
        return user_id_to;
    }

    public double getAmount() {
        return amount;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public void setTransfer_status_id(int transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public void setTransfer_type_desc(String transfer_type_desc) {
        this.transfer_type_desc = transfer_type_desc;
    }

    public void setTransfer_status_desc(String transfer_status_desc) {
        this.transfer_status_desc = transfer_status_desc;
    }

    public void setAccount_from(int account_from) {
        this.account_from = account_from;
    }

    public void setAccount_to(int account_to) {
        this.account_to = account_to;
    }

    public void setUsername_from(String username_from) {
        this.username_from = username_from;
    }

    public void setUsername_to(String username_to) {
        this.username_to = username_to;
    }

    public void setUser_id_from(int user_id_from) {
        this.user_id_from = user_id_from;
    }

    public void setUser_id_to(int user_id_to) {
        this.user_id_to = user_id_to;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "\n--------------------------------------------" +
                "\n Transfer Details" +
                "\n--------------------------------------------" +
                "\n Id: " + transfer_id +
                "\n Type: " + transfer_status_desc +
                "\n Status: " + transfer_status_desc +
                "\n From User: " + username_from +
                "\n From User Id: " + user_id_from +
                "\n From account Id: " + account_from +
                "\n To User: " + username_to +
                "\n To User Id: " + user_id_to +
                "\n To account Id: " + account_to +
                "\n amount: " + amount;
    }
}

