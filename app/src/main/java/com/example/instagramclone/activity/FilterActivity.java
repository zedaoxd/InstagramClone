package com.example.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.instagramclone.R;
import com.example.instagramclone.adapter.AdapterThumbnails;
import com.example.instagramclone.databinding.ActivityFilterBinding;
import com.example.instagramclone.util.RecyclerItemClickListener;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

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

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void clickEventRecyclerViewFilters(){
        binding.recyclerFilters.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                binding.recyclerFilters,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        ThumbnailItem item = thumbnailItemList.get(position);

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
    }

    private void retrieveFilters(){
        ThumbnailsManager.clearThumbs();
        thumbnailItemList.clear();

        // settings filter normal
        ThumbnailItem item = new ThumbnailItem();
        item.image = image;
        item.filterName = "Normal";
        ThumbnailsManager.addThumb(item);

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