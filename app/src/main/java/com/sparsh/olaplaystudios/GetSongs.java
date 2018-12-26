package com.sparsh.olaplaystudios;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class GetSongs extends AsyncTask<Void,Void,ArrayList<Songs>> {

    private Context context;
    private View myFragmentView;
    private int fragmentNumber = 0;
    private SQLiteDatabase db;
    DatabaseHelper dbHelper ;
    static Songs song;

    public GetSongs(Context context,View myFragmentView, int fragmentNumber){
        this.context = context;
        this.myFragmentView = myFragmentView;
        this.fragmentNumber = fragmentNumber;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected ArrayList<Songs> doInBackground(Void... params) {
        ArrayList<Songs> allsongs = new ArrayList<>();
        ArrayList<Songs> favsongs = new ArrayList<>();
        dbHelper = new DatabaseHelper(context);
        try {
            db = dbHelper.getWritableDatabase();
        }
        catch (Exception e){
            Log.d(TAG, "doInBackground: exception" + e.getMessage());
        }

        //getting information according to fragment and adding it respective songs list.

        Cursor cursor = dbHelper.getInformation(db,fragmentNumber);
        Log.v("Cursor in getSongs", DatabaseUtils.dumpCursorToString(cursor));
        if (cursor!=null && cursor.moveToFirst()){
            do {

                song = new Songs(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7));
                switch (fragmentNumber){
                    case 1: allsongs.add(song);
                        break;
                    case 2: favsongs.add(song);
                        break;
                    default:
                        break;
                }
            }while(cursor!=null && cursor.moveToNext());

        }
        else{
            Log.d(TAG, "doInBackground: cursor supposed to contain songs in getSongs.java is null");
        }
        cursor.close();
        db.close();

        switch (fragmentNumber){
            case 1: return allsongs;
            case 2: return favsongs;
            default:
                Log.d(TAG, "doInBackground: (GetSongs.java) fragment number id neither 1 nor 2 hence no fragment is called \n");
                return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Songs> result){

        //attaching returned songlist to RecyclerView

        RecyclerViewAdapter adapter;
        RecyclerView recyclerView = null;
        switch (fragmentNumber){
            case 1: recyclerView = (RecyclerView) myFragmentView.findViewById(R.id.all_songs_recycler);
                break;
            case 2: recyclerView = (RecyclerView) myFragmentView.findViewById(R.id.fav_song_recycler);
                break;
            default:
                break;

        }
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new RecyclerViewAdapter(result,context);
        recyclerView.setAdapter(adapter);



    }
}
