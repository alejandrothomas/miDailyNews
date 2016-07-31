package ar.com.thomas.mydailynews.view.RSSFeedFlow;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.controller.RSSFeedController;
import ar.com.thomas.mydailynews.dao.NewsDAO;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.util.ResultListener;
import ar.com.thomas.mydailynews.view.MainActivity;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentRSSFeedContainer extends Fragment {

    public static final String RSSFEED_CATEGORYID = "rssFeedCategory";
    public static final String RSSFEED_TITLE = "rssFeedTitle";
    private String rssFeedCategoryID;
    private String rssfeedCategoryTitle;
    private FloatingActionButton fab;
    private List<String> favouriteList;
    private Integer currentPosition;
    private String rssFeed;
    private List<RSSFeed> rssFeedList;

    public void setRssFeedList(List<RSSFeed> rssFeedList) {
        this.rssFeedList = rssFeedList;
    }

    public void setFavouriteList(List<String> favouriteList) {
        this.favouriteList = favouriteList;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rrsfeed_container, container, false);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerMainActivity);

        OverScrollDecoratorHelper.setUpOverScroll(viewPager);

        Bundle bundle = getArguments();
        rssFeedCategoryID = bundle.getString(RSSFEED_CATEGORYID);
        rssfeedCategoryTitle = bundle.getString(RSSFEED_TITLE);

        rssFeedList = new ArrayList<>();
        rssFeedList=((MainActivity)getContext()).getRssFeedList();


        final FragmentRSSFeedViewPagerAdapter fragmentRSSFeedViewPagerAdapter = new FragmentRSSFeedViewPagerAdapter(getChildFragmentManager(), getContext(), rssFeedCategoryID, rssFeedList);
        viewPager.setAdapter(fragmentRSSFeedViewPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        currentPosition = viewPager.getCurrentItem();
        rssFeed = fragmentRSSFeedViewPagerAdapter.getPageTitle(currentPosition).toString();

        ((MainActivity) getContext()).setCurrentRSSFeed(rssFeed);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = viewPager.getCurrentItem();
                rssFeed = fragmentRSSFeedViewPagerAdapter.getPageTitle(currentPosition).toString();
                ((MainActivity) getContext()).setCurrentRSSFeed(rssFeed);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

}
