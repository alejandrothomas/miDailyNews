package ar.com.thomas.mydailynews.view.RSSFeedFlow;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.model.RSSFeed;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentRSSFeedViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<FragmentRSSFeedViewPager> fragmentRSSFeedList;
    private List<RSSFeed> rssFeedList;


    public FragmentRSSFeedViewPagerAdapter(FragmentManager fm, Context context, final String rssFeedCategoryID, List<RSSFeed> feedList) {
        super(fm);

        rssFeedList = new ArrayList<>();
        this.rssFeedList = feedList;
        this.fragmentRSSFeedList = new ArrayList<>();


        for (Integer i = 0; i < feedList.size(); i++) {
            if (feedList.get(i).getCategory().getObjectId().equals(rssFeedCategoryID)) {
                this.fragmentRSSFeedList.add(FragmentRSSFeedViewPager.generateFragment(feedList.get(i)));
            }
        }
    }


    public FragmentRSSFeedViewPagerAdapter(FragmentManager fm, Context context, List<RSSFeed> favouriteList) {
        super(fm);

        this.fragmentRSSFeedList = new ArrayList<>();

        for (RSSFeed favourite : favouriteList) {
            this.fragmentRSSFeedList.add(FragmentRSSFeedViewPager.generateFragment(favourite));
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