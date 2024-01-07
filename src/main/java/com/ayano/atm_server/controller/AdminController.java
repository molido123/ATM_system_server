package com.ayano.atm_server.controller;

import com.ayano.atm_server.entity.User;
import com.ayano.atm_server.param.Response;
import com.ayano.atm_server.service.LoginService;
import com.ayano.atm_server.service.SignUpService;
import com.ayano.atm_server.service.UserService;
import com.ayano.atm_server.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @Autowired
    private UserService userService; // User 用于与数据库交互
    @Autowired
    private SignUpService signUpService;
    @Autowired
    private WithdrawalService withdrawalService;
    @Autowired
    private LoginService loginService;

    @GetMapping("/getUserByUsername")
    @ResponseBody
    public Response<User> getUserByUsername(@RequestParam String username) {
        User user= userService.getUserByUsername(username);
        if(user!=null){
            return new Response<User>("成功", 200, user);
        }
        return new Response<User>("查无此人",404,null);
    }
}
