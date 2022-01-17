package com.example.instagramclone.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.R;
import com.example.instagramclone.adapter.AdapterFeed;
import com.example.instagramclone.databinding.FragmentFeedBinding;
import com.example.instagramclone.databinding.FragmentPerfilBinding;
import com.example.instagramclone.model.Feed;
import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.StringUtils;
import com.example.instagramclone.util.UserFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedFragment extends Fragment {

    private FragmentFeedBinding binding;
    private AdapterFeed adapterFeed;
    private List<Feed> listFeed = new ArrayList<>();
    private ValueEventListener valueEventListener;
    private DatabaseReference feedRef;
    private String idCurrentUser;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentFeedBinding.inflate(inflater, container, false);

        initialSettings();
        settingRecyclerFeed();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        listsFeed();
    }

    @Override
    public void onStop() {
        super.onStop();
        feedRef.removeEventListener(valueEventListener);
    }

    private void initialSettings(){

        idCurrentUser = UserFirebase.getUserId();
        feedRef = FirebaseUtils.getDatabaseReference()
                .child(StringUtils.feed)
                .child(idCurrentUser);
    }

    private void settingRecyclerFeed(){
        adapterFeed = new AdapterFeed(listFeed, getActivity());
        binding.recyclerFeed.setHasFixedSize(true);
        binding.recyclerFeed.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerFeed.setAdapter(adapterFeed);
    }

    private void listsFeed(){
        valueEventListener = feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    listFeed.add(ds.getValue(Feed.class));
                }
                Collections.reverse(listFeed);
                adapterFeed.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}