package com.github.lionboard.service;

import com.github.lionboard.tenth2.ImageFileUploadForTenth2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Lion.k on 16. 2. 7..
 */

@Service
public class TenthAttachmentServie implements AttachmentService{

    @Autowired
    ImageFileUploadForTenth2 imageFileUploadForTenth2;


    @Override
    public String uploadFile(InputStream is, String fileName) throws IOException {
        //tenth2에 등록 성공하면, 접근할 수 있는 URL을 반환함.
        //todo fileName encoding.
        String uploadUrl = insertFileOnTenthServer(is, fileName);

        return uploadUrl;
    }

    //업로드 팜을 이용해서 tenth서버에 업로드된 파일을 다시 업로드하는 로직.
    private String insertFileOnTenthServer(InputStream is, String fileName) throws IOException {
        try {
            imageFileUploadForTenth2.init();
            return imageFileUploadForTenth2.create(is, fileName);
        } catch (IOException e) {
            throw e;
        }
    }

}
