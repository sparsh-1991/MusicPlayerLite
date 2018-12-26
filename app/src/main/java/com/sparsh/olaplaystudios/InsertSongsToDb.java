package com.sparsh.olaplaystudios;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class InsertSongsToDb extends AsyncTask<Void,Void,Integer> {

    int jsonArrayLength = 1;
    public static final String TAG = "ANY-NAME";
    ArrayList<Songs> songsArrayList = new ArrayList<>();
    SQLiteDatabase db;
    int insertSuccess =0;
    Activity mActivity;
    Cursor musicCursor;

    public InsertSongsToDb(Activity activity){
        mActivity = activity;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected Integer doInBackground(Void... params) {
        fetchSongDetails();
        addToDb();
        return 1;
    }


    void fetchSongDetails(){
        ContentResolver musicResolver = mActivity.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        musicCursor = musicResolver.query(musicUri,projection,selection,null,sortOrder);

    }

    void addToDb() {
        if (musicCursor != null) {
            Log.d(TAG, "fetchSongDetails: musicCursor is null" + musicCursor);
        }

        DatabaseHelper dbHelper = new DatabaseHelper(mActivity);
        try {            db = dbHelper.getWritableDatabase();        }
        catch(Exception e){            Log.d(TAG, "addToDb: exception" + e.getMessage());        }

        //entering the fetched song values from android system to an variable and then to a database

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int albumId = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
            int path = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int filename = musicCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int duration = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            //add songs to db
            do {
                String thisId = musicCursor.getString(idColumn);
                //String thisAlbumi = musicCursor.getString(albumId);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisPath = musicCursor.getString(path);
                String thisFilename = musicCursor.getString(filename);
                String thisDuration = musicCursor.getString(duration);
                String thisAlbumPath = "test";

                dbHelper.insertTransaction(db,thisId,thisTitle, thisArtist,thisPath,thisFilename,thisDuration, "0");
            }
            while (musicCursor.moveToNext());
            musicCursor.close();
            dbHelper.printDB(db);
            db.close();

        }
    }

    protected void onPostExecute(Integer result){

    }
}
