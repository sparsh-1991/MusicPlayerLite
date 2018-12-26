package com.sparsh.olaplaystudios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "musicdb"; // the name of our database
    private static final int DB_VERSION = 1; // the version of the database

    DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        //creates the database with following attributes

        db.execSQL("CREATE TABLE PLAYLIST (_id INTEGER PRIMARY KEY AUTOINCREMENT," +"SONGID TEXT, SONGNAME TEXT, ARTISTNAME TEXT,PATH TEXT, FILENAME TEXT, DURATION TEXT, FAVORITE TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: 21-02-2018 implement db upgrade and downgrades
        //will be used when the upgrade and downgrade functionality will be enabled.
    }


    public static void insertTransaction(SQLiteDatabase db, String songid,String songname, String artistname,
                                         String path,String filename, String duration, String favorite) {
        ContentValues transactionValues = new ContentValues();
        //Log.d(TAG, "insertTransaction: "+ albumcover + songname+ artistname + path+filename+duration);
        //transactionValues.put("ALBUMCOVER",albumcover);
        transactionValues.put("SONGID",songid);
        transactionValues.put("SONGNAME", songname);
        transactionValues.put("ARTISTNAME", artistname);
        transactionValues.put("PATH", path);
        transactionValues.put("FILENAME",filename);
        transactionValues.put("DURATION",duration);
        transactionValues.put("FAVORITE", favorite);
        // TODO: 29-12-2017 avoid duplicates 
        db.insert("PLAYLIST", null, transactionValues);

    }

    public static  void printDB(SQLiteDatabase db){
        Log.d(TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", "PLAYLIST");
        Cursor allRows  = db.rawQuery("SELECT * FROM " + "PLAYLIST", null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String columnname: columnNames) {
                    tableString += String.format("%s: %s\n", columnname,
                            allRows.getString(allRows.getColumnIndex(columnname)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }
        Log.d(TAG, ""+ tableString);
    }

    public static Cursor getInformation(SQLiteDatabase db,int fragmentNumber){
        Cursor cursor;
        String[] projection = {"_id","SONGID","SONGNAME","ARTISTNAME","PATH","FILENAME","DURATION","FAVORITE"};
        switch (fragmentNumber){
            case 1: cursor = db.query("PLAYLIST  ", projection, null, null, null, null, null);
                break;
            case 2:
                cursor = db.query("PLAYLIST",projection,"FAVORITE=?",new String[]{"1"},null,null,null);
                break;
            default:
                cursor = null;
        }
        return cursor;
    }

    // TODO: 15-02-2018 fix the leaking of the database

    public static Boolean isFavorite(SQLiteDatabase db,String songID){
        Cursor cursor;
        Boolean b = null;
        Log.d(TAG, "isFavorite: songID =" + songID);
        String[] projection = {"_id","SONGID","SONGNAME","ARTISTNAME","PATH","FILENAME","DURATION","FAVORITE"};
        cursor = db.query("PLAYLIST",new String[] {"FAVORITE"},"SONGID=?",new String[]{songID},null,null,null);
        Log.d(TAG, "value of cursor"+DatabaseUtils.dumpCursorToString(cursor));
        if(cursor.moveToFirst() && cursor!=null){
            if(cursor.getString(0).equals("1")){
                return true;
            }
            else{
                return false;
            }
        }
        return null;
    }


}
