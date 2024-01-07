package com.ayano.atm_server.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCardPasswordRequest {
    private String username;
    private String newPassword;
    private String token;

    // Getter 和 Setter
}
