package com.ayano.atm_server.DAO;

import com.ayano.atm_server.entity.*;
import com.ayano.atm_server.utils.QueryUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDatabase {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CardDatabase cardDatabase;
    @Autowired
    private TransactionDatabase transactionDatabase;

    private static final String TABLE = "users";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FULL_NAME = "fullName";
    private static final String EMAIL = "email";


    public boolean insertUser(User user) {
        String query = QueryUtils.insetQuery(TABLE, USERNAME + ", " + PASSWORD + ", " + FULL_NAME + ", " + EMAIL
                , "?, ?, ?, ?");
        return jdbcTemplate.update(query, user.getUsername(), user.getPassword(), user.getFullName(), user.getEmail()) > 0;
    }

    public boolean updateUser(User user) {
        String query = QueryUtils.updateQuery(TABLE, (PASSWORD + " = ?, " + FULL_NAME + " = ?, " + EMAIL + " = ?"), (USERNAME + " = ?"));
        return jdbcTemplate.update(query, user.getPassword(), user.getFullName(), user.getEmail(), user.getUsername()) > 0;
    }

    public boolean deleteUser(String username) {
        String query = QueryUtils.deleteQuery(TABLE, (USERNAME + " = ?"));
        return jdbcTemplate.update(query, username) > 0;
    }


    public User getUser(String username) {
        String query = QueryUtils.selectQuery((USERNAME + ", " + PASSWORD + ", " + FULL_NAME + ", " + EMAIL), TABLE, (USERNAME + " = ?"));
        try {
            return jdbcTemplate.queryForObject(query, new Object[]{username}, userRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public User getUserByEmail(String email) {
        String query = QueryUtils.selectQuery((USERNAME + ", " + PASSWORD + ", " + FULL_NAME + ", " + EMAIL), TABLE, (EMAIL + " = ?"));
        try {
            return jdbcTemplate.queryForObject(query, new Object[]{email}, userRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<User> getAllUsers() {
        try{
            String query = QueryUtils.selectQuery("*", TABLE);
            return jdbcTemplate.query(query, userRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if no user is found
        }
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            String username = rs.getString(USERNAME);
            String password = rs.getString(PASSWORD);
            String fullName = rs.getString(FULL_NAME);
            String email = rs.getString(EMAIL);
            Card card = cardDatabase.getCardByHolder(username);
            List<Transaction> transactions = transactionDatabase.getTransactionsByDoer(username);
            return new User(username, password, fullName, email, card, transactions);
        };
    }

}
