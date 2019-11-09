package com.thatquick.multipartimageupload;

import com.google.gson.JsonElement;


import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @Multipart
    @POST(HttpParams.API_UPLOAD_IMAGE)
    Call<ImageResponsModel> uploadPhoto(@Part MultipartBody.Part photo);

}
