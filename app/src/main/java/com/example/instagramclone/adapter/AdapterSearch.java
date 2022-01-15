package com.example.instagramclone.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.MyViewHolder> {

    private List<User> userList;
    private Context context;

    public AdapterSearch(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_user, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getName());
        if (user.getPhotoPath() != null){
            Uri url = Uri.parse(user.getPhotoPath());
            Glide.with(context)
                    .load(url)
                    .into(holder.photo);
        } else {
            Log.i("foto", "foto: " + user.getPhotoPath());
            holder.photo.setImageResource(R.drawable.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView photo;
        private TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imagePhotoSearch);
            name = itemView.findViewById(R.id.textNameSearch);
        }
    }
}
