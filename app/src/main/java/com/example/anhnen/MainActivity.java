package com.example.anhnen;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.anhnen.MyRetrofitBuilder;
import com.example.anhnen.PhotoAdapter;
import com.example.anhnen.R;
import com.example.anhnen.model.FPhoto;
import com.example.anhnen.model.Photo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<FPhoto> {

    private RecyclerView rcView;
    private List<Photo> photoList = new ArrayList<>();
    public PhotoAdapter adapter;
    private static final String FULL_EXTRAS = "views,media,path_alias,url_sq,url_t,url_s,url_q,url_m,url_n,url_z,url_c,url_l,url_o";
    private static final String USER_ID = "7deae6b16e812fbef3b79c2be93a7e6e";
    private static final String KEY_TOKEN = "f19e1f07f5442207";
    private static final String GET_FAVO = "flickr.favorites.getList";
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("WallpaperHD+");
        setContentView(R.layout.activity_main);
  //      overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        rcView = (RecyclerView) findViewById(R.id.rcView);
        callRequest();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_game:
                Toast.makeText(this, "Chưa có gì", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.help:
                //        Toast.makeText(this, "Chưa có gì", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void callRequest() {
//        refreshLayout.setRefreshing(true);

        MyRetrofitBuilder.getInstance().getListFavo(FULL_EXTRAS,"1", USER_ID,
                "json", KEY_TOKEN, GET_FAVO, page,
                100).enqueue(MainActivity.this);

            RecyclerView.LayoutManager linearLayoutManager = new GridLayoutManager(MainActivity.this, 2);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        rcView.setLayoutManager(staggeredGridLayoutManager);
    }


    @Override
    public void onResponse(Call<FPhoto> call, Response<FPhoto> response) {
        photoList = response.body().getPhotos().getPhoto();

        Log.e("???", photoList + "");
        Toast.makeText(this, ""+photoList, Toast.LENGTH_SHORT).show();
adapter = new PhotoAdapter(MainActivity.this, photoList) ;
        rcView.setAdapter(adapter);
    }

    @Override
    public void onFailure(Call<FPhoto> call, Throwable t) {

    }
}
