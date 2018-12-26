package com.sparsh.olaplaystudios;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class Songs {

    String id;
    String songID;
    String songname = "testsongname";
    String artistname = "testartistname";
    String path = "testpath";
    String fileName="testfilename";
    String duration = "duration";
    String favorite = "0";


    public Songs(String id,String songID, String songname, String artistname, String path,String filename, String duration, String favorite) {
        this.id =id;
        this.songID = songID;
        this.songname = songname;
        this.artistname = artistname;
        this.path = path;
        this.fileName = fileName;
        this.duration = duration;
        this.favorite = favorite;
    }

    public String getfilename(){ return fileName;}

    public  void setfilename(){this.fileName = fileName;}

    public String getID(){ return id;}

    public  void setID(){this.id = id;}

    public String getSongname() {     return songname;    }

    public void setSongame(String songname) {
        this.songname = songname;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public Boolean getFavorite() {
        if (favorite.equals("0"))
            return false;
        else if(favorite.equals("1"))
            return true;
        else return null;
    }


    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}

