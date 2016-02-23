package com.github.lionboard.service;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Lion.k on 16. 2. 7..
 */

@Service
@PropertySource({"classpath:/config/custom.properties", "classpath:/config/release.properties"})
public class LocalAttachmentService implements AttachmentService {

    @Resource
    private Environment environment;

    @Override
    public String uploadFile(InputStream is, String fileName) throws IOException {

        //ToDo FileName Encoding
        String filePath = environment.getProperty("server.resources.path") + environment.getProperty("server.upload.path") + fileName;

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

        return filePath;

    }
}
