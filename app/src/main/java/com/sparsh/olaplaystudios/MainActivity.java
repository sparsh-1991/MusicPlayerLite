package com.sparsh.olaplaystudios;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabPagerAdapter adapter;
    static MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    GetSongs getSongs;
    private Uri uri;
    private MediaPlayer mPlayer;
    private Songs song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getting permission for reading external storage
        checkPermission();
        setContentView(R.layout.activity_main);

        //making the tab layout

        TabLayout tabLayout =(TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new TabPagerAdapter(getSupportFragmentManager());

        //Adding Fragments here

        adapter.addFragment(new AllSongsFragment(),"All Songs");
        adapter.addFragment(new FavoriteSongsFragment(),"Favorite Songs");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager,true);

        //adding listener when page changes
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //updating page on being scrolled

            @Override
            public void onPageSelected(int position) {
                if(position == 1){
                    FavoriteSongsFragment fragment = (FavoriteSongsFragment) adapter.instantiateItem(viewPager,position);
                    if(fragment != null){              fragment.update();                    }
                }
                else{
                    AllSongsFragment fragment = (AllSongsFragment) adapter.instantiateItem(viewPager,position);
                    if(fragment != null){              fragment.update();                   }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();

            ArrayList<Songs> songList = new ArrayList<>();
            songList.add(GetSongs.song);

            //pass list
            Log.d("list", "onServiceConnected: value of songList "+ songList.toString());
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };


    //If Already permission is there InsertSongsToDb is called else requestPermissions() invokes nRequestPermissionsResult
    @TargetApi(Build.VERSION_CODES.M)
    void checkPermission(){
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        //new InsertSongsToDb(MainActivity.this).execute();
    }

    //handling the permission request

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    //inserting songs into DB
                    // TODO: 03-02-2018 insert songs into database for 1st installation or rescan button is clicked
                    new InsertSongsToDb(this).execute();

                } else {
                    // Permission Denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //Overriding onStart because we want to start the music service when the activity starts

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

     public void sendInfo(Songs song){
        this.song = song;
        /*this.uri = uri;
        mPlayer = MediaPlayer.create( this, uri);
        mPlayer.start();*/
    }

}
