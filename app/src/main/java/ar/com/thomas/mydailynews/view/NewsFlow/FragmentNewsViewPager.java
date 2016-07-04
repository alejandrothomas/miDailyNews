package ar.com.thomas.mydailynews.view.NewsFlow;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.model.News;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentNewsViewPager extends Fragment {

    public static final String NEWS_TITLE = "newsTitle";
    public static final String NEWS_DESCRIPTION = "newsDescription";
    public static final String NEWS_IMAGE_URL = "newsImageUrl";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_viewpager, container, false);

        ScrollView scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);

        Bundle bundle = getArguments();

        String newsTitle = bundle.getString(NEWS_TITLE);
        String newsDescription = bundle.getString(NEWS_DESCRIPTION);
        String newsImageUrl = bundle.getString(NEWS_IMAGE_URL);

        TextView textViewNewsTitle = (TextView)view.findViewById(R.id.fragmentNewsViewPager_TEXTVIEW_Title);
        TextView textViewNewsSubtitle = (TextView)view.findViewById(R.id.fragmentNewsViewPager_TEXTVIEW_Subtitle);
        ImageView imageViewImageUrl = (ImageView)view.findViewById(R.id.fragmentNewsViewPager_IMAGEVIEW_ImageURL);

        textViewNewsTitle.setText(newsTitle);
        textViewNewsSubtitle.setText(newsDescription);
        Picasso.with(getActivity()).load(newsImageUrl).placeholder(R.drawable.placeholder_unavailable_image).into(imageViewImageUrl);

        return view;
    }

    public FragmentNewsViewPager generateFragment (News news){

        FragmentNewsViewPager fragmentNewsViewPager = new FragmentNewsViewPager();

        Bundle arguments = new Bundle();

        arguments.putString(NEWS_TITLE, news.getTitle());
        arguments.putString(NEWS_DESCRIPTION, news.getDescription());
        arguments.putString(NEWS_IMAGE_URL, news.getImageUrl());
        fragmentNewsViewPager.setArguments(arguments);
        return fragmentNewsViewPager;
    }


}
