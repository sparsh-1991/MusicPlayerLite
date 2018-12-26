package com.sparsh.olaplaystudios;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

interface Updateable {
    public void update();
}


public class FavoriteSongsFragment extends Fragment implements Updateable {

    View myFragmentView;
    Context context;

    @Override
    public void update() {
        new GetSongs(context,myFragmentView,2).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_favorite_songs,container,false);
        context = getActivity();
        // sending contect and the fragment view to asynctask to be able to use findViewbyId
        new GetSongs(context,myFragmentView,2).execute();
        return myFragmentView;
    }



}
