package com.ayano.atm_server;

import com.ayano.atm_server.DAO.Database;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Connection;

@SpringBootApplication
public class AtmServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AtmServerApplication.class, args);
    }
}
