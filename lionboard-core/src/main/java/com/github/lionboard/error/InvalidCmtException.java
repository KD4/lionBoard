package com.github.lionboard.error;

/**
 * Created by Lion.k on 16. 1. 21..
 */
public class InvalidCmtException extends RuntimeException {

    public InvalidCmtException() {
        super("invalid Cmt Id or Cmt Status. You can't see this Cmt.");
    }

    public InvalidCmtException(String msg) {
        super(msg);
    }

}
