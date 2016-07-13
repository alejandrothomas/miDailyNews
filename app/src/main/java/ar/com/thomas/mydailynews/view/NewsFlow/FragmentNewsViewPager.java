package ar.com.thomas.mydailynews.view.NewsFlow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.view.MainActivity;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentNewsViewPager extends Fragment {

    public static final String NEWS_TITLE = "newsTitle";
    public static final String NEWS_DESCRIPTION = "newsDescription";
    public static final String NEWS_IMAGE_URL = "newsImageUrl";
    public static final String RSS_FEED = "rssFeed";
    private ImageView imageViewImageUrl;
    private TextView textViewNewsTitle;
    private TextView textViewNewsSubtitle;
    private Integer backgroundColor;
    private Context context;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_viewpager, container, false);

        Bundle bundle = getArguments();

        String newsTitle = bundle.getString(NEWS_TITLE);
        String newsDescription = bundle.getString(NEWS_DESCRIPTION);
        String newsImageUrl = bundle.getString(NEWS_IMAGE_URL);
        String rssFeed = bundle.getString(RSS_FEED);

//        final Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar_news);
//        ((MainActivity)getContext()).setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(rssFeed);

        textViewNewsTitle = (TextView) view.findViewById(R.id.fragmentNewsViewPager_TEXTVIEW_Title);
        textViewNewsSubtitle = (TextView) view.findViewById(R.id.fragmentNewsViewPager_TEXTVIEW_Subtitle);
        imageViewImageUrl = (ImageView) view.findViewById(R.id.fragmentNewsViewPager_IMAGEVIEW_ImageURL);

        textViewNewsTitle.setText(newsTitle);
        textViewNewsSubtitle.setText(newsDescription);
        Picasso.with(getActivity()).load(newsImageUrl).placeholder(R.drawable.placeholder_unavailable_image).resize(0,300).into(imageViewImageUrl, new Callback() {
            @Override
            public void onSuccess() {
                loadPalette();
            }

            @Override
            public void onError() {

            }
        });

        return view;
    }

    public FragmentNewsViewPager generateFragment(News news) {

        FragmentNewsViewPager fragmentNewsViewPager = new FragmentNewsViewPager();

        Bundle arguments = new Bundle();

        arguments.putString(NEWS_TITLE, news.getTitle());
        arguments.putString(NEWS_DESCRIPTION, news.getDescription());
        arguments.putString(NEWS_IMAGE_URL, news.getImageUrl());
        arguments.putString(RSS_FEED,news.getRssFeed());
        fragmentNewsViewPager.setArguments(arguments);
        return fragmentNewsViewPager;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    private void loadPalette() {
        BitmapDrawable drawable = (BitmapDrawable) imageViewImageUrl.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Palette.Builder builder = new Palette.Builder(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {

                Palette.Swatch lightMuted = palette.getMutedSwatch();

                if (lightMuted != null) {
                    backgroundColor = lightMuted.getRgb();
                }
            }
        });
    }


}
