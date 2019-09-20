package com.androidcodeshop.obviousassigment.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.androidcodeshop.obviousassigment.R;
import com.androidcodeshop.obviousassigment.adapters.ImageRecyclerViewAdapter;
import com.androidcodeshop.obviousassigment.database.MyDatabase;
import com.androidcodeshop.obviousassigment.datamodels.DayResponseDataModel;
import com.androidcodeshop.obviousassigment.utils.AppUtils;
import com.androidcodeshop.obviousassigment.viewmodels.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String CURRENT_DATE = "current_date";
    private static final String IMAGE_URLS = "image_urls";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_recycler_view)
    RecyclerView imageRecyclerView;
    @BindView(R.id.fab_share)
    FloatingActionButton fab;
    private ArrayList<String> mImageUrls;
    private ImageRecyclerViewAdapter mAdapter;
    SharedPreferences.Editor mEditor;
    private MyDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mImageUrls = new ArrayList<>();
        mDatabase = MyDatabase.getDatabase(this);
        MainActivityViewModel mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        if (savedInstanceState != null) {
            if (savedInstanceState.getStringArrayList(IMAGE_URLS) != null)
                mImageUrls.addAll(savedInstanceState.getStringArrayList(IMAGE_URLS));
        }

        SharedPreferences sharedPreferences = getSharedPreferences(CURRENT_DATE, 0);
        mEditor = sharedPreferences.edit();
        setImageGrid();

//        if (!sharedPreferences.getString("date", "").equals(AppUtils.getTodayDate()))
        if (mDatabase.resposeDao().getImageByDate(AppUtils.getTodayDate()) == null) {
            loadTodaysImage(mainActivityViewModel, mEditor);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(IMAGE_URLS, mImageUrls);
    }

    private void loadTodaysImage(MainActivityViewModel mainActivityViewModel, SharedPreferences.Editor mEditor) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Wait...");
        progressDialog.setMessage("Picture is coming from satellite");
        progressDialog.show();
        String currentDate = AppUtils.getTodayDate();
        Log.d(TAG, "loadTodaysImage: " + currentDate);
        mainActivityViewModel
                .getImageByDateObservable(currentDate, "8dRnZMRZQKBGuPVu9OeHAUmc03c7qhGS97xKQM2a")
                .observe(this, new Observer<DayResponseDataModel>() {
                    @Override
                    public void onChanged(@Nullable DayResponseDataModel dayResponseDataModel) {
                        if(dayResponseDataModel != null) {
                            Log.d(TAG, "onChanged: " + dayResponseDataModel.getTitle());
                            mImageUrls.add(0, dayResponseDataModel.getUrl());
                            mAdapter.notifyDataSetChanged();
                            progressDialog.hide();
                            mEditor.putString("date", currentDate).apply();
                            mDatabase.resposeDao().insert(dayResponseDataModel);
                        }else {
                            progressDialog.hide();
                            Toast.makeText(MainActivity.this, "No Data For Today", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void setImageGrid() {
        mImageUrls.clear();
        List<String> savedUrls = mDatabase.resposeDao().getAllImageUrls();
        mImageUrls.addAll(savedUrls);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        imageRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new ImageRecyclerViewAdapter(this, mImageUrls);
        imageRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.fab_share)
    public void onViewClicked() {

    }
}
