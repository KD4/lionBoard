package com.github.lionboard.service;

import com.github.lionboard.tenth2.ImageFileUploadForTenth2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Lion.k on 16. 2. 7..
 */

@Service
public class TenthAttachmentServie implements AttachmentService{
    @Override
    public String uploadFile(byte[] bytes, String fileName) throws Exception {
        return null;
    }
//
//    @Autowired
//    ImageFileUploadForTenth2 imageFileUploadForTenth2;
//
//
//    @Override
//    public String uploadFile(byte[] bytes, String fileName) throws Exception {
//        //tenth2에 등록 성공하면, 접근할 수 있는 URL을 반환함.
//        String uploadUrl = insertFileOnTenthServer(bytes, fileName);
//
//        return uploadUrl;
//    }
//
//    //업로드 팜을 이용해서 tenth서버에 업로드된 파일을 다시 업로드하는 로직.
//    private String insertFileOnTenthServer(byte[] imageFileBytes, String fileName) throws Exception {
//        try {
//            imageFileUploadForTenth2.init();
//            return imageFileUploadForTenth2.create(imageFileBytes, fileName);
//        } catch (Exception e) {
//            throw new Exception(e);
//        }
//    }

}
