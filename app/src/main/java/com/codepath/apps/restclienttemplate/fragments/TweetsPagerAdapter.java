package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by gpailet on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[]=new String[]{"Home","Mentions"};
    private Context context;

    HomeTimelineFragment homeTimelineFragment;
    MentionsTimelineFragment mentionsTimelineFragment;

    public TweetsPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context=context;
        homeTimelineFragment = (HomeTimelineFragment) new HomeTimelineFragment();
        mentionsTimelineFragment = (MentionsTimelineFragment) new MentionsTimelineFragment();
    }
    //return the total # of fragments

    @Override
    public int getCount() {
        return 2;
    }


    // return the fragment to use depending on the position

    @Override
    public TweetsListFragment getItem(int position) {
        if (position==0){
            return homeTimelineFragment;
        }
        else if(position==1){
            return mentionsTimelineFragment;
        }
        else{
            return null;
        }
    }

    //return title based on item position


    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
