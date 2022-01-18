package com.example.instagramclone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.model.Comment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyViewHolder> {

    private Context context;
    private List<Comment> list;

    public AdapterComments(Context context, List<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comments , parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Comment comment = list.get(position);

        holder.name.setText(comment.getUserName());
        holder.comment.setText(comment.getComment());

        String pathImage = comment.getPathPhoto();
        if (pathImage!= null){
            Uri url = Uri.parse(pathImage);
            Glide.with(context).load(url).into(holder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView circleImageView;
        private TextView name, comment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.imageComments);
            name = itemView.findViewById(R.id.textUserName);
            comment = itemView.findViewById(R.id.textViewComment);
        }
    }
}
