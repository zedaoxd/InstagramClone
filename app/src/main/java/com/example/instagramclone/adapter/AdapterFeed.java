package com.example.instagramclone.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.model.Feed;
import com.example.instagramclone.model.LikesPosts;
import com.example.instagramclone.model.User;
import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.StringUtils;
import com.example.instagramclone.util.UserFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jackandphantom.androidlikebutton.AndroidLikeButton;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

    private List<Feed> feedList;
    private Context context;

    public AdapterFeed(List<Feed> feedList, Context context) {
        this.feedList = feedList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Feed feed = feedList.get(position);
        User currentUser = UserFirebase.getDataCurrentUser();
        String urlUserPhotoString = feed.getPathPhotoProfileUser();

        if (urlUserPhotoString != null) {
            Uri urlUserPhoto = Uri.parse(urlUserPhotoString);
            Glide.with(context)
                    .load(urlUserPhoto)
                    .into(holder.photoProfile);
        } else {
            holder.photoProfile.setImageResource(R.drawable.avatar);
        }

        String urlPostPhotoString = feed.getPathPhoto();
        if (urlPostPhotoString != null) {
            Uri urlPostPhoto = Uri.parse(urlPostPhotoString);
            Glide.with(context)
                    .load(urlPostPhoto)
                    .into(holder.postPhoto);
        } else {
            holder.postPhoto.setImageResource(R.drawable.avatar);
        }

        holder.description.setText(feed.getDescription());
        holder.name.setText(feed.getUserName());

        DatabaseReference likesPostsRef = FirebaseUtils.getDatabaseReference()
                .child(StringUtils.likesPosts)
                .child(feed.getId());
        likesPostsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int quantityLikes = 0;
                if (snapshot.hasChild("quantityLikes")){
                    LikesPosts likesPosts = snapshot.getValue(LikesPosts.class);
                    quantityLikes = likesPosts.getQuantityLikes();
                }

                // checks if the current user has already liked
                holder.likeButton.setCurrentlyLiked(snapshot.hasChild(currentUser.getId()));

                LikesPosts like = new LikesPosts();
                like.setFeed(feed);
                like.setUser(currentUser);
                like.setQuantityLikes(quantityLikes);

                holder.likeButton.setOnLikeEventListener(new AndroidLikeButton.OnLikeEventListener() {
                    @Override
                    public void onLikeClicked(AndroidLikeButton androidLikeButton) {
                        like.save();
                        holder.likesQuantity.setText(like.getQuantityLikes() + " curtidas");
                    }

                    @Override
                    public void onUnlikeClicked(AndroidLikeButton androidLikeButton) {
                        like.removeLike();
                        holder.likesQuantity.setText(like.getQuantityLikes() + " curtidas");
                    }
                });

                holder.likesQuantity.setText(like.getQuantityLikes() + " curtidas");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView photoProfile;
        TextView name, description, likesQuantity;
        ImageView postPhoto, commentButton;
        AndroidLikeButton likeButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            photoProfile = itemView.findViewById(R.id.imageProfilePostClicked);
            name = itemView.findViewById(R.id.textNameProfilePostClicked);
            description = itemView.findViewById(R.id.textDescriptionPost);
            likesQuantity = itemView.findViewById(R.id.textPostLikes);
            postPhoto = itemView.findViewById(R.id.imagePostSelectedClicked);
            commentButton = itemView.findViewById(R.id.buttonComentarioFeed);
            likeButton = itemView.findViewById(R.id.likeButton);
        }
    }
}
