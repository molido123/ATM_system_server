package com.ayano.atm_server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class TextUtils {

    public static String capitalize(String text) {
        int count = 0;
        StringBuilder capitalizedText = new StringBuilder();
        String[] splitText = text.split("\\s");
        for (String name : splitText) {
            count++;
            if (count == splitText.length) {
                capitalizedText.append(org.apache.commons.lang.StringUtils.capitalize(name));
                continue;
            }
            capitalizedText.append(org.apache.commons.lang.StringUtils.capitalize(name)).append(" ");
        }
        return capitalizedText.toString();
    }

    public static String toCurrency(String amount) {
        boolean isNegative = false;

        if (amount.contains("-")) {
            amount = amount.replace("-", "");
            isNegative = true;
        }

        String[] textToArray = amount.split("");
        StringBuilder customizedText = new StringBuilder(amount);

        int count = textToArray.length;
        int three = 0;

        while (count-- != 0) {
            three++;
            if (three == 3 && count != 0) {
                customizedText.insert(count, ",");
                three = 0;
            }
        }

        if (isNegative) {
            customizedText.insert(0, "-");
        }

        return customizedText.toString();
    }

    public static String hide(int start, int end, String mark, String text) {
        return new StringBuffer(text).replace(start, end, mark).toString();
    }

    public static String random(int count, boolean letters, boolean numbers) {
        return RandomStringUtils.random(count, letters, numbers);
    }
    public static boolean validateUsernamePattern(String username) {
        return username.matches("[A-Za-z0-9]*") && username.length() >= 5;
    }
    public static boolean validateEmailPattern(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

    public static String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime());
    }
    public static String normalize(String text) {
        return StringUtils.normalizeSpace(text);
    }

    public static boolean isBlack(String text) {
        return StringUtils.isBlank(text);
    }
    public static String generateToken(String username, String role) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role) // Add role information as a claim
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + 3600000)) // Token valid for 1 hour
                .signWith(SignatureAlgorithm.HS512, "114514")
                .compact();
    }
    public static boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("114514")
                    .parseClaimsJws(token)
                    .getBody();

            // Optional: Check additional claims, e.g., user role:
            // String role = claims.get("role", String.class);

            return true;
        } catch (Exception e) {
//            throw new RuntimeException("token系伪造的或已经过期");
            return false;
        }
    }
    public static String getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("114514")
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("role", String.class); // Extract the role claim
        }catch (Exception e) {
            return null;
        }
    }

    public static String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("114514")
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // Get username (subject)
        } catch (Exception e) {
            return null;
        }
    }
}
