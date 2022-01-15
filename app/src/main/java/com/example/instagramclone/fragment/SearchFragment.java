package com.example.instagramclone.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.example.instagramclone.activity.FriendProfileActivity;
import com.example.instagramclone.adapter.AdapterSearch;
import com.example.instagramclone.databinding.FragmentSearchBinding;
import com.example.instagramclone.model.User;
import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.RecyclerItemClickListener;
import com.example.instagramclone.util.StringUtils;
import com.example.instagramclone.util.UserFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private List<User> userList;
    private DatabaseReference userRef;
    private AdapterSearch adapterSearch;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        initialSettings();

        searchView();
        clickEventRecyclerView();
        return binding.getRoot();
    }

    private void initialSettings(){
        userList = new ArrayList<>();
        userRef = FirebaseUtils.getDatabaseReference().child("users");

        // adapter
        adapterSearch = new AdapterSearch(userList, getContext());

        // recycler view
        binding.recyclerViewSearch.setHasFixedSize(true);
        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewSearch.setAdapter(adapterSearch);
    }

    private void searchView(){
        binding.searchView.setQueryHint("Buscar usuários");

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String searchText = s.toUpperCase();
                searchUsers(searchText);
                return true;
            }
        });
    }

    private void searchUsers(@NonNull String searchText){
        userList.clear();

        if (searchText.length() > 1){
            Query query = userRef.orderByChild("name")
                    .startAt(searchText)
                    .endAt(searchText + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    userList.clear();
                    String idCurrentUser = UserFirebase.getUserId();

                    for (DataSnapshot ds : snapshot.getChildren()){
                        User user = ds.getValue(User.class);

                        if (user.getId().equals(idCurrentUser)){
                            continue;
                        }

                        userList.add(user);
                    }

                    adapterSearch.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void clickEventRecyclerView(){
        binding.recyclerViewSearch.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                binding.recyclerViewSearch,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        User selectedUser = userList.get(position);
                        Intent i = new Intent(getActivity(), FriendProfileActivity.class);
                        i.putExtra(StringUtils.friendProfile, selectedUser);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));
    }
}