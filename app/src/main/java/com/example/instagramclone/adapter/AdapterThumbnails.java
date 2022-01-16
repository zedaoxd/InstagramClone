package com.example.instagramclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class AdapterThumbnails extends RecyclerView.Adapter<AdapterThumbnails.MyViewHolder> {

    private List<ThumbnailItem> list;
    private Context context;

    public AdapterThumbnails(List<ThumbnailItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filters , parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ThumbnailItem filter = list.get(position);
        holder.thumb.setImageBitmap(filter.image);
        holder.nameFilter.setText(filter.filterName);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView thumb;
        private TextView nameFilter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.filterImage);
            nameFilter = itemView.findViewById(R.id.filterName);
        }
    }
}
