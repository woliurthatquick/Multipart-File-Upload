package com.thatquick.multipartimageupload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button imageSelect;
    Button uploadImage;
    ImageView imageView;
    Bitmap bitmap;
    MultipartBody.Part body;
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageSelect = findViewById(R.id.button);
        uploadImage = findViewById(R.id.button2);
        imageView = findViewById(R.id.imageView);
        imageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiInterface apiInterface = ApiUtil.getApiInterface();
                Call<ImageResponsModel> call = apiInterface.uploadPhoto(body);
                call.enqueue(new Callback<ImageResponsModel>() {
                    @Override
                    public void onResponse(Call<ImageResponsModel> call, Response<ImageResponsModel> response) {
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ImageResponsModel> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Nothing",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);

            File file = new File(data.getData().getPath());
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),file);
            body = MultipartBody.Part.createFormData("photo",file.getName(),requestBody);
        }
    }
}
