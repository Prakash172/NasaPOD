package com.androidcodeshop.obviousassigment.repository;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.androidcodeshop.obviousassigment.datamodels.DayResponseDataModel;
import com.androidcodeshop.obviousassigment.rests.ApiClient;
import com.androidcodeshop.obviousassigment.rests.Endpoints;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityRepository {

    private static final String TAG = "MainActivityRepository";

    public MutableLiveData<DayResponseDataModel> mDayResponse = new MutableLiveData<>();
    private static MainActivityRepository mainActivityRepository;

    public synchronized static MainActivityRepository getInstance() {
        if (mainActivityRepository == null)
            mainActivityRepository = new MainActivityRepository();
        return mainActivityRepository;
    }

    public MutableLiveData<DayResponseDataModel> getImageDataByDate(String date, String api_key) {
        Endpoints endpoints = ApiClient.getClient().create(Endpoints.class);
        Call<DayResponseDataModel> responseDataModelCall = endpoints.getImageByDate(date, api_key);
        responseDataModelCall.enqueue(new Callback<DayResponseDataModel>() {
            @Override
            public void onResponse(Call<DayResponseDataModel> call, Response<DayResponseDataModel> response) {
                if (response.body() != null) {
                    mDayResponse.setValue(response.body());
                    Log.d(TAG, "onResponse: " + response.body().getTitle());
                } else {
                    mDayResponse.setValue(null);
                    Log.d(TAG, "onResponse: response is null");
                }
            }

            @Override
            public void onFailure(Call<DayResponseDataModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mDayResponse.setValue(null);
            }

        });
        return mDayResponse;
    }
}
