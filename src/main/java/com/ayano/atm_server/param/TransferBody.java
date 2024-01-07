package com.ayano.atm_server.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferBody {
    String sender;
    String receiver;
    String note;
    BigDecimal amount;
    String token;
}
