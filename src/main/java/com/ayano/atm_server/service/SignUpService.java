package com.ayano.atm_server.service;

import com.ayano.atm_server.entity.User;

import java.sql.SQLException;

public interface SignUpService {
    User generateUser(User user) throws SQLException;
}
