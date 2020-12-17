package com.example.fileupload.remote;

import com.example.fileupload.model.FileInfo;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileService {

    @Multipart
    @POST("imageupload")
    Call<FileInfo> upload(@Part MultipartBody.Part file);
}
