package ar.com.thomas.mydailynews.view.RSSFeedFlow;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.dao.NewsDAO;


/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentRSSFeedContainer extends Fragment{

    public static final String RSSFEED_CATEGORYID = "rssFeedCategory";
    public static final String RSSFEED_TITLE = "rssFeedTitle";
    private String rssFeedCategory;
    private String rssfeedTitle;
    public FloatingActionButton fab;

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(rssfeedTitle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rrsfeed_container, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerMainActivity);

        Bundle bundle = getArguments();
        rssFeedCategory = bundle.getString(RSSFEED_CATEGORYID);
        rssfeedTitle = bundle.getString(RSSFEED_TITLE);

        FragmentRSSFeedViewPagerAdapter fragmentRSSFeedViewPagerAdapter = new FragmentRSSFeedViewPagerAdapter(getChildFragmentManager(),getContext(),rssFeedCategory);
        viewPager.setAdapter(fragmentRSSFeedViewPagerAdapter);

        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) view.findViewById(R.id.fab_pressed);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsDAO newsDAO = new NewsDAO(getActivity());
                newsDAO.addToFavourites(rssfeedTitle);
            }
        });

        return view;
    }



}
