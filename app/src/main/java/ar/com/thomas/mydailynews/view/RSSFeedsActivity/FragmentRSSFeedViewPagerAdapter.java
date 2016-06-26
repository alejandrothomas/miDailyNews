package ar.com.thomas.mydailynews.view.RSSFeedsActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentRSSFeedViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<FragmentRSSFeedViewPager> fragmentRSSFeedList;


    public FragmentRSSFeedViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentRSSFeedList = new ArrayList<>();
        this.fragmentRSSFeedList.add(FragmentRSSFeedViewPager.generateFragment("Clarín"));
        this.fragmentRSSFeedList.add(FragmentRSSFeedViewPager.generateFragment("La Nación"));
        this.fragmentRSSFeedList.add(FragmentRSSFeedViewPager.generateFragment("La Razón"));
        this.fragmentRSSFeedList.add(FragmentRSSFeedViewPager.generateFragment("Olé"));
        this.fragmentRSSFeedList.add(FragmentRSSFeedViewPager.generateFragment("Infobae"));
        this.fragmentRSSFeedList.add(FragmentRSSFeedViewPager.generateFragment("New York Times"));
        this.fragmentRSSFeedList.add(FragmentRSSFeedViewPager.generateFragment("Página 12"));
        this.fragmentRSSFeedList.add(FragmentRSSFeedViewPager.generateFragment("AS"));
        this.fragmentRSSFeedList.add(FragmentRSSFeedViewPager.generateFragment("Marca"));
        this.fragmentRSSFeedList.add(FragmentRSSFeedViewPager.generateFragment("L.A. Times"));
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
