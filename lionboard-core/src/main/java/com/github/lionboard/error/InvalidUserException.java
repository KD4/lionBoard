package com.github.lionboard.error;

/**
 * Created by Lion.k on 16. 1. 21..
 */
public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
        super("invalid User Id or User Status. can't find User.");
    }

    public InvalidUserException(String msg) {
        super(msg);
    }
}
