package com.sample.token.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDetailsWraper {
    private int id;
    private String firstName;
    private String userName;
    private String role;
}
