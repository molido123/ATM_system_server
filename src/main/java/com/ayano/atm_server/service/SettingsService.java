package com.ayano.atm_server.service;

import com.ayano.atm_server.entity.User;

public interface SettingsService {

    void changeUserPassword(String newPassword, String username);

    void changeCardPassword(String username, String newPassword);
    boolean updateCardPassword(String username, String password);
    boolean validateCardPassword(String username, String password);
    void editUserInfo(String username, String fullName, String email, String which);
    boolean updateUserInfo(String username, String fullName, String email);
    boolean deleteUser(String username);
}
