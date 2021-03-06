package ar.com.thomas.mydailynews.view.NewsFlow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import java.util.ArrayList;
import java.util.List;
import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.view.MainActivity;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentNewsViewPager extends Fragment {

    public static final String NEWS_TITLE = "newsTitle";
    public static final String NEWS_DESCRIPTION = "newsDescription";
    public static final String NEWS_ENCODED = "newsEncoded";
    public static final String NEWS_CONTENT = "newsContent";
    public static final String NEWS_IMAGE_URL = "newsImageUrl";
    public static final String RSS_FEED = "rssFeed";
    public static final String ID = "id";
    public static final String LINK = "link";
    private ImageView imageViewImageUrl;
    private TextView textViewNewsTitle;
    private TextView textViewNewsSubtitle;
    private TextView textViewNewsContent;
    private Integer backgroundColor;
    private Context context;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout fab;
    private NewsController newsController;
    private List<News> bookmarkedNewsList;
    private News news;
    private View imageView;
    private ShareButton shareButtonFacebook;
    private LinearLayout shareButtonTwitter;
    private Uri mUri;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_viewpager, container, false);

        context = getActivity();

        Bundle bundle = getArguments();

        final String newsTitle = bundle.getString(NEWS_TITLE);
        String newsDescription = bundle.getString(NEWS_DESCRIPTION);
        String newsContent = bundle.getString(NEWS_CONTENT);
        String newsImageUrl = bundle.getString(NEWS_IMAGE_URL);
        String rssFeed = bundle.getString(RSS_FEED);
        String newsId = bundle.getString(ID);
        String newsLink = bundle.getString(LINK);
        String newsEncoded = bundle.getString(NEWS_ENCODED);

        news = new News();

        news.setNewsID(newsId);
        news.setTitle(newsTitle);
        news.setDescription(newsDescription);
        news.setEncoded(newsEncoded);
        news.setContent(newsContent);
        news.setImageUrl(newsImageUrl);
        news.setRssFeed(rssFeed);
        news.setLink(newsLink);

        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(rssFeed);
        collapsingToolbarLayout.setExpandedTitleColor(0x000000);

        textViewNewsTitle = (TextView) view.findViewById(R.id.fragmentNewsViewPager_TEXTVIEW_Title);
        textViewNewsSubtitle = (TextView) view.findViewById(R.id.fragmentNewsViewPager_TEXTVIEW_Subtitle);
        textViewNewsContent = (TextView) view.findViewById(R.id.fragmentNewsViewPager_TEXTVIEW_Description);
        imageViewImageUrl = (ImageView) view.findViewById(R.id.fragmentNewsViewPager_IMAGEVIEW_ImageURL);
        imageView = (ImageView) view.findViewById(R.id.image_view);
        shareButtonFacebook = (ShareButton) view.findViewById(R.id.facebook_share_button);
        shareButtonTwitter = (LinearLayout) view.findViewById(R.id.twitter_share_button);
        fab = (LinearLayout) view.findViewById(R.id.fab_news_viewpager);


        shareButtonTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweet(view);
            }
        });

        mUri = Uri.parse(news.getLink());

        ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(mUri).build();

        shareButtonFacebook.setShareContent(content);

        bookmarkedNewsList = new ArrayList<>();
        newsController = new NewsController();
        bookmarkedNewsList = newsController.getBookmarkNewsList(context);

        if (bookmarkedNewsList.contains(news)) {
            fab.setSelected(true);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bookmarkedNewsList.contains(news)) {
                    ((MainActivity) context).setSnackbar(news.getTitle() + context.getResources().getString(R.string.snack_bookmarks_remove));
                    fab.setSelected(false);
                    newsController.removeBookmark(context, news);
                    bookmarkedNewsList.remove(news);
                } else {
                    ((MainActivity) context).setSnackbar(news.getTitle() + context.getResources().getString(R.string.snack_bookmarks_add));
                    fab.setSelected(true);
                    newsController.addBookmark(context, news);
                    bookmarkedNewsList.add(news);
                }
            }
        });

        textViewNewsTitle.setText(newsTitle);
        if (newsDescription != null) {
            textViewNewsSubtitle.setText(Html.fromHtml(newsDescription.replaceAll("(<(/)img>)|(<img.+?>)", "")));
            textViewNewsSubtitle.setMovementMethod(LinkMovementMethod.getInstance());

        }
        if (newsContent != null) {
            textViewNewsContent.setText(Html.fromHtml(newsContent.replaceAll("(<(/)img>)|(<img.+?>)", "")));
            textViewNewsContent.setMovementMethod(LinkMovementMethod.getInstance());
        }

        if (newsImageUrl == null || newsImageUrl.isEmpty()) {
            imageViewImageUrl.setImageResource(R.drawable.placeholder_unavailable_image);
        } else {
            Picasso.with(getActivity()).load(newsImageUrl).resize(0, 200).into(imageViewImageUrl, new Callback() {
                @Override
                public void onSuccess() {
                    loadPalette();
                }

                @Override
                public void onError() {

                }
            });
        }

        return view;
    }

    public FragmentNewsViewPager generateFragment(News news) {

        FragmentNewsViewPager fragmentNewsViewPager = new FragmentNewsViewPager();

        Bundle arguments = new Bundle();

        arguments.putString(NEWS_TITLE, news.getTitle());
        arguments.putString(NEWS_DESCRIPTION, news.getDescription());
        arguments.putString(NEWS_CONTENT, news.getContent());
        arguments.putString(NEWS_IMAGE_URL, news.getImageUrl());
        arguments.putString(RSS_FEED, news.getRssFeed());
        arguments.putString(ID, news.getNewsID());
        arguments.putString(LINK, news.getLink());
        fragmentNewsViewPager.setArguments(arguments);
        return fragmentNewsViewPager;
    }

    private void loadPalette() {
        BitmapDrawable drawable = (BitmapDrawable) imageViewImageUrl.getDrawable();
        if(drawable!=null){
            Bitmap bitmap = drawable.getBitmap();
            Palette.Builder builder = new Palette.Builder(bitmap);
            builder.generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {

                    Palette.Swatch lightMuted = palette.getMutedSwatch();

                    if (lightMuted != null) {

                        backgroundColor = lightMuted.getRgb();
                        imageView.setBackgroundColor(backgroundColor);

                    }
                }
            });
        }
    }

    public void tweet(View view) {

        TweetComposer.Builder composerActivityBuilder = new TweetComposer.Builder(getActivity()).text(news.getTitle() + " - " + news.getLink() + " #DailyNEWS");
        composerActivityBuilder.show();
    }
}