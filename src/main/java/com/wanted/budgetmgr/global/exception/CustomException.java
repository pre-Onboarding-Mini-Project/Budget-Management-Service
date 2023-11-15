package com.wanted.budgetmgr.global.exception;

public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }

    public static CustomException duplicateEmail(String email) {
        return new CustomException("중복 이메일 입니다. 이메일 : " + email);
    }
}
