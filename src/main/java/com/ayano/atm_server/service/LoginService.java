package com.ayano.atm_server.service;

import com.ayano.atm_server.entity.User;

public interface LoginService {
    void Login(String username, String password);

    boolean validateUserPassword(String password, User user);
}
