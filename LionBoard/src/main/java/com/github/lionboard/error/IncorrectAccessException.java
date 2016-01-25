package com.github.lionboard.error;

/**
 * Created by daum on 16. 1. 25..
 */
public class IncorrectAccessException extends RuntimeException {

    public IncorrectAccessException(){
        super("Access Denied. Please check request URL. ");
    }

    public IncorrectAccessException(String msg){
        super(msg);
    }

}
