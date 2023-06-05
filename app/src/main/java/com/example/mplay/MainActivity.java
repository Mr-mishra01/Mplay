package com.example.mplay;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView txtview;
    ArrayList <AudioModel> songsList = new ArrayList<>();
    private static final int REQUEST_READ_EXTERNAL_STORAGE=1;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        checkPermission();
//        requestPermission();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, so request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_AUDIO},
                    REQUEST_READ_EXTERNAL_STORAGE);

        } else {
            // Permission has already been granted
            // You can now access the external storage for reading
            // TODO: Perform your actions here
        }
        recyclerView = findViewById(R.id.recycler_view);
        txtview = findViewById(R.id.no_songs);
//        if (!checkPermission()){
//            requestPermission();
//            return;
//        }
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION


        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";


        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);
        while (cursor.moveToNext()){
            AudioModel songData = new AudioModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
            if (new File(songData.getPath()).exists())
            songsList.add(songData);
        }
        if (songsList.size()==0){
            txtview.setVisibility(View.VISIBLE);
        }
        else {
            //recyclerview
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicListAdapter(songsList,getApplicationContext()));
        }
    }


    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else return false;
    }
    void requestPermission(){
//        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
//            Toast.makeText(MainActivity.this, "READ PERMISSION IS REQUIRED , PLEASE ALLOW FROM SETTINGS", Toast.LENGTH_SHORT).show();
//        }else {

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_MEDIA_AUDIO},123);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView !=null){
            recyclerView.setAdapter (new MusicListAdapter(songsList,getApplicationContext()));
        }
    }
}