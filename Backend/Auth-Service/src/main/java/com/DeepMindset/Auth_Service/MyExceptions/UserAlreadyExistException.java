package com.DeepMindset.Auth_Service.MyExceptions;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message);
    }

    public UserAlreadyExistException() {
    }
}
