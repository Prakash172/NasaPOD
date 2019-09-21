package com.androidcodeshop.obviousassigment.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidcodeshop.obviousassigment.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailViewFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DATE_STR = "param1";
    @BindView(R.id.iv_display)
    ImageView ivDisplay;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.guideline_vertical)
    Guideline guidelineVertical;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    Unbinder unbinder;

    private String mDate;


    public DetailViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param date Parameter 1.
     * @return A new instance of fragment DetailViewFragment.
     */
    public static DetailViewFragment newInstance(String date) {
        DetailViewFragment fragment = new DetailViewFragment();
        Bundle args = new Bundle();
        args.putString(DATE_STR, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDate = getArguments().getString(DATE_STR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvDate.setText(mDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
