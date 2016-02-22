package com.github.lionboard.error;

/**
 * Created by Lion.k on 16. 1. 21..
 */
public class InvalidPostException extends RuntimeException {

    public InvalidPostException() {
        super("invalid Post Id or Post Status. You can't see this post.");
    }

    public InvalidPostException(String msg) {
        super(msg);
    }

}
