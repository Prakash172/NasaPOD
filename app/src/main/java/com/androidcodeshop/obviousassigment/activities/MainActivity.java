package com.androidcodeshop.obviousassigment.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_recycler_view)
    RecyclerView imageRecyclerView;

    public static ArrayList<DayResponseDataModel> sResponseDataModelArrayList;
    private ArrayList<DayResponseDataModel> mResponseDataModelArrayList;
    public ImageRecyclerViewAdapter mAdapter;
    private MyDatabase mDatabase;
    private ImageViewModel mImageViewModel;
    private MutableLiveData<DayResponseDataModel> mDataModelMutableLiveData;
    Observer<DayResponseDataModel> mDataModelObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        sResponseDataModelArrayList = new ArrayList<>();
        mResponseDataModelArrayList = new ArrayList<>();
        mImageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        mDataModelMutableLiveData = mImageViewModel.getImageByDateObservable();
        mDatabase = MyDatabase.getDatabase(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        imageRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new ImageRecyclerViewAdapter(this, mResponseDataModelArrayList);
        imageRecyclerView.setAdapter(mAdapter);

        setImageGrid();

        if (mDatabase.resposeDao().getImageByDate(AppUtils.getTodayDate()) == null) {
            loadTodaysImage(AppUtils.getTodayDate());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDataModelMutableLiveData != null && mDataModelObserver != null)
            mDataModelMutableLiveData.observe(this, mDataModelObserver);
    }

    /*
     * @param currentDate: current date is today's date,
     * this method will run only once in a day to fetch the latest image if available
     * */
    private void loadTodaysImage(String currentDate) {
        Log.d(TAG, "loadTodaysImage: " + currentDate);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Image is coming from satellite");
        progressDialog.show();
        mDataModelObserver = dayResponseDataModel -> {
            progressDialog.hide();
            if (dayResponseDataModel != null) {
                Log.d(TAG, "onChanged: " + dayResponseDataModel.getTitle());
                sResponseDataModelArrayList.add(0, dayResponseDataModel);
                mResponseDataModelArrayList.add(0, dayResponseDataModel);
                mAdapter.notifyDataSetChanged();
                mDatabase.resposeDao().insert(dayResponseDataModel);
            } else {
                Toast.makeText(MainActivity.this, "Today's Picture is not posted! Try Again Later", Toast.LENGTH_SHORT).show();
            }
        };
        mDataModelMutableLiveData.observe(this, mDataModelObserver);
        mImageViewModel.getImageByDateNetworkCall(currentDate, getString(R.string.api_key));
    }


    /**
     * This method set up image grid from database for all the fetched images
     */

    private void setImageGrid() {
        //Clear the response
        sResponseDataModelArrayList.clear();
        mResponseDataModelArrayList.clear();
        List<DayResponseDataModel> temp = mDatabase.resposeDao().getAllResponse();
        sResponseDataModelArrayList.addAll(temp);
        mResponseDataModelArrayList.addAll(temp);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mDataModelObserver != null)
            mDataModelMutableLiveData.removeObserver(mDataModelObserver);
    }

    // Double tap to exit
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to close", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
