package com.ayano.atm_server.service.Impl;
import com.ayano.atm_server.entity.User;
import com.ayano.atm_server.service.LoginService;
import com.ayano.atm_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ayano.atm_server.utils.PasswordUtils.decodePassword;
import static com.ayano.atm_server.utils.TextUtils.validateUsernamePattern;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    UserService userService;
    @Override
    public void Login(String username, String password) {
        if (validateUsernamePattern(username)) {
            if(validateUserPassword(password, userService.getUserByUsername(username))){
                return;
            }
            throw new RuntimeException("密码错误！");
        }
        throw new RuntimeException("用户名不存在");
    }
    @Override
    public boolean validateUserPassword(String password, User user) {
        return decodePassword(user.getPassword(), user.getUsername()).equals(password);
    }

}
