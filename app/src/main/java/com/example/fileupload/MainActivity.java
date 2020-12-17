package com.example.fileupload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.database.CursorWindowCompat;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fileupload.Utility.FileUtils;
import com.example.fileupload.model.FileInfo;
import com.example.fileupload.remote.APIUtils;
import com.example.fileupload.remote.FileService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    FileService fileService;
    Button btnChooseImage;
    Button btnUploadImage;
    String imagePath;
    EditText fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChooseImage=findViewById(R.id.btnChooseFile);
        btnUploadImage=findViewById(R.id.btnUpload);
        fileName=findViewById(R.id.etFileName);
        fileService= APIUtils.getFileService();

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,0);
            }
        });
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file=new File(imagePath);
                fileName.setText(file.getName());
                //file.getName();

                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // check if the body matches with the spring
                MultipartBody.Part body= MultipartBody.Part.createFormData("file", file.getName(),requestBody);
                Call<FileInfo> call = fileService.upload(body);
                call.enqueue(new Callback<FileInfo>() {
                    @Override
                    public void onResponse(Call<FileInfo> call, Response<FileInfo> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(MainActivity.this, "image uploaded successfully", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<FileInfo> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "error why this????" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println("I have been looking this since yesterday>>>>>>>>!!!!!!!!!!!" + requestCode + "    "+ RESULT_OK);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0){
            if (data==null){
                Toast.makeText(this, "unable to choose image", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri imageUri= data.getData();
            imagePath= FileUtils.getPath(this,imageUri);
            System.out.println("I have been lokking this since yesterday>>>>>>>>!!!!!!!!!!!" + imagePath);
            //imagePath=getRealPathFromUri(imageUri);
        }
    }

    private String getRealPathFromUri(Uri uri){
        String [] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(getApplicationContext(),uri,projection,null,null,null);
        Cursor cursor=loader.loadInBackground();
        int column_idx=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_idx);
        cursor.close();
        return result;

    }
}