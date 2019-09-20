package com.androidcodeshop.obviousassigment.rests;

import com.androidcodeshop.obviousassigment.datamodels.DayResponseDataModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Endpoints {

    @GET("planetary/apod?")
    Call<DayResponseDataModel> getImageByDate(@Query("date") String dateStr, @Query("api_key") String api_key);

}
