package com.sparsh.olaplaystudios;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;

public class MusicService extends Service implements  MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //binder object
    private final IBinder musicBind = new MusicBinder();
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Songs> songs;
    //current position
    private int songPosn;

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;

    public void onCreate(){
        //create the service
        super.onCreate();
        //initialize position
        songPosn=0;
        //create player
        player = new MediaPlayer();
        initMusicPlayer();
    }

    //method to receive Songlist for playback

    public void setList(ArrayList<Songs> theSongs){
        songs=theSongs;
    }

    public MusicService() {
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void initMusicPlayer(){
        //set player properties`
        player.setWakeMode(getApplicationContext(),PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    //play a song

    public void playSong(){
        player.reset(); //reseting mediaplayer from previous songs
        //get song
        Songs playSong = songs.get(songPosn);
        //get id
        long currSong =Long.parseLong(playSong.getID());
        //set uri
        Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,currSong);
        //using trackUri as datasource
        try{player.setDataSource(getApplicationContext(), trackUri);}
        catch(Exception e){Log.e("MUSIC SERVICE", "Error setting data source", e);}
        //prepare music player after which on prepared method is called
        player.prepareAsync();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        //start playback
        mediaPlayer.start();
    }

    //method to get a particular song
    public void setSong(int songIndex){
        songPosn=songIndex;
    }
}
