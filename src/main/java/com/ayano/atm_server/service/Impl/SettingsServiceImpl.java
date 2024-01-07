package com.ayano.atm_server.service.Impl;

import com.ayano.atm_server.DAO.CardDatabase;
import com.ayano.atm_server.DAO.UserDatabase;
import com.ayano.atm_server.entity.User;
import com.ayano.atm_server.service.UserService;
import com.ayano.atm_server.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ayano.atm_server.utils.PasswordUtils.encodePassword;

@Service
public class SettingsServiceImpl implements com.ayano.atm_server.service.SettingsService {
    @Autowired
    private UserService userService;
    @Autowired
    private CardDatabase cardDatabase;
    @Autowired
    private UserDatabase userDatabase;
    @Override
    public void changeUserPassword(String newPassword, String username) {
        String password = encodePassword(newPassword, username);
        User user=userDatabase.getUser(username);
        userService.updateUserPassword(user,password);
    }

    @Override
    public void changeCardPassword(String username, String newPassword) {
        User user=userDatabase.getUser(username);
        String password = encodePassword(newPassword, user.getCard().getCardNumber());
        updateCardPassword(username,password);
    }

    @Override
    public boolean updateCardPassword(String username, String password) {
        User user=userDatabase.getUser(username);
        user.getCard().setPassword(password);
        return cardDatabase.updateCard(user.getCard());
    }

    @Override
    public boolean validateCardPassword(String username, String password) {
        User user=userDatabase.getUser(username);
        return PasswordUtils.decodePassword(user.getCard().getPassword(), user.getCard().getCardNumber()).equals(password);
    }

    // USER INFO SECTION

    @Override
    public void editUserInfo(String username, String fullName, String email, String which) {
        User user=userDatabase.getUser(username);
        switch (which) {
            case "姓名":
                updateUserInfo(username,fullName, user.getEmail());
                break;
            case "全部":
                updateUserInfo(username,fullName, email);
                break;
        }
    }

    @Override
    public boolean updateUserInfo(String username, String fullName, String email) {
        User user=userDatabase.getUser(username);
        user.setFullName(fullName);
        user.setEmail(email);
        return userDatabase.updateUser(user);
    }


    @Override
    public boolean deleteUser(String username) {
        return userDatabase.deleteUser(username);
    }
}
