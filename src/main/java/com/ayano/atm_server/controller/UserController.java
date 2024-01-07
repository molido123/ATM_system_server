package com.ayano.atm_server.controller;

import com.ayano.atm_server.entity.Transaction;
import com.ayano.atm_server.entity.User;
import com.ayano.atm_server.param.*;
import com.ayano.atm_server.service.*;
import com.ayano.atm_server.utils.PasswordUtils;
import com.ayano.atm_server.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService; // User 用于与数据库交互
    @Autowired
    private SignUpService signUpService;
    @Autowired
    private WithdrawalService withdrawalService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private DepositService depositService;
    @Autowired
    private TransferService transferService;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/login")
    @ResponseBody
    public Response<String> login(@RequestBody LoginBody loginRequest) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            loginService.Login(username, password); // 如果验证错误会抛出异常
            return new Response<String>("成功", 200, TextUtils.generateToken(username,"user"));
        } catch (Exception e) {
            return new Response<String>(e.getMessage(), 400, null);
        }
    }


    @PostMapping("/validateLogin")
    @ResponseBody
    public Response<String> validateLogin(@RequestBody TokenRequest tokenRequest) {
        String token = tokenRequest.getToken();
        if (TextUtils.validateToken(token)) {
            return new Response<String>("成功", 200, "您当前登陆有效");
        }
        return new Response<String>("失败", 200, "您当前登陆已经失效");
    }


    //根据token获取整个用户信息,只能查自己的
    @PostMapping("/getUserByOwnName")
    @ResponseBody
    public Response<User> getUserByUsername(@RequestBody TokenRequest tokenRequest) {
        String token = tokenRequest.getToken();
        String username = TextUtils.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return new Response<User>("成功", 200, user);
        }
        return new Response<User>("查无此人", 404, null);
    }

    //注册接口
    @PostMapping("/signup")
    @ResponseBody
    public Response<User> signup(@RequestBody SignUpBody signupRequest) {
        User newUser = null;
        try {
            newUser = signUpService.generateUser(new User(
                    signupRequest.getUsername(),
                    PasswordUtils.encodePassword(signupRequest.getPassword(), signupRequest.getUsername()),
                    signupRequest.getFullName(),
                    signupRequest.getEmail()));
        } catch (Exception e) {
            return new Response<User>(e.getMessage(), 400, null);
        }
        return new Response<User>("成功", 200, newUser);
    }


    @PostMapping("/Withdrawal")
    @ResponseBody
    public Response<Transaction> Withdrawal(@RequestBody WithDrawlBody withdrawalRequest) {
        if (!TextUtils.validateToken(withdrawalRequest.getToken())) {
            return new Response<Transaction>("token无效！", 200, null);
        }
        String username = TextUtils.getUsernameFromToken(withdrawalRequest.getToken());
        if (!transactionService.isHolder(withdrawalRequest.getCardNumber(), username)) {
            return new Response<Transaction>("这不是您的卡", 200, null);
        }
        try {
            return new Response<Transaction>("成功", 200, withdrawalService.executeWithdrawal(
                    withdrawalRequest.getCardNumber(),
                    withdrawalRequest.getNote(),
                    withdrawalRequest.getAmount()));
        } catch (Exception e) {
            return new Response<Transaction>(e.getMessage(), 400, null);
        }
    }


    @PostMapping("/deposit")
    @ResponseBody
    public Response<Transaction> deposit(@RequestBody DepositBody depositRequest) {
        if (!TextUtils.validateToken(depositRequest.getToken())) {
            return new Response<Transaction>("token无效！", 200, null);
        }
        String username = TextUtils.getUsernameFromToken(depositRequest.getToken());
        if (!transactionService.isHolder(depositRequest.getCardNumber(), username)) {
            return new Response<Transaction>("这不是您的卡", 200, null);
        }
        try {
            return new Response<Transaction>("成功", 200, depositService.executeDeposit(
                    depositRequest.getCardNumber(),
                    depositRequest.getNote(),
                    depositRequest.getAmount()));
        } catch (Exception e) {
            return new Response<Transaction>(e.getMessage(), 400, null);
        }
    }


    @PostMapping("/transfer")
    @ResponseBody
    public Response<Transaction> transfer(@RequestBody TransferBody transferRequest) {
        if (!TextUtils.validateToken(transferRequest.getToken())) {
            return new Response<Transaction>("token无效！", 200, null);
        }
        String username = TextUtils.getUsernameFromToken(transferRequest.getToken());
        if (!transactionService.isHolder(transferRequest.getSender(), username)) {
            return new Response<Transaction>("这不是您的卡", 200, null);
        }
        try {
            return new Response<Transaction>("成功", 200, transferService.executeTransfer(
                    transferRequest.getSender(),
                    transferRequest.getReceiver(),
                    transferRequest.getNote(),
                    transferRequest.getAmount()));
        } catch (Exception e) {
            return new Response<Transaction>(e.getLocalizedMessage(), 400, null);
        }
    }

    @PostMapping("/deleteMyself")
    @ResponseBody
    public Response<String> deleteMyself(@RequestBody TokenRequest tokenRequest) {
        if (!TextUtils.validateToken(tokenRequest.getToken())) {
            return new Response<String>("token无效！", 200, null);
        }
        String username = TextUtils.getUsernameFromToken(tokenRequest.getToken());
        try {
            settingsService.deleteUser(username);
            return new Response<String>("成功", 200, username + "已经被删除");
        } catch (Exception e) {
            return new Response<String>(e.getMessage(), 200, "");
        }
    }

    //忘记密码服务
    //////////////////////
    // 发送验证码
    @PostMapping("/sendCode")
    public Response<String> sendVerificationCode(@RequestBody EmailRequest emailRequest) {
        User user = userService.getUserByUsername(emailRequest.getUsername());
        if (user == null) {
            return new Response<>("用户未找到", HttpStatus.NOT_FOUND.value(), null);
        }
        forgotPasswordService.verifyCode(user);
        return new Response<>("验证码已发送", HttpStatus.OK.value(), "");
    }

    // 验证验证码并重置密码
    @PostMapping("/validateAndResetPassword")
    public Response<String> validateAndResetPassword(@RequestBody ResetPasswordRequest request) {
        User user = userService.getUserByUsername(request.getUsername());
        if (user == null) {
            return new Response<>("成功", HttpStatus.NOT_FOUND.value(), "用户未找到");
        }
        boolean isValid = forgotPasswordService.validateCode(user, request.getCode());
        if (!isValid) {
            return new Response<>("error", HttpStatus.BAD_REQUEST.value(), "验证码无效");
        }
        forgotPasswordService.resetPassword(user, request.getNewPassword());
        return new Response<>("成功", HttpStatus.OK.value(), "密码重置成功");
    }

    ///////////////////////
    /////////////////
    //下面是设置页面的接口
    ///
    // 更改用户密码
    @PostMapping("/changeUserPassword")
    public Response<String> changeUserPassword(@RequestBody ChangePasswordRequest request) {
        if (!TextUtils.validateToken(request.getToken())) {
            return new Response<String>("token无效！", 200, null);
        }
        String username = TextUtils.getUsernameFromToken(request.getToken());
        if (username == null || !username.equals(request.getUsername())) {
            return new Response<String>("token无效！", 200, null);
        }
        settingsService.changeUserPassword(request.getNewPassword(), username);
        return new Response<>("密码更改成功", HttpStatus.OK.value(), "");
    }

    //更改卡片密码
    @PostMapping("/changeCardPassword")
    public Response<String> changeCardPassword(@RequestBody ChangeCardPasswordRequest request) {
        if (!TextUtils.validateToken(request.getToken())) {
            return new Response<>("token无效！", HttpStatus.UNAUTHORIZED.value(), null);
        }
        String username = TextUtils.getUsernameFromToken(request.getToken());
        if (username == null || !username.equals(request.getUsername())) {
            return new Response<>("无权更改此卡片的密码", HttpStatus.UNAUTHORIZED.value(), null);
        }
        if(request.getNewPassword().length()!=4) {
            return new Response<>("密码长度必须为4位", HttpStatus.UNAUTHORIZED.value(), null);
        }
        settingsService.changeCardPassword(request.getUsername(), request.getNewPassword());
        return new Response<>("卡片密码更改成功", HttpStatus.OK.value(), "");
    }

    //更改用户信息
    @PostMapping("/editUserInfo")
    public Response<String> editUserInfo(@RequestBody EditUserInfoRequest request) {
        if (!TextUtils.validateToken(request.getToken())) {
            return new Response<>("token无效！", HttpStatus.UNAUTHORIZED.value(), null);
        }
        String username = TextUtils.getUsernameFromToken(request.getToken());
        if (username == null || !username.equals(request.getUsername())) {
            return new Response<>("失败", HttpStatus.UNAUTHORIZED.value(), "无权编辑此用户信息\"");
        }
        settingsService.editUserInfo(request.getUsername(), request.getFullName(), request.getEmail(), request.getWhich());
        return new Response<>("成功", HttpStatus.OK.value(), "用户信息更新成功");
    }
    @PostMapping("/getBalance")
    public Response<String> getBalance(@RequestBody TokenRequest tokenRequest){
        String token = tokenRequest.getToken();
        if (!TextUtils.validateToken(token)) {
            return new Response<String>("当前token无效", 200, null);
        }
        String username=TextUtils.getUsernameFromToken(token);
        System.out.println(username);
        User user=userService.getUserByUsername(username);
        if(user==null)
            return new Response<String>("查无此人", 200, null);

        return new Response<>("成功", HttpStatus.OK.value(), user.getCard().getBalance().toString());

    }

    @PostMapping("/getTransactions")
    public Response<List<Transaction>> getTransactions(@RequestBody TokenRequest tokenRequest){
        String token = tokenRequest.getToken();
        if (!TextUtils.validateToken(token)) {
            return new Response<List<Transaction>>("当前token无效", 200, null);
        }
        String username=TextUtils.getUsernameFromToken(token);
        User user=userService.getUserByUsername(username);
        if(user==null)
            return new Response<>("查无此人", 200, null);

        return new Response<>("成功", HttpStatus.OK.value(), user.getTransactions());
    }

}





