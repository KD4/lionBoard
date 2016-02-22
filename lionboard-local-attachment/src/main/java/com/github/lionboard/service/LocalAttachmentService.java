package com.github.lionboard.service;

import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Lion.k on 16. 2. 7..
 */

@Service
public class LocalAttachmentService implements AttachmentService {
    public static final String serverPath = "/Users/daum/LionProject/lionboard-web/target/lionboard-web-0.1/WEB-INF/resources";
    public static final String fileUploadPath = "/uploadFile/";

    @Override
    public String uploadFile(InputStream is, String fileName) throws Exception {

        //ToDo FileName Encoding
        String filePath = serverPath + fileUploadPath + fileName;

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {

            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = is.read(buf)) > 0) {

                bos.write(buf);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        return fileUploadPath + fileName;

    }
}
