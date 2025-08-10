package com.DeepMindset.Auth_Service.MyExceptions;

public class SmallPasswordException extends RuntimeException {
    public SmallPasswordException(String message) {
        super(message);
    }

  public SmallPasswordException() {
  }
}
