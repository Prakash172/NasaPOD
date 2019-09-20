package com.androidcodeshop.obviousassigment.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.androidcodeshop.obviousassigment.datamodels.DayResponseDataModel;

@Database(entities = {DayResponseDataModel.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    public abstract ResposeDao resposeDao();

    private static volatile MyDatabase INSTANCE;

    public static MyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyDatabase.class, "DayResponseDataModel")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
