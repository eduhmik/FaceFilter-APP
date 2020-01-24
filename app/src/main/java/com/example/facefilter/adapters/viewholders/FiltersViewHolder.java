package com.example.facefilter.adapters.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.example.facefilter.R;
import com.example.facefilter.model.TrendingFilters;
import com.example.facefilter.sharedprefs.SharedPrefs;
import com.example.facefilter.tools.GlideApp;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FiltersViewHolder extends RecyclerView.ViewHolder {

    SharedPrefs sharedPrefs;
    public static String PHOTO_URL="";
    private Context context;
    TrendingFilters trendingFilters;
    @BindView(R.id.filters_image)
    ImageView filtersImage;

    public FiltersViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        sharedPrefs = new SharedPrefs(context);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final TrendingFilters trendingFilters) {
        this.trendingFilters = trendingFilters;
        if(trendingFilters.getEmbedUrl() != null) {
            GlideApp.with(context)
                    .load(trendingFilters.getEmbedUrl())
                    .placeholder(R.drawable.fox_face_mesh_texture)
                    .apply(RequestOptions.circleCropTransform())
                    .fitCenter()
                    .dontAnimate()
                    .into(filtersImage);
            PHOTO_URL = trendingFilters.getEmbedUrl();
        }else{

        }
        itemView.setOnClickListener(view -> {
            Toast.makeText(context, "Item clicked: " + trendingFilters.getId(), Toast.LENGTH_SHORT).show();
        });
    }

    @OnClick(R.id.filters_image)
    public void onViewClicked() {
    }
}

