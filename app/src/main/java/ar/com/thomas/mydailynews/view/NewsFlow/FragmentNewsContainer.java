package ar.com.thomas.mydailynews.view.NewsFlow;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.view.MainActivity;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentNewsContainer extends Fragment {

    public static final String NEWS_TITLE_ID = "newsTitle";
    public static final String RSS_SOURCE = "rssFeed";
    public static final String POSITION = "position";
    public static final String RSS_FEED = "rssFeed";
    private String selectedNewsRSSFeedID;
    private FragmentNewsViewPagerAdapter fragmentNewsViewPagerAdapter;
    private Integer backgroundColor;
    private Integer position;
    private FragmentNewsViewPager fragmentNewsViewPager;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_container, container, false);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager_container);
        viewPager.setOffscreenPageLimit(6);

        OverScrollDecoratorHelper.setUpOverScroll(viewPager);

        Bundle bundle = getArguments();
        selectedNewsRSSFeedID = bundle.getString(RSS_FEED);

        position = bundle.getInt(POSITION);

        ((MainActivity)getContext()).setToolbarVisibility(false);
        ((MainActivity)getContext()).setFabVisibility(false);

        fragmentNewsViewPagerAdapter = new FragmentNewsViewPagerAdapter(getChildFragmentManager(), getContext(), selectedNewsRSSFeedID);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                fragmentNewsViewPager = (FragmentNewsViewPager) fragmentNewsViewPagerAdapter.getItem(position);
                if (fragmentNewsViewPager != null) {
                    backgroundColor = fragmentNewsViewPager.getBackgroundColor();

                    if (((MainActivity) getActivity()) != null) {
                        ((MainActivity) getActivity()).setDrawerLayoutBackgroundColor(backgroundColor);
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
        viewPager.setPageTransformer(true, new CustomPageTransformer());

        return view;
    }

    public class CustomPageTransformer implements ViewPager.PageTransformer {
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            View imageView = view.findViewById(R.id.image_view);
            View contentView = view.findViewById(R.id.content_area);

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left
            } else if (position <= 0) { // [-1,0]
                // This page is moving out to the left

                // Counteract the default swipe
                view.setTranslationX(pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    contentView.setTranslationX(pageWidth * position);
                }
                if (imageView != null) {
                    // Fade the image in
                    imageView.setAlpha(1 + position);
                }

            } else if (position <= 1) { // (0,1]
                // This page is moving in from the right

                // Counteract the default swipe
                view.setTranslationX(pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    contentView.setTranslationX(pageWidth * position);
                }
                if (imageView != null) {
                    // Fade the image out
                    imageView.setAlpha(1 - position);
                }
            } else { // (1,+Infinity]
                // This page is way off-screen to the right
            }
        }
    }

}
