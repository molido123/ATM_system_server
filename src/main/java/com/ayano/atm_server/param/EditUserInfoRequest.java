package com.ayano.atm_server.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditUserInfoRequest {
    private String username;
    private String fullName;
    private String email;
    private String which;
    private String token;
}
