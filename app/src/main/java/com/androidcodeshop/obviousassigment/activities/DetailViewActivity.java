package com.androidcodeshop.obviousassigment.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        ButterKnife.bind(this);
        mImageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        mDatabase = MyDatabase.getDatabase(this);
        contextActivity = this;
        loadAllSavedResponse();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mSelectedDatePos = bundle.getInt(ImageRecyclerViewAdapter.SELECTED_POS);
        }

        mAdapter = new ImageViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mAdapter);
        tabs.setupWithViewPager(viewpager);
        viewpager.setOnSwipeOutListener(new CustomViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtStart() {

                Toast.makeText(DetailViewActivity.this, "Swipe at start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeOutAtEnd() {
                int currentPos = viewpager.getCurrentItem();
                String currentDate = MainActivity.responseDataModelArrayList.get(currentPos).getDate();
                String previousDay = AppUtils.previousDay(currentDate);
                update = true;
                MutableLiveData<DayResponseDataModel> dataModelMutableLiveData = mImageViewModel.getImageByDateObservable(previousDay, getString(R.string.api_key));
                ProgressDialog progressDialog = new ProgressDialog(contextActivity);
                progressDialog.setMessage("Fetching from satellite...");
                if (mDatabase.resposeDao().getImageByDate(previousDay) == null) {
                    progressDialog.show();
                    dataModelMutableLiveData.observe(contextActivity, new Observer<DayResponseDataModel>() {
                        @Override
                        public void onChanged(@Nullable DayResponseDataModel dayResponseDataModel) {
                            if (dayResponseDataModel != null) {
                                dataModelMutableLiveData.removeObserver(this);
                                progressDialog.hide();
                                if (update) {
                                    MainActivity.responseDataModelArrayList.add(dayResponseDataModel);
                                    mDatabase.resposeDao().insert(dayResponseDataModel);
                                    mAdapter.notifyDataSetChanged();
                                }
                                update = false;
                                viewpager.setCurrentItem(currentPos + 1, true);
                            }
                        }
                    });
                }
                Toast.makeText(DetailViewActivity.this, "swiped at end", Toast.LENGTH_SHORT).show();
            }
        });
        viewpager.setCurrentItem(mSelectedDatePos, true);
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
