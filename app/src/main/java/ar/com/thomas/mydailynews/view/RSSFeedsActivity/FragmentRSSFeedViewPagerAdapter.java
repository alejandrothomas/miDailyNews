package ar.com.thomas.mydailynews.view.RSSFeedsActivity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.model.RSSFeed;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentRSSFeedViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<FragmentRSSFeedViewPager> fragmentRSSFeedList;
    private List<RSSFeed> rssFeedList;


    public FragmentRSSFeedViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.rssFeedList = new ArrayList<>();
        this.fragmentRSSFeedList = new ArrayList<>();

        NewsController newsController = new NewsController();
        rssFeedList = newsController.getRSSFeedList(context);

        for (Integer i = 0; i<rssFeedList.size();i++){
            this.fragmentRSSFeedList.add(FragmentRSSFeedViewPager.generateFragment(rssFeedList.get(i).getTitle()));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragmentRSSFeedList.get(position);
    }

    @Override
    public int getCount() {
        return this.fragmentRSSFeedList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.fragmentRSSFeedList.get(position).getRssFeed();
    }
}
