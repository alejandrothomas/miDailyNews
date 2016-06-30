package ar.com.thomas.mydailynews.view.NewsFlow;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.model.News;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentNewsViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<FragmentNewsViewPager> fragmentNewsList = new ArrayList<>();
    Context context;




    public FragmentNewsViewPagerAdapter(FragmentManager fm, Context context, String rssFeed) {
        super(fm);
        NewsController newsController = new NewsController();

        List<News> newsList = newsController.getNewsListFromDB(rssFeed,context);

        for (News news : newsList){

            fragmentNewsList.add(new FragmentNewsViewPager().generateFragment(news));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentNewsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentNewsList.size();
    }


}
