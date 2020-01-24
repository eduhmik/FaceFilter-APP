package com.example.facefilter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.facefilter.R;
import com.example.facefilter.adapters.viewholders.FiltersViewHolder;
import com.example.facefilter.model.TrendingFilters;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersViewHolder> {

    private Context context;
    private ArrayList<TrendingFilters> trendingFiltersArrayList;

    public FiltersAdapter(Context context, ArrayList<TrendingFilters> trendingFiltersArrayList) {
        this.trendingFiltersArrayList = trendingFiltersArrayList;
        this.context = context;
    }


    @Override
    public FiltersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_filter_item, parent, false);
        return new FiltersViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(final FiltersViewHolder holder, int position) {
        holder.bind(trendingFiltersArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return trendingFiltersArrayList.size();
    }
}