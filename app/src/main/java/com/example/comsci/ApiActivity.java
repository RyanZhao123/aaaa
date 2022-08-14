package com.example.comsci;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        Bitmap image = (Bitmap) getIntent().getParcelableExtra("image");

        File file = new File(getFilesDir() + "/capture");
        OutputStream out = null;
        try {
            file.createNewFile();
            out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        OkHttpClient client = new OkHttpClient();
        byte[] fileContent = new byte[0];
        try {
            fileContent = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/octet-stream"),
                fileContent
        );

        Request request = new Request.Builder()
                .url("https://api.logmeal.es/v2/recognition/type")
                .addHeader("Authorization", "bcf5ed1019583aadc5e2e3d96343f1a1811e85e6")
                .addHeader("Content-Type", "application/octet-stream")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}