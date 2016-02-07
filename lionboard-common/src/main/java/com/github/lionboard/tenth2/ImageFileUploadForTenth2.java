package com.github.lionboard.tenth2;

import net.daum.tenth2.Tenth2Authentication;
import net.daum.tenth2.Tenth2Connector;
import net.daum.tenth2.Tenth2File;
import net.daum.tenth2.exceptions.AuthenticationException;
import net.daum.tenth2.util.Tenth2Util;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageFileUploadForTenth2 {

    private static final byte TENTH2_MUTI_PART = 4;

    public static void init() {
        try {
            Tenth2Authentication.addAccessKey(Tenth2ServiceConfig.getServiceName(), Tenth2ServiceConfig.getReadKey(), Tenth2ServiceConfig.getWriteKey());
            Tenth2Connector.setHost(new Tenth2Connector.HostInfo(Tenth2ServiceConfig.getUploadHost()));
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }

    public static void remove(String imageFilePath) throws IOException{
        Tenth2File file = new Tenth2File(getTenth2Path(imageFilePath));
        System.out.println(file.getPath());
        file.delete();
        System.out.println(imageFilePath + " is removed on server.");
    }

    public static String create(byte[] imageFileBytes,String imageFileName) throws Exception {

        String uploadPath = makeTenth2FileUploadPath(imageFileName);

        put(uploadPath, imageFileBytes);

        return Tenth2ServiceConfig.getServiceHost() + uploadPath;
    }

    private static void put(String tenthPath, byte[] imageFileBytes) throws IOException {

        Tenth2Util.put(tenthPath, imageFileBytes);
    }

    private static void putMultipleStream(String uploadPath, String imageFilePath) throws Exception {
        File f = new File(imageFilePath);
        InputStream[] is = new InputStream[TENTH2_MUTI_PART];
        for(int oss = 0; oss < TENTH2_MUTI_PART; ++oss) {
            is[oss] = new FileInputStream(f);
        }
        Tenth2Util.putMultipleStream(uploadPath, f.length(), is);
    }

    private static String makeTenth2FileUploadPath(String fileName){
        return "/" + Tenth2ServiceConfig.getServiceName() + "/tech/" + fileName;
    }

    private static String getTenth2Path(String imgurl){
        return imgurl.replaceAll(Tenth2ServiceConfig.getServiceHost(), "");
    }

    private static boolean isTenth2(String imgurl) {
        System.out.println(imgurl);
        System.out.println(String.format("%s/%s/([-a-zA-Z0-9_/].+)", Tenth2ServiceConfig.getServiceHost(), Tenth2ServiceConfig.getServiceName()));
        return imgurl.matches(String.format("%s/%s/([-a-zA-Z0-9_/].+)", Tenth2ServiceConfig.getServiceHost(), Tenth2ServiceConfig.getServiceName()));
    }
}