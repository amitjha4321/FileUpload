package com.example.fileupload.remote;

public class APIUtils {
    public static final String API_URL="http://192.168.1.66:8081/";

    public static FileService getFileService(){
        return RetrofilClient.getClient(API_URL).create(FileService.class);
    }
}
