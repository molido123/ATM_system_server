package com.ayano.atm_server.DAO;

import com.ayano.atm_server.entity.Card;
import com.ayano.atm_server.utils.QueryUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Data
public class CardDatabase {

    private final JdbcTemplate jdbcTemplate;

    private static final String TABLE = "cards";
    private static final String HOLDER = "holder";
    private static final String CARD_NUMBER = "cardNumber";
    private static final String PASSWORD = "password";
    private static final String BALANCE = "balance";

    @Autowired
    public CardDatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertCard(Card card) {
        String query = QueryUtils.insetQuery(TABLE, (HOLDER + ", " + CARD_NUMBER + ", " + PASSWORD + ", " + BALANCE)
                , "?, ?, ?, ?");
        jdbcTemplate.update(query, card.getHolder(), card.getCardNumber(), card.getPassword(), card.getBalance());
    }

    public boolean updateCard(Card card) {
        String query = QueryUtils.updateQuery(TABLE, (PASSWORD + " = ?, " + BALANCE + " = ?"), (CARD_NUMBER + " = ?"));
        return jdbcTemplate.update(query, card.getPassword(), card.getBalance(), card.getCardNumber()) > 0;
    }

    public void deleteCardByHolder(String holder) {
        String query = QueryUtils.deleteQuery(TABLE, (HOLDER + " = ?"));
        jdbcTemplate.update(query, holder);
    }

    public Card getCardByHolder(String holder) {
        try {
            String query = QueryUtils.selectQuery((HOLDER + ", " + CARD_NUMBER + ", " + PASSWORD + ", " + BALANCE), TABLE, (HOLDER + " = ?"));
            return jdbcTemplate.queryForObject(query, new Object[]{holder}, cardRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if no card is found
        }
    }

    public Card getCardByCardNumber(String cardNumber) {
       try{
        String query = QueryUtils.selectQuery((HOLDER + ", " + CARD_NUMBER + ", " + PASSWORD + ", " + BALANCE), TABLE
                , (CARD_NUMBER + " = ?"));
        return jdbcTemplate.queryForObject(query, new Object[]{cardNumber}, cardRowMapper());
       }catch (Exception e){
           return null;
       }
    }

    public List<Card> getAllCards() {
        String query = QueryUtils.selectQuery("*", TABLE);
        return jdbcTemplate.query(query, cardRowMapper());
    }

    private RowMapper<Card> cardRowMapper() {
        return (rs, rowNum) -> new Card(
                rs.getString(HOLDER),
                rs.getString(CARD_NUMBER),
                rs.getString(PASSWORD),
                rs.getBigDecimal(BALANCE)
        );
    }
}
