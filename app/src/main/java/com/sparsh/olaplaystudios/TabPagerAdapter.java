package com.sparsh.olaplaystudios;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class TabPagerAdapter extends FragmentPagerAdapter  {

    private final List<Fragment> listFragment = new ArrayList<>();
    private final List<String> listTitles = new ArrayList();

    public TabPagerAdapter(FragmentManager fragment){
        super(fragment);
    }
    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        FavoriteSongsFragment f = (FavoriteSongsFragment ) object;
        if (f != null) {
            f.update();
        }
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitles.get(position);
    }

    @Override
    public int getCount(){
        return listTitles.size();
    }

    public void addFragment(Fragment fragment,String title){
        listFragment.add(fragment);
        listTitles.add(title);
    }

}
