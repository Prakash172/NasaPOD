package com.androidcodeshop.obviousassigment.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.androidcodeshop.obviousassigment.R;
import com.androidcodeshop.obviousassigment.adapters.ImageRecyclerViewAdapter;
import com.androidcodeshop.obviousassigment.adapters.ImageViewPagerAdapter;
import com.androidcodeshop.obviousassigment.database.MyDatabase;
import com.androidcodeshop.obviousassigment.datamodels.DayResponseDataModel;
import com.androidcodeshop.obviousassigment.utils.AppUtils;
import com.androidcodeshop.obviousassigment.utils.CustomViewPager;
import com.androidcodeshop.obviousassigment.viewmodels.ImageViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailViewActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    CustomViewPager viewpager;
    private ImageViewPagerAdapter mAdapter;
    public static int mSelectedDatePos;
    private MyDatabase mDatabase;
    List<DayResponseDataModel> mUrlResponses;
    private ImageViewModel mImageViewModel;
    private AppCompatActivity contextActivity;
    private boolean update = true;
    private MutableLiveData<DayResponseDataModel> dataModelMutableLiveData;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        ButterKnife.bind(this);
        mDatabase = MyDatabase.getDatabase(this);
        contextActivity = this;
        loadAllSavedResponse();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mSelectedDatePos = bundle.getInt(ImageRecyclerViewAdapter.SELECTED_POS);
        }
        mImageViewModel = ViewModelProviders.of(contextActivity).get(ImageViewModel.class);
        dataModelMutableLiveData = mImageViewModel.getImageByDateObservable();
        progressDialog = new ProgressDialog(contextActivity);
        progressDialog.setMessage("Fetching from satellite...");
        mAdapter = new ImageViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mAdapter);
        tabs.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(mSelectedDatePos, true);
        dataModelMutableLiveData.observe(contextActivity, new Observer<DayResponseDataModel>() {
            @Override
            public void onChanged(@Nullable DayResponseDataModel dayResponseDataModel) {
                if (dayResponseDataModel != null) {
                    onDataChange(dayResponseDataModel);
                }
                progressDialog.hide();
            }
        });
        viewpager.setOnSwipeOutListener(new CustomViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtStart() {

            }

            @Override
            public void onSwipeOutAtEnd() {
                progressDialog.show();
                int currentPos = viewpager.getCurrentItem();
                String currentDate = MainActivity.responseDataModelArrayList.get(currentPos).getDate();
                String previousDay = AppUtils.previousDay(currentDate);
                mImageViewModel.getImageByDateNetworkCall(previousDay, getString(R.string.api_key));

            }
        });
    }

    private void onDataChange(DayResponseDataModel dayResponseDataModel) {
        MainActivity.responseDataModelArrayList.clear();
        mDatabase.resposeDao().insert(dayResponseDataModel);
        MainActivity.responseDataModelArrayList.addAll(mDatabase.resposeDao().getAllResponse());
        mAdapter.notifyDataSetChanged();
        viewpager.setCurrentItem(viewpager.getCurrentItem() + 1, true);
    }

    private void loadAllSavedResponse() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching database");
        progressDialog.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mUrlResponses = mDatabase.resposeDao().getAllResponse();
                progressDialog.hide();
            }
        });

    }
}
