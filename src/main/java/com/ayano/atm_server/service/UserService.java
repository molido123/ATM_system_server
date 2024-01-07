package com.ayano.atm_server.service;

import com.ayano.atm_server.entity.User;

public interface UserService {
    User getUserByUsername(String username);


    boolean updateUserPassword(User user, String password);

    boolean isNewUser(String username);

    boolean isNewEmail(String email);

    User getUserByEmail(String email);
}
