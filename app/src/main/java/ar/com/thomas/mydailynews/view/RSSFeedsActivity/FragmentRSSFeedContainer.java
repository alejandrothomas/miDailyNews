package ar.com.thomas.mydailynews.view.RSSFeedsActivity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.thomas.mydailynews.R;


/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentRSSFeedContainer extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rrsfeed_container,container,false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerMainActivity);
        FragmentRSSFeedViewPagerAdapter fragmentRSSFeedViewPagerAdapter = new FragmentRSSFeedViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(fragmentRSSFeedViewPagerAdapter);

        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
