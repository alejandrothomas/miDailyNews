package ar.com.thomas.mydailynews.view.NewsFlow;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageView imageViewImageUrl;
    private TextView textViewNewsTitle;
    private TextView textViewNewsSubtitle;
    private LinearLayout linearLayoutNews;


    private void loadPalette() {
        BitmapDrawable drawable = (BitmapDrawable) imageViewImageUrl.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Palette.Builder builder = new Palette.Builder(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {

                Palette.Swatch lightMuted = palette.getMutedSwatch();

                if (lightMuted != null) {

                    linearLayoutNews.setBackgroundColor(lightMuted.getRgb());
//                    textViewNewsTitle.setTextColor(lightMuted.getTitleTextColor());
//                    textViewNewsSubtitle.setTextColor(lightMuted.getBodyTextColor());



                }

            }
        });
    }


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

        textViewNewsTitle = (TextView) view.findViewById(R.id.fragmentNewsViewPager_TEXTVIEW_Title);
        textViewNewsSubtitle = (TextView) view.findViewById(R.id.fragmentNewsViewPager_TEXTVIEW_Subtitle);
        imageViewImageUrl = (ImageView) view.findViewById(R.id.fragmentNewsViewPager_IMAGEVIEW_ImageURL);
        linearLayoutNews = (LinearLayout) view.findViewById(R.id.linear_layout_news);

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
        fragmentNewsViewPager.setArguments(arguments);
        return fragmentNewsViewPager;
    }


}
