package com.github.lionboard.service;

import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Lion.k on 16. 2. 7..
 */

@Service
public class LocalAttachmentService implements AttachmentService {
    public static final String serverPath = "/Users/daum/LionProject/lionboard-web/target/lionboard-web-0.1/WEB-INF/resources";
    public static final String fileUploadPath = "/uploadFile/";

    @Override
    public String uploadFile(byte[] bytes, String fileName) throws Exception {
        //todo fileName encoding.
        String filePath =serverPath+fileUploadPath+fileName;
        BufferedOutputStream bos = null;
        try {
            /* 파일 쓰기 */
            bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bos.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }finally {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
        }

        System.out.println("success upload to "+filePath);
        return fileUploadPath+fileName;
    }
}
