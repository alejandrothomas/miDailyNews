package ar.com.thomas.mydailynews.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import ar.com.thomas.mydailynews.controller.RSSFeedController;
import ar.com.thomas.mydailynews.view.IntroFlow.Intro;
import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.controller.RSSFeedCategoryController;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.model.RSSFeedCategory;
import ar.com.thomas.mydailynews.util.ResultListener;
import ar.com.thomas.mydailynews.view.FavouriteFlow.FragmentFavouriteContainer;
import ar.com.thomas.mydailynews.view.LoginFlow.FragmentLogin;
import ar.com.thomas.mydailynews.view.SavedFlow.FragmentSavedContainer;
import ar.com.thomas.mydailynews.view.NewsFlow.FragmentNewsContainer;
import ar.com.thomas.mydailynews.view.RSSFeedFlow.FragmentRSSFeedContainer;
import ar.com.thomas.mydailynews.view.RSSFeedFlow.FragmentRSSFeedViewPager;

import com.facebook.FacebookSdk;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

/**
 * Created by alejandrothomas on 6/25/16.
 */

public class MainActivity extends AppCompatActivity implements FragmentRSSFeedViewPager.FragmentCalls {

    private static final String TWITTER_KEY = "n17S5rk1q3FkyeAQGX3xXtchD";
    private static final String TWITTER_SECRET = "n5snTcbuv3DHDLVCmbe5NXXgTsFW2tr91qiNGa1uEKSXYXa9dn";
    protected Context context;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private DrawerLayout drawerLayout;
    private List<RSSFeedCategory> rssFeedCategoryList;
    private List<RSSFeed> rssFeedList;
    private NavigationView navigationView;
    private List<String> favouriteListMainActivity;
    private FragmentFavouriteContainer fragmentFavouriteContainer;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private String currentRSSFeed;
    private Toolbar toolbar;
    private Window window;
    private Button bookmarksButton = null;
    private Button historyButton = null;
    private Button favouritesButton = null;
    private Button loginButton = null;
    private NewsController newsController;
    private Menu menu;
    private AppBarLayout appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        context = this;

        Intro intro = new Intro();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.coordinatorLayout, intro, "intro").commit();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        window = getWindow();
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        fab = (FloatingActionButton) findViewById(R.id.fab_pressed);
        historyButton = (Button) findViewById(R.id.history_button);
        favouritesButton = (Button) findViewById(R.id.favourites_button);
        bookmarksButton = (Button) findViewById(R.id.bookmarked_button);
        loginButton = (Button) findViewById(R.id.login_button);
        appBar = (AppBarLayout) findViewById(R.id.appbar);


        fab.setVisibility(View.GONE);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        favouriteListMainActivity = new ArrayList<>();
        newsController = new NewsController();
        final ListenerMenu listenerMenu = new ListenerMenu();
        rssFeedCategoryList = new ArrayList<>();
        rssFeedList = new ArrayList<>();

        resetColors();
        newsController.clearNewsDB(context);

        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        drawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        int[][] state = new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled},
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_pressed}
        };

        int[] color = new int[]{
                0xFFE5E5E5,
                0xFFE5E5E5,
                0xFFE5E5E5,
                0xFFE5E5E5
        };

        ColorStateList csl = new ColorStateList(state, color);

        navigationView.setNavigationItemSelectedListener(new ListenerMenu());

        menu = navigationView.getMenu();
        navigationView.setBackgroundColor(0xE1000000);
        navigationView.setItemTextColor(csl);

        List<RSSFeed> rssFavouriteList = newsController.getFavouritesFromDB(context);
        for (RSSFeed rssFeed : rssFavouriteList) {
            favouriteListMainActivity.add(rssFeed.getTitle());
        }

        RSSFeedCategoryController rssFeedCategoryController = new RSSFeedCategoryController();
        rssFeedCategoryController.getRSSFeedCategoryList(new ResultListener<List<RSSFeedCategory>>() {
            @Override
            public void finish(List<RSSFeedCategory> result) {

                rssFeedCategoryList.addAll(result);

                RSSFeedController rssFeedController = new RSSFeedController();
                rssFeedController.getRSSFeedList(new ResultListener<List<RSSFeed>>() {
                    @Override
                    public void finish(List<RSSFeed> result) {

                        rssFeedList.addAll(result);

                        populateNavigationDrawerMenu(rssFeedCategoryList);

                        if (favouriteListMainActivity.size() < 1) {
                            newsController.updateFavourites(favouriteListMainActivity, context);

                            if (navigationView.getMenu().getItem(0) != null) {
                                listenerMenu.onNavigationItemSelected(navigationView.getMenu().getItem(0));
                                navigationView.getMenu().getItem(0).setChecked(true);
                            }
                        } else {
                            if (favouritesButton != null) {
                                favouritesButton.performClick();
                            }
                        }
                        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("intro")).setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out).commit();
                        setWindowStatusBarColor(0xFF212121);
                        setSnackbar(getString(R.string.welcome));
                        fab.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentLogin fragmentLogin = new FragmentLogin();

                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, fragmentLogin, "login").addToBackStack(null).commit();
                    appBar.setExpanded(true);
                }
            });
        }

        if (bookmarksButton != null) {
            bookmarksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (newsController.getBookmarkNewsList(context).size() < 1) {
                        setSnackbar(getString(R.string.snack_bookmarks_empty));
                    } else {
                        bookmarksButton.setSelected(true);
                        if (favouritesButton != null) {
                            favouritesButton.setSelected(false);
                        }
                        FragmentSavedContainer fragmentSavedContainer = new FragmentSavedContainer();
                        Bundle arguments = new Bundle();

                        arguments.putString(FragmentSavedContainer.SECTION, getString(R.string.bookmarks));
                        fragmentSavedContainer.setArguments(arguments);

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
                                .replace(R.id.fragment_container, fragmentSavedContainer, "bookmarks")
                                .addToBackStack(null)
                                .commit();

                        appBar.setExpanded(true);
                        bookmarksButton.setEnabled(false);
                    }
                }
            });
        }

        if (historyButton != null) {
            historyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (newsController.getHistoryNewsList(context).size() < 1) {
                        setSnackbar(getString(R.string.snack_historial_empty));
                    } else {
                        if (bookmarksButton != null && favouritesButton != null) {
                            bookmarksButton.setSelected(false);
                            favouritesButton.setSelected(false);
                        }

                        FragmentSavedContainer fragmentSavedContainer = new FragmentSavedContainer();
                        Bundle arguments = new Bundle();

                        arguments.putString(FragmentSavedContainer.SECTION, getString(R.string.history));
                        fragmentSavedContainer.setArguments(arguments);

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
                                .replace(R.id.fragment_container, fragmentSavedContainer, "history")
                                .addToBackStack(null)
                                .commit();

                        appBar.setExpanded(true);
                        historyButton.setEnabled(false);
                    }
                }
            });
        }

        if (favouritesButton != null) {
            favouritesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsController newsController = new NewsController();
                    newsController.updateFavourites(favouriteListMainActivity, context);
                    List<RSSFeed> newFavouriteList = newsController.getFavouritesFromDB(context);

                    if (newFavouriteList.size() > 0) {
                        favouritesButton.setSelected(true);
                        favouritesButton.setEnabled(false);
                        if (bookmarksButton != null) {
                            bookmarksButton.setSelected(false);
                        }
                        fragmentFavouriteContainer = new FragmentFavouriteContainer();
                        fragmentManager = getSupportFragmentManager();

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
                                .replace(R.id.fragment_container, fragmentFavouriteContainer, "favourites")
                                .addToBackStack(null)
                                .commit();

                        fragmentFavouriteContainer.setRssFeedList(newFavouriteList);

                    } else {

                        setSnackbar(getString(R.string.snack_favourites_empty));

                    }
                }
            });

            appBar.setExpanded(true);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favouriteListMainActivity.contains(currentRSSFeed)) {
                    favouriteListMainActivity.remove(currentRSSFeed);
                    fab.setSelected(false);
                    setSnackbar(currentRSSFeed + getString(R.string.snack_favourites_remove));
                } else {
                    favouriteListMainActivity.add(currentRSSFeed);
                    setSnackbar(currentRSSFeed + getString(R.string.snack_favourites_add));
                    fab.setSelected(true);
                }
            }
        });
    }

    @Override
    public void getNotifications(String newsClickedID, Integer itemPosition, String rssFeedID) {

        FragmentNewsContainer fragmentNewsContainer = new FragmentNewsContainer();

        Bundle arguments = new Bundle();

        arguments.putString(FragmentNewsContainer.NEWS_TITLE_ID, newsClickedID);
        arguments.putInt(FragmentNewsContainer.POSITION, itemPosition);
        arguments.putString(FragmentNewsContainer.RSS_SOURCE, rssFeedID);

        fragmentNewsContainer.setArguments(arguments);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.fragment_container, fragmentNewsContainer)
                .addToBackStack(null)
                .commit();

        appBar.setExpanded(true);

        resetColors();
    }

    public void populateNavigationDrawerMenu(List<RSSFeedCategory> rssFeedCategoryList) {


        if (rssFeedCategoryList != null) {
            for (Integer i = 0; i < rssFeedCategoryList.size(); i++) {
                menu.add(R.id.navigation_drawer_menu_RSSFeedCategories, i, i, rssFeedCategoryList.get(i).getCategoryName());
                menu.setGroupCheckable(R.id.navigation_drawer_menu_RSSFeedCategories, true, true);
                menu.setGroupVisible(R.id.navigation_drawer_menu_RSSFeedCategories, true);
            }
        }
    }

    public void setCurrentRSSFeed(String rssFeed) {

        this.currentRSSFeed = rssFeed;
        if (favouriteListMainActivity.contains(currentRSSFeed)) {
            fab.setSelected(true);
        } else {
            fab.setSelected(false);
        }
    }

    private class ListenerMenu implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {

            RSSFeedCategory rssFeedCategory = rssFeedCategoryList.get(item.getItemId());

            FragmentRSSFeedContainer fragmentRSSFeedContainer = new FragmentRSSFeedContainer();
            Bundle arguments = new Bundle();

            arguments.putString(FragmentRSSFeedContainer.RSSFEED_CATEGORYID, rssFeedCategory.getObjectId());
            arguments.putString(FragmentRSSFeedContainer.RSSFEED_TITLE, rssFeedCategory.getCategoryName());

            fragmentRSSFeedContainer.setArguments(arguments);


            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
                    .replace(R.id.fragment_container, fragmentRSSFeedContainer, "rss_feed")
                    .commit();

            fragmentRSSFeedContainer.setFavouriteList(favouriteListMainActivity);

            if (drawerLayout != null) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }

            if (bookmarksButton != null && favouritesButton != null) {
                bookmarksButton.setSelected(false);
                favouritesButton.setSelected(false);
            }

            resetColors();

            setTitle(rssFeedCategory.getCategoryName());
            appBar.setExpanded(true);

            return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        NewsController newsController = new NewsController();
        newsController.updateFavourites(favouriteListMainActivity, this);
    }

    public void setWindowStatusBarColor(Integer color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(color);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resetColors();
        setToolbarVisibility(true);
    }

    public void setFabVisibility(Boolean trueOrFalse) {

        if (trueOrFalse != null) {
            if (trueOrFalse) {
                fab.setVisibility(View.VISIBLE);
            } else {
                fab.setVisibility(View.GONE);
            }
        }
    }

    public void setToolbarVisibility(Boolean state) {

        if (state != null) {
            if (state) {
                toolbar.setVisibility(View.VISIBLE);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        }
    }

    public void resetColors() {

        if (drawerLayout != null && toolbar != null && window != null) {

            drawerLayout.setBackgroundColor(0xFF212121);
            toolbar.setBackgroundColor(0xFF212121);
            setWindowStatusBarColor(0xFF212121);
        }
    }

    public void setSnackbar(String snackbarMessage) {

        if (coordinatorLayout != null) {

            Snackbar snackbar = Snackbar.make(coordinatorLayout, snackbarMessage, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(0xE7000000);
            snackbar.show();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setLoginButtonColor(Integer socialMedia) {

        loginButton.setBackgroundDrawable(getDrawable(socialMedia));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fragment = getSupportFragmentManager();

        if (fragment.findFragmentByTag("login") != null) {

            fragment.findFragmentByTag("login").onActivityResult(requestCode, resultCode, data);

        } else Log.d("Twitter", "fragment is null");
    }

    public List<RSSFeed> getRssFeedList() {

        return rssFeedList;
    }

    public void setAppBarStatus(Boolean state) {

        if (appBar != null) {

            appBar.setExpanded(true);
        }
    }

    public void setAppBarButtonsStatus(Boolean state, String fromFavs) {

        if (bookmarksButton != null && favouritesButton != null && historyButton != null) {

            bookmarksButton.setEnabled(state);
            bookmarksButton.setSelected(!state);
            historyButton.setEnabled(state);
            historyButton.setSelected(!state);

            if(fromFavs.equals("fromFavs")){
                favouritesButton.setEnabled(!state);
                favouritesButton.setSelected(state);
            }else{
                favouritesButton.setEnabled(state);
                favouritesButton.setSelected(!state);
            }
        }
    }
}
