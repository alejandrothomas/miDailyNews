package ar.com.thomas.mydailynews.view.NewsActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.model.News;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentNewsViewPager extends Fragment {

    public static final String NEWS_TITLE = "newsTitle";
    public static final String NEWS_SUBTITLE = "newsSubtitle";
    public static final String RSS_SOURCE = "rssFeed";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_viewpager, container, false);

        Bundle bundle = getArguments();

        String newsTitle = bundle.getString(NEWS_TITLE);
        String newsSubtitle = bundle.getString(NEWS_SUBTITLE);
        String rssSource = bundle.getString(RSS_SOURCE);

        TextView textViewNewsTitle = (TextView)view.findViewById(R.id.fragmentNewsViewPager_TEXTVIEW_Title);
        TextView textViewNewsSubtitle = (TextView)view.findViewById(R.id.fragmentNewsViewPager_TEXTVIEW_Subtitle);
        textViewNewsTitle.setText(newsTitle);
        textViewNewsSubtitle.setText(newsSubtitle);

        return view;
    }

    public static FragmentNewsViewPager generateFragment (News news){

        FragmentNewsViewPager fragmentNewsViewPager = new FragmentNewsViewPager();

        Bundle arguments = new Bundle();

        arguments.putString(NEWS_TITLE, news.getTitle());
//        arguments.putString(NEWS_SUBTITLE,news.getSubtitle());
//        arguments.putString(RSS_SOURCE,news.getRSSFeed());
        fragmentNewsViewPager.setArguments(arguments);
        return fragmentNewsViewPager;
    }
}
