package com.github.lionboard.error;

/**
 * Created by daum on 16. 2. 2..
 */
public class UploadFileToTenthException extends RuntimeException{
    public UploadFileToTenthException(){
        super("Tenth서버에 연결할 수 없습니다.");
    }

    public UploadFileToTenthException(String msg){
        super(msg);
    }
}
