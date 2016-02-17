package com.github.lionboard.service;

import java.io.InputStream;

/**
 * Created by Lion.k on 16. 2. 7..
 */
public interface AttachmentService {
    String uploadFile(InputStream is, String fileName) throws Exception;
}
