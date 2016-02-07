package com.github.lionboard.service;

/**
 * Created by Lion.k on 16. 2. 7..
 */
public interface AttachmentService {

    String uploadFile(byte[] bytes, String fileName) throws Exception;
}
