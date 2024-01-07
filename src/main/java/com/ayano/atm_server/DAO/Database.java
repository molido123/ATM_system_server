package com.ayano.atm_server.DAO;

import com.ayano.atm_server.utils.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Database {

    private final JdbcTemplate jdbcTemplate;

    private static final String TEXT = " TEXT, ";
    private static final String INTEGER = " INTEGER, ";
    private static final String PRIMARY_KEY = "PRIMARY KEY";

    @Autowired
    public Database(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        initUserTable();
        initCardTable();
        initTransactionTable();
    }

    private void initUserTable() {
        String query = QueryUtils.creatTableQuery("users", ("username" + TEXT + "password" + TEXT + "fullName" + TEXT
                + "email" + TEXT + PRIMARY_KEY + "(username)"));

        jdbcTemplate.execute(query);
    }

    private void initCardTable() {
        String query = QueryUtils.creatTableQuery("cards", ("holder" + TEXT + "cardNumber" + TEXT + "password" + TEXT
                + "balance" + INTEGER + PRIMARY_KEY + "(cardNumber)"));

        jdbcTemplate.execute(query);
    }

    private void initTransactionTable() {
        String query = QueryUtils.creatTableQuery("transactions", ("doer" + TEXT + "trackingCode" + TEXT + "sender" + TEXT
                + "receiver" + TEXT + "note" + TEXT + "dateTime" + TEXT + "amount" + INTEGER + "transactionType" + TEXT + PRIMARY_KEY
                + "(trackingCode)"));

        jdbcTemplate.execute(query);
    }
}
