package com.androidcodeshop.obviousassigment.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.androidcodeshop.obviousassigment.datamodels.DayResponseDataModel;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;
@Dao
public interface ResposeDao {

    @Insert(onConflict = REPLACE)
    public void insert(DayResponseDataModel mainResponse);

    @Query("SELECT * FROM DayResponseDataModel Where date = :date")
    DayResponseDataModel getImageByDate(String date);

    @Query("SELECT * FROM DayResponseDataModel")
    List<DayResponseDataModel> getAllResponse();

    @Query("SELECT url FROM DayResponseDataModel")
    List<String> getAllImageUrls();
}
