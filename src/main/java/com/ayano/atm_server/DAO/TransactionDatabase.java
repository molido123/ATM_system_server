package com.ayano.atm_server.DAO;

import com.ayano.atm_server.entity.Transaction;
import com.ayano.atm_server.utils.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionDatabase {

    private final JdbcTemplate jdbcTemplate;

    private static final String TABLE = "transactions";
    private static final String DOER = "doer";
    private static final String TRACKING_CODE = "trackingCode";
    private static final String SENDER = "sender";
    private static final String RECEIVER = "receiver";
    private static final String NOTE = "note";
    private static final String DATE_TIME = "dateTime";
    private static final String AMOUNT = "amount";
    private static final String TRANSACTION_TYPE = "transactionType";

    @Autowired
    public TransactionDatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean insertTransaction(Transaction transaction) {
        String query = QueryUtils.insetQuery(TABLE, (DOER + ", " + TRACKING_CODE + ", " + SENDER + ", " + RECEIVER + ", "
                + NOTE + ", " + DATE_TIME + ", " + AMOUNT + ", " + TRANSACTION_TYPE), "?, ?, ?, ?, ?, ?, ?, ?");
        return jdbcTemplate.update(query, transaction.getDoer(), transaction.getTrackingCode(), transaction.getSender(),
                transaction.getReceiver(), transaction.getNote(), transaction.getDateTime(), transaction.getAmount(),
                transaction.getTransactionType()) > 0;
    }

    public void deleteTransactionsByDoer(String doer) {
        String query = QueryUtils.deleteQuery(TABLE, (DOER + " = ?"));
        jdbcTemplate.update(query, doer);
    }

    public List<Transaction> getTransactionsByDoer(String doer) {
        String query = QueryUtils.selectQuery("*", TABLE, (DOER + " = ?"));
        return jdbcTemplate.query(query, new Object[]{doer}, transactionRowMapper());
    }

    public List<Transaction> getAllTransactions() {
        String query = QueryUtils.selectQuery("*", TABLE);
        return jdbcTemplate.query(query, transactionRowMapper());
    }

    private RowMapper<Transaction> transactionRowMapper() {
        return (rs, rowNum) -> new Transaction(
                rs.getString(DOER),
                rs.getString(TRACKING_CODE),
                rs.getString(SENDER),
                rs.getString(RECEIVER),
                rs.getString(NOTE),
                rs.getString(DATE_TIME),
                rs.getBigDecimal(AMOUNT),
                rs.getString(TRANSACTION_TYPE)
        );
    }
}
