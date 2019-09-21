package com.androidcodeshop.obviousassigment.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
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
import com.androidcodeshop.obviousassigment.viewmodels.ImageViewModel;

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
    @BindView(R.id.fab_sync)
    FloatingActionButton fab;
    private ArrayList<String> mImageUrls;
    public static ArrayList<DayResponseDataModel> responseDataModelArrayList;
    private ImageRecyclerViewAdapter mAdapter;
    private MyDatabase mDatabase;
    private ImageViewModel imageViewModel;
    private MutableLiveData<DayResponseDataModel> dataModelMutableLiveData;
    Observer<DayResponseDataModel> dataModelObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mImageUrls = new ArrayList<>();
        responseDataModelArrayList = new ArrayList<>();
        mDatabase = MyDatabase.getDatabase(this);
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        if (savedInstanceState != null) {
            if (savedInstanceState.getStringArrayList(IMAGE_URLS) != null)
                mImageUrls.addAll(savedInstanceState.getStringArrayList(IMAGE_URLS));
        }

        setImageGrid();

        if (mDatabase.resposeDao().getImageByDate(AppUtils.getTodayDate()) == null) {
            loadTodaysImage();
        }

        Log.d(TAG, "nextDay: " + AppUtils.nextDay(AppUtils.getTodayDate()));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(IMAGE_URLS, mImageUrls);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dataModelMutableLiveData != null && dataModelObserver != null)
            dataModelMutableLiveData.observe(this, dataModelObserver);
    }

    private void loadTodaysImage() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Wait...");
        progressDialog.setMessage("Picture is coming from satellite");
        progressDialog.show();
        String currentDate = AppUtils.getTodayDate();
        Log.d(TAG, "loadTodaysImage: " + currentDate);
        dataModelMutableLiveData = imageViewModel.getImageByDateObservable(currentDate, getString(R.string.api_key));
        dataModelObserver = dayResponseDataModel -> {
            if (dayResponseDataModel != null) {
                Log.d(TAG, "onChanged: " + dayResponseDataModel.getTitle());
                mImageUrls.add(0, dayResponseDataModel.getUrl());
                responseDataModelArrayList.add(0, dayResponseDataModel);
                mAdapter.notifyDataSetChanged();
                progressDialog.hide();
                mDatabase.resposeDao().insert(dayResponseDataModel);
            } else {
                progressDialog.hide();
                Toast.makeText(MainActivity.this, "No Data For Today", Toast.LENGTH_SHORT).show();
            }
        };
        dataModelMutableLiveData.observe(this, dataModelObserver);

    }

    private void setImageGrid() {
        //Clear the response
        mImageUrls.clear();
        responseDataModelArrayList.clear();
        //add the responses
        List<String> savedUrls = mDatabase.resposeDao().getAllImageUrls();
        List<DayResponseDataModel> savedResponse = mDatabase.resposeDao().getAllResponse();
        mImageUrls.addAll(savedUrls);
        responseDataModelArrayList.addAll(savedResponse);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        imageRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new ImageRecyclerViewAdapter(this, savedResponse);
        imageRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.fab_sync)
    public void onViewClicked() {
        setImageGrid();
        Toast.makeText(this, "Data Synced", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dataModelObserver != null)
            dataModelMutableLiveData.removeObserver(dataModelObserver);
    }
}
