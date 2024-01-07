package com.ayano.atm_server.service;

import com.ayano.atm_server.entity.User;

public interface ForgotPasswordService {
    void resetPassword(User user, String newPassword);

    void verifyCode(User user);

    boolean validateCode(User user, String code);
}
