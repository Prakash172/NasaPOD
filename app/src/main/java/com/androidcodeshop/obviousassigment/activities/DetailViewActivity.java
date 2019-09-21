package com.androidcodeshop.obviousassigment.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.androidcodeshop.obviousassigment.R;
import com.androidcodeshop.obviousassigment.adapters.ImageRecyclerViewAdapter;
import com.androidcodeshop.obviousassigment.adapters.ImageViewPagerAdapter;
import com.androidcodeshop.obviousassigment.utils.CustomViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailViewActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    CustomViewPager viewpager;
    private ImageViewPagerAdapter mAdapter;
    public static String mSelectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mSelectedDate = bundle.getString(ImageRecyclerViewAdapter.SELECTED_DATE_STR);
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
                Toast.makeText(DetailViewActivity.this, "swiped at end", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
