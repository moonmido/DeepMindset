package com.DeepMindset.Auth_Service.MyExceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException() {
    }
}
