package com.wanted.budgetmgr.dto;

import lombok.Getter;
import java.time.LocalDate;

@Getter
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private String createAt;
    private String updatedAt;
}
