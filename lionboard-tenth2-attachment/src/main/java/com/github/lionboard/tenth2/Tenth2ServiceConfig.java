package com.github.lionboard.tenth2;

/**
 * Created by Lion.k on 15. 10. 16..
 */
public class Tenth2ServiceConfig {
    private static final String TENTH2_SERVICE = "osa";
    private static final String TENTH2_TEST_UPLOAD_HOST = "sa.beta.tset.daumcdn.net";
    private static final String TENTH2_TEST_SERVICE_HOST = "http://t1.beta.daumcdn.net";
    private static final String TENTH2_TEST_READ_KEY = "r_d7be85c460ebdee130ba3e9a6f4004";
    private static final String TENTH2_TEST_WRITE_KEY = "w_89a5eca4f67b2a8b5dbc060564eba2";
    private static final String TENTH2_SERVICE_UPLOAD_HOST = "sa.tset.daumcdn.net";
    private static final String TENTH2_SERVICE_SERVICE_HOST = "http://t1.daumcdn.net";
    private static final String TENTH2_SERVICE_READ_KEY = "r_4362a057580faa8409261c3a268c83";
    private static final String TENTH2_SERVICE_WRITE_KEY = "w_6c6817b57eab8c6efa1e45b8935631";
    private static boolean isService = true;

    public static void init(String mode) {
        Tenth2ServiceConfig.isService = "service".equals(mode);
    }

    public static String getServiceName() {
        return TENTH2_SERVICE;
    }

    public static String getUploadHost() {
        return (isService) ? TENTH2_SERVICE_UPLOAD_HOST : TENTH2_TEST_UPLOAD_HOST;
    }

    public static String getServiceHost() {
        return (isService) ? TENTH2_SERVICE_SERVICE_HOST : TENTH2_TEST_SERVICE_HOST;
    }

    public static String getReadKey() {
        return (isService) ? TENTH2_SERVICE_READ_KEY : TENTH2_TEST_READ_KEY;
    }

    public static String getWriteKey() {
        return (isService) ? TENTH2_SERVICE_WRITE_KEY : TENTH2_TEST_WRITE_KEY;
    }
}