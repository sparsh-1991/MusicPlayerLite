package com.sparsh.olaplaystudios;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static android.R.id.list;


public class AllSongsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View myFragmentView;
    RecyclerView.Adapter adapter;
    Context context;

    ArrayList<Songs> songsArrayList;
    private static String url = "http://starlord.hackerearth.com/studio";

    public void update() {
        new GetSongs(context,myFragmentView,1).execute();
    }

    // TODO: 21-02-2018 make a separate  updateable interface and implement it in both allsongsfragment and favoritesongsfragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_all_songs,container,false);
        context = (MainActivity)getActivity();

        //sending context and the fragment view to asynctask to be able to use findViewbyId

        new GetSongs(context,myFragmentView,1).execute();
        return myFragmentView;
    }



}
