package com.androidcodeshop.obviousassigment.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidcodeshop.obviousassigment.R;
import com.androidcodeshop.obviousassigment.activities.DetailViewActivity;
import com.androidcodeshop.obviousassigment.datamodels.DayResponseDataModel;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder> {
    public static final String SELECTED_POS = "date";
    public static int selectedPos = 0;
    private Context mContext;
    private List<DayResponseDataModel> imageUrls;

    public ImageRecyclerViewAdapter(Context mContext, List<DayResponseDataModel> imageUrls) {
        this.mContext = mContext;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_rv, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(mContext).load(Uri.parse(imageUrls.get(i).getUrl())).into(viewHolder.image);
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailViewActivity.class);
                intent.putExtra(SELECTED_POS, i);
                selectedPos = i;
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
