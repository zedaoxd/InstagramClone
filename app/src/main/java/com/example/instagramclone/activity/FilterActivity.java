package com.example.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.adapter.AdapterThumbnails;
import com.example.instagramclone.databinding.ActivityFilterBinding;
import com.example.instagramclone.model.Post;
import com.example.instagramclone.model.User;
import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.RecyclerItemClickListener;
import com.example.instagramclone.util.StringUtils;
import com.example.instagramclone.util.UserFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterActivity extends AppCompatActivity {

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    private ActivityFilterBinding binding;
    private Bitmap image;
    private Bitmap imageFilter;
    private List<ThumbnailItem> thumbnailItemList;
    private AdapterThumbnails adapterThumbnails;
    private String idCurrentUser;
    private ThumbnailItem regularFilter;
    private DatabaseReference currentUserRef;
    private DatabaseReference usersRef;
    private boolean isLoading;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initialSettings();
        settingsToolbar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            byte[] dataImage = bundle.getByteArray("photo");
            image = BitmapFactory.decodeByteArray(dataImage, 0, dataImage.length);
            imageFilter = image.copy(image.getConfig(), true );
            binding.imagePhotoFilter.setImageBitmap(image);

            recyclerViewThumbs();
            clickEventRecyclerViewFilters();
            retrieveFilters();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_filter_post){
            publishPost();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void publishPost(){

        if (isLoading){

            Toast.makeText(getApplicationContext(), "carregando dados aguarde", Toast.LENGTH_LONG).show();

        } else {

            String description = binding.textDescriptionFilter.getText().toString();
            Post post = new Post();
            post.setUserId(idCurrentUser);
            post.setDescription(description);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageFilter.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] dataImage = baos.toByteArray();

            StorageReference storageRef = FirebaseUtils.getFirebaseStorage();
            StorageReference imageRef = storageRef
                    .child(StringUtils.images)
                    .child(StringUtils.posts)
                    .child(post.getId() + ".jpeg");

            UploadTask uploadTask = imageRef.putBytes(dataImage);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FilterActivity.this,
                            "Erro ao salvar postagem",
                            Toast.LENGTH_LONG).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageRef.getDownloadUrl().addOnCompleteListener(
                            new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    post.setPathPhoto(url.toString());

                                    if (post.save()){

                                        int postQuantity = currentUser.getPosts() + 1;
                                        currentUser.setPosts(postQuantity);
                                        currentUser.updatePostQuantity();

                                        Toast.makeText(FilterActivity.this,
                                                "Sucesso ao salvar postagem",
                                                Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }
                            }
                    );
                }
            });
        }
    }


    private void recoverLoggedUserData(){

        loadingData(true);

        currentUserRef = usersRef.child(idCurrentUser);
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                currentUser = snapshot.getValue(User.class);
                loadingData(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void clickEventRecyclerViewFilters(){
        binding.recyclerFilters.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                binding.recyclerFilters,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        ThumbnailItem item = thumbnailItemList.get(position);
                        // remove the current filter before applying the other one
                        imageFilter = image.copy(image.getConfig(), true );

                        Filter filter = item.filter;
                        binding.imagePhotoFilter.setImageBitmap(filter.processFilter(imageFilter));
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

    private void recyclerViewThumbs(){
        // adapter
        adapterThumbnails = new AdapterThumbnails(thumbnailItemList, getApplicationContext());

        // recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerFilters.setLayoutManager(layoutManager);
        binding.recyclerFilters.setHasFixedSize(true);
        binding.recyclerFilters.setAdapter(adapterThumbnails);
    }

    private void initialSettings(){
        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        thumbnailItemList = new ArrayList<>();
        idCurrentUser = UserFirebase.getUserId();
        usersRef = FirebaseUtils.getDatabaseReference().child(StringUtils.users);

        recoverLoggedUserData();
    }

    private void loadingData(boolean state){
        if (state){
            isLoading = true;
            binding.progressBarSavePost.setVisibility(View.VISIBLE);
        } else {
            isLoading = false;
            binding.progressBarSavePost.setVisibility(View.GONE);
        }
    }

    private void retrieveFilters(){
        ThumbnailsManager.clearThumbs();
        thumbnailItemList.clear();

        // settings filter normal
        regularFilter = new ThumbnailItem();
        regularFilter.image = image;
        regularFilter.filterName = "Normal";
        ThumbnailsManager.addThumb(regularFilter);

        // list all filters
        List<Filter> filters = FilterPack.getFilterPack(getApplicationContext());
        for (Filter filter : filters){
            ThumbnailItem itemFilter = new ThumbnailItem();
            itemFilter.image = image;
            itemFilter.filter = filter;
            itemFilter.filterName = filter.getName();

            ThumbnailsManager.addThumb(itemFilter);
        }

        thumbnailItemList.addAll(ThumbnailsManager.processThumbs(getApplicationContext()));

        adapterThumbnails.notifyDataSetChanged();
    }

    private void settingsToolbar(){
        setSupportActionBar(binding.include.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setTitle(R.string.filters);
    }
}