package com.ayano.atm_server.service.Impl;
import com.ayano.atm_server.entity.User;
import com.ayano.atm_server.service.UserService;
import com.ayano.atm_server.utils.EmailUtils;
import com.ayano.atm_server.utils.PasswordUtils;
import com.ayano.atm_server.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class ForgotPasswordServiceImpl implements com.ayano.atm_server.service.ForgotPasswordService {
    @Autowired
    UserService userService;
    @Override
    public void resetPassword(User user, String newPassword) {
        newPassword=PasswordUtils.encodePassword(newPassword, user.getUsername());
        userService.updateUserPassword(user, newPassword);
    }


    @Override
    public void verifyCode(User user){
        //连接redis
        Jedis jedis=new Jedis("127.0.0.1",6379);
        String code = generateCode();
        String subject = "重设账户密码";
        String text = "您的验证码: " + code +"\n请注意将在五分钟内过期";
        String receiver = user.getEmail();
        EmailUtils.sendEmail(subject, text, receiver);
        jedis.setex(user.getEmail(), 300,code); //五分钟后过期
        jedis.close();
    }

    @Override
    public boolean validateCode(User user,String code) {
        Jedis jedis=new Jedis("127.0.0.1",6379);
        String storedCode = jedis.get(user.getEmail());
        jedis.close();
        return storedCode != null && storedCode.equals(code);
    }

    private String generateCode() {
        return TextUtils.random(6, false, true);
    }
}
