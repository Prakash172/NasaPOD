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
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.androidcodeshop.obviousassigment.R;
import com.androidcodeshop.obviousassigment.adapters.ImageRecyclerViewAdapter;
import com.androidcodeshop.obviousassigment.datamodels.DayResponseDataModel;
import com.androidcodeshop.obviousassigment.viewmodels.MainActivityViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_recycler_view)
    RecyclerView imageRecyclerView;
    @BindView(R.id.fab_share)
    FloatingActionButton fab;
    private ArrayList<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        imageUrls = new ArrayList<>();
//        SharedPreferences sharedPreferences = getSharedPreferences("current_date",0);
//        sharedPreferences.
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        imageRecyclerView.setLayoutManager(gridLayoutManager);
        ImageRecyclerViewAdapter adapter = new ImageRecyclerViewAdapter(this,imageUrls);
        imageRecyclerView.setAdapter(adapter);
        MainActivityViewModel mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Wait...");
        progressDialog.setMessage("Picture is coming from satellite");
        progressDialog.show();
        mainActivityViewModel.getImageByDateObservable("2019-09-18","8dRnZMRZQKBGuPVu9OeHAUmc03c7qhGS97xKQM2a").observe(this, new Observer<DayResponseDataModel>() {
            @Override
            public void onChanged(@Nullable DayResponseDataModel dayResponseDataModel) {
                Log.d(TAG, "onChanged: "+dayResponseDataModel.getTitle());
                imageUrls.add(0,dayResponseDataModel.getUrl());
                adapter.notifyDataSetChanged();
                progressDialog.hide();
            }
        });

    }

    @OnClick(R.id.fab_share)
    public void onViewClicked() {

    }
}
