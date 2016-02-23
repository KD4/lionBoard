package com.github.lionboard.error;

import java.io.IOException;

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

    public InvalidPostException(String s, IOException e) {
        super(s,e);
    }
}
