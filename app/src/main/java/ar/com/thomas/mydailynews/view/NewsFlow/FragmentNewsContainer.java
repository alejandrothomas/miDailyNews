package ar.com.thomas.mydailynews.view.NewsFlow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.view.MainActivity;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentNewsContainer extends Fragment implements FragmentNewsViewPager.BackgroundColorCalls {

    public static final String NEWS_TITLE_ID = "newsTitle";
    public static final String RSS_SOURCE = "rssFeed";
    public static final String POSITION = "position";
    public static final String RSS_FEED = "rssFeed";
    private String selectedNewsRSSFeedID;
    private FragmentNewsViewPagerAdapter fragmentNewsViewPagerAdapter;
    private Integer backgroundColor;
    private Integer position;
    private FragmentManager fragmentManager;
    private FragmentNewsViewPager fragmentNewsViewPager;


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(selectedNewsRSSFeedID);
    }

    @Override
    public void backgroundColor(Integer color){

        fragmentManager = getChildFragmentManager();
        fragmentNewsViewPager = (FragmentNewsViewPager)fragmentNewsViewPagerAdapter.getItem(position);
        if(fragmentNewsViewPager!=null){
            backgroundColor = fragmentNewsViewPager.getBackgroundColor();
            if(((MainActivity)getActivity())!=null){
                ((MainActivity)getActivity()).setDrawerLayoutBackgroundColor(backgroundColor);
            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_container, container, false);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager_container);
        viewPager.setOffscreenPageLimit(0);

        OverScrollDecoratorHelper.setUpOverScroll(viewPager);

        Bundle bundle = getArguments();
        selectedNewsRSSFeedID = bundle.getString(RSS_FEED);

        position = bundle.getInt(POSITION);

        fragmentNewsViewPagerAdapter = new FragmentNewsViewPagerAdapter(getChildFragmentManager(), getContext(), selectedNewsRSSFeedID);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {

                if(fragmentNewsViewPager!=null){
                    backgroundColor = fragmentNewsViewPager.getBackgroundColor();
                    if(((MainActivity)getActivity())!=null){
                        ((MainActivity)getActivity()).setDrawerLayoutBackgroundColor(backgroundColor);
                    }
                }

                NewsController newsController = new NewsController();

                switch (selectedNewsRSSFeedID) {
                    case "Bookmarks":
                        newsController.addHistory(getContext(), fragmentNewsViewPagerAdapter.getBookmarkNews(position));

                        break;
                    case "History":
                        newsController.addHistory(getContext(), fragmentNewsViewPagerAdapter.getHistoryNews(position));

                        break;
                    default:
                        newsController.addHistory(getContext(), fragmentNewsViewPagerAdapter.getNews(position));

                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(fragmentNewsViewPagerAdapter);
        viewPager.setCurrentItem(position);
        return view;
    }

}
