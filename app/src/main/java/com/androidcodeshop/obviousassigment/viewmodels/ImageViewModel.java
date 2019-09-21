package com.androidcodeshop.obviousassigment.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.androidcodeshop.obviousassigment.datamodels.DayResponseDataModel;
import com.androidcodeshop.obviousassigment.repository.MainActivityRepository;

public class ImageViewModel extends AndroidViewModel {

    private MutableLiveData<DayResponseDataModel> mDayResponseDataModelMutableLiveData;
    private MainActivityRepository mRepository;

    public ImageViewModel(@NonNull Application application) {
        super(application);
        mRepository = MainActivityRepository.getInstance();
        mDayResponseDataModelMutableLiveData = mRepository.mDayResponse;
    }

    public MutableLiveData<DayResponseDataModel> getImageByDateObservable(String date, String api_key) {
        DayResponseDataModel dayResponseDataModel = mRepository.getImageDataByDate(date, api_key).getValue();
        if (dayResponseDataModel != null)
            mDayResponseDataModelMutableLiveData.setValue(dayResponseDataModel);
        return mDayResponseDataModelMutableLiveData;
    }

}
