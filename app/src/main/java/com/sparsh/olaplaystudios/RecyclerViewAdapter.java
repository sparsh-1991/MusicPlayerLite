package com.sparsh.olaplaystudios;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.sparsh.olaplaystudios.DatabaseHelper.isFavorite;
//import static com.sparsh.olaplaystudios.R.id.cover_image;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Songs> list = new ArrayList<>();
    private Context context;
    SQLiteDatabase db;
    MediaPlayer mplayer;

    protected RecyclerViewAdapter(ArrayList<Songs> list, Context Context) {
        this.list = list;
        //context = Context;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //getting values ui elements in cardView

        final ImageButton favorite = (ImageButton) holder.cardView.findViewById(R.id.favorite);
        TextView songname = (TextView) holder.cardView.findViewById(R.id.song_name);
        TextView artistname = (TextView) holder.cardView.findViewById(R.id.artistname);


        //getting and setting ImageView details of songs at a particular position in recyclerView

        final Songs song = list.get(position);

        //populating songname,artistname at particular position of recyclerView

        artistname.setText(song.getArtistname());
        songname.setText(song.getSongname());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri =  Uri.parse("file:///"+song.getPath());
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.sendInfo(song);
                /*try {
                    mplayer.setDataSource(context, uri);
                }
                catch (Exception e){
                    Log.d(TAG, "exception"+e.getMessage());
                }
                mplayer.start();
                */
            }
        });

        //setting value if favorite icon as per favorite value in the songs object

        Boolean b= song.getFavorite();
        if (b==null){
            Log.d(TAG, "onBindViewHolder: b is null (isFavorite() is not working properly)");
        }
        if(b == true){
            Log.d(TAG, "onBindViewHolder: favorite value at position" + position + "is 1");
            favorite.setBackgroundResource(R.drawable.favorite);
        }
        else if (b== false){
            Log.d(TAG, "onBindViewHolder: favorite value at position" + position + "is 1");
            favorite.setBackgroundResource(R.drawable.star);
        }



        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting database
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                try {
                    if (isFavorite(db, song.getSongID())) {
                        Log.d(TAG, "onClick: it is favorite");
                        //changing the value of favorite to 1 for specific song according to position in recyclerView

                        ContentValues cv = new ContentValues();
                        cv.put("FAVORITE","0");
                        //String strSQL = "UPDATE PLAYLIST SET FAVORITE = 0 WHERE _id = "+ (position+1);
                        String strSQL = "UPDATE PLAYLIST SET FAVORITE = 0 WHERE _id = "+ (song.getID());
                        db.execSQL(strSQL);

                        //changing image of favorite
                        favorite.setBackgroundResource(R.drawable.star);
                    } else{
                        Log.d(TAG, "onClick: it isn't favorite");
                        //changing the value of favorite to 1 for specific song according to position in recyclerView

                        ContentValues cv = new ContentValues();
                        cv.put("FAVORITE","1");
                        String strSQL = "UPDATE PLAYLIST SET FAVORITE = 1 WHERE _id = "+ (position+1);
                        db.execSQL(strSQL);

                        //changing image of image button

                        favorite.setBackgroundResource(R.drawable.favorite);
                    }
                }
                catch (NullPointerException e){
                    Log.d(TAG, "onClick: is favorite returned null implies curdor is null");
                }
                db.close();
            }
        });

    }

    @Override
    public int getItemCount () {            return list.size();        }
}
