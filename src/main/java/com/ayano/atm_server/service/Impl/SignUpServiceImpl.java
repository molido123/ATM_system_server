package com.ayano.atm_server.service.Impl;

import com.ayano.atm_server.DAO.CardDatabase;
import com.ayano.atm_server.DAO.UserDatabase;
import com.ayano.atm_server.entity.*;
import com.ayano.atm_server.service.UserService;
import com.ayano.atm_server.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.ayano.atm_server.utils.PasswordUtils.encodePassword;

@Service
public class SignUpServiceImpl implements com.ayano.atm_server.service.SignUpService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDatabase userDatabase;
    @Autowired
    private CardDatabase cardDatabase;
    @Override

    public User generateUser(User user) {
        if(!userService.isNewUser(user.getUsername())) throw new RuntimeException("此用户名已被注册");
        if(!TextUtils.validateUsernamePattern(user.getUsername())) throw new RuntimeException(user.getUsername()+":此用户名格式错误！");
        String username = user.getUsername();
        String password = user.getPassword();
        String fullName = user.getFullName();
        if(!userService.isNewEmail(user.getEmail())) throw new RuntimeException("此邮箱已经被注册");
        if(!TextUtils.validateEmailPattern(user.getEmail())) throw new RuntimeException("邮箱格式错误！");
        String email = user.getEmail();
        Card card = generateCard(username);
        List<Transaction> transactions = new ArrayList<>();
        userDatabase.insertUser(new User(username, password, fullName, email, card, transactions));
        cardDatabase.insertCard(card);
        return new User(username, password, fullName, email, card, transactions);
    }



    private Card generateCard(String holder) {
        String cardNumber = generateCardNumber();
        String password = generateFourDigitPassword();
        String cardPassword = encodePassword(password, cardNumber);
        return new Card(holder, cardNumber, cardPassword, new BigDecimal(0));
    }

    private String generateFourDigitPassword() {
        int randomNumber = 1000 + new Random().nextInt(9000); // 生成1000到9999之间的随机数
        return String.valueOf(randomNumber);
    }

    private String generateCardNumber() {
        String cardNumber;
        do {
            cardNumber = "811206" + TextUtils.random(10, false, true);
        } while (!isNewCard(cardNumber));

        return cardNumber;
    }

    private boolean isNewCard(String cardNumber) {
//        try {
            List<Card> cards = cardDatabase.getAllCards();
            for (Card card : cards) {
                if (card.getCardNumber().equals(cardNumber)) {
                    return false;
                }
            }
            return true;
//        } catch (SQLException exception) {
//            return false;
//        }
    }
}