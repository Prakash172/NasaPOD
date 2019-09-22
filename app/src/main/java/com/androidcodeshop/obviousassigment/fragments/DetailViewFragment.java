package com.androidcodeshop.obviousassigment.fragments;


import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidcodeshop.obviousassigment.R;
import com.androidcodeshop.obviousassigment.activities.MainActivity;
import com.androidcodeshop.obviousassigment.datamodels.DayResponseDataModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Locale;

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
    private static final String SELECTED_POSITION = "param1";
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
    private static DayResponseDataModel dayResponseDataModel;
    @BindView(R.id.tv_copyright)
    TextView tvCopyright;
    @BindView(R.id.tv_no_image)
    TextView tvNoImage;
    @BindView(R.id.progress_circular)
    ProgressBar progressCircular;
    private int mPos;


    public DetailViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pos Parameter 1.
     * @return A new instance of fragment DetailViewFragment.
     */
    public static DetailViewFragment newInstance(int pos) {
        DetailViewFragment fragment = new DetailViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SELECTED_POSITION, pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPos = getArguments().getInt(SELECTED_POSITION);
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
        tvDate.setText(MainActivity.responseDataModelArrayList.get(mPos).getDate());
        Glide.with(getContext())
                .load(Uri.parse(MainActivity.responseDataModelArrayList.get(mPos).getUrl()))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressCircular.setVisibility(View.GONE);
                        tvNoImage.setText(getContext().getResources().getString(R.string.failed_label));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressCircular.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(ivDisplay);
        tvDescription.setText(MainActivity.responseDataModelArrayList.get(mPos).getExplanation());
        tvTitle.setText(MainActivity.responseDataModelArrayList.get(mPos).getTitle());
        tvCopyright.setText(String.format(Locale.ENGLISH, "\u00a9 %s", MainActivity.responseDataModelArrayList.get(mPos).getCopyright()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
