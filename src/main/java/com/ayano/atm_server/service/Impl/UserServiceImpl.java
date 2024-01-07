package com.ayano.atm_server.service.Impl;
import com.ayano.atm_server.DAO.UserDatabase;
import com.ayano.atm_server.entity.User;
import com.ayano.atm_server.service.UserService;
import com.ayano.atm_server.utils.PasswordUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
   @Autowired
    private UserDatabase userDatabase;

    @Override
    public User getUserByUsername(String username) {
//        try {
            return userDatabase.getUser(username);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
    }
    @Override
    public boolean updateUserPassword(User user, String password) {
//        try {
            user.setPassword(password);
            return userDatabase.updateUser(user);
//        } catch (SQLException exception) {
//            return false;
//        }
    }
    @Override
    public boolean isNewUser(String username) {
            User user = userDatabase.getUser(username);
            return user == null;
    }
    @Override
    public boolean isNewEmail(String email) {
            List<User> users = userDatabase.getAllUsers();
            for (User user : users) {
                if (user.getEmail()!=null&&user.getEmail().equals(email)) {
                    return false;
                }
            }
            return true;
    }

    @Override
    public User getUserByEmail(String email) {
        return userDatabase.getUserByEmail(email);
    }

}
