package ar.com.thomas.mydailynews.view.FavouriteFlow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.view.MainActivity;
import ar.com.thomas.mydailynews.view.RSSFeedFlow.FragmentRSSFeedViewPagerAdapter;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class FragmentFavouriteContainer extends Fragment {

    private List<RSSFeed> rssFeedList = new ArrayList<>();

    public void setRssFeedList(List<RSSFeed> rssFeedList) {
        this.rssFeedList = rssFeedList;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.favourites));
        ((MainActivity) getContext()).setAppBarButtonsStatus(true,"fromFavs");
        ((MainActivity) getContext()).setFabVisibility(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites_container, container, false);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager_favourites);
        OverScrollDecoratorHelper.setUpOverScroll(viewPager);


        final FragmentRSSFeedViewPagerAdapter fragmentRSSFeedViewPagerAdapter = new FragmentRSSFeedViewPagerAdapter(getChildFragmentManager(), getContext(), rssFeedList);
        viewPager.setAdapter(fragmentRSSFeedViewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs_favourite);
        tabLayout.setupWithViewPager(viewPager);
        ((MainActivity) getContext()).setCurrentRSSFeed(fragmentRSSFeedViewPagerAdapter.getPageTitle(0).toString());

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                ((MainActivity) getContext()).setCurrentRSSFeed(fragmentRSSFeedViewPagerAdapter.getPageTitle(position).toString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

}
