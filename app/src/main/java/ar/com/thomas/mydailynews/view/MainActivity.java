package ar.com.thomas.mydailynews.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;
import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.dao.RSSFeedCategoryDAO;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.model.RSSFeedCategory;
import ar.com.thomas.mydailynews.view.FavouriteFlow.FragmentFavouriteContainer;
import ar.com.thomas.mydailynews.view.SavedFlow.FragmentSavedContainer;
import ar.com.thomas.mydailynews.view.NewsFlow.FragmentNewsContainer;
import ar.com.thomas.mydailynews.view.RSSFeedFlow.FragmentRSSFeedContainer;
import ar.com.thomas.mydailynews.view.RSSFeedFlow.FragmentRSSFeedViewPager;

public class MainActivity extends AppCompatActivity implements FragmentRSSFeedViewPager.FragmentCalls{

    protected Context context;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private DrawerLayout drawerLayout;
    private List<RSSFeedCategory> rssFeedCategoryList;
    private NavigationView navigationView;
    private List<String> favouriteListMainActivity;
    private FragmentRSSFeedContainer fragmentRSSFeedContainer;
    private FragmentFavouriteContainer fragmentFavouriteContainer;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private String currentRSSFeed;
    private Toolbar toolbar;
    private Window window;
    private int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    private Button bookmarks = null;
    private Button history = null;
    private Button favourites = null;
    private NewsController newsController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        context = this;

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        window = getWindow();
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.navigationView);
        fab = (FloatingActionButton) findViewById(R.id.fab_pressed);
        history = (Button) findViewById(R.id.history_button);
        favourites = (Button)findViewById(R.id.favourites_button);
        bookmarks = (Button) findViewById(R.id.bookmarked_button);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        favouriteListMainActivity = new ArrayList<>();
        newsController = new NewsController();


        setSnackbar(getString(R.string.welcome));
        resetColors();
        newsController.clearNewsDB(context);



        List<RSSFeed> rssFeedList = newsController.getFavouritesFromDB(context);
        for (RSSFeed rssFeed:rssFeedList){
            favouriteListMainActivity.add(rssFeed.getTitle());
        }

        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        drawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new ListenerMenu());
        populateNavigationDrawerMenu();

        newsController.updateFavourites(favouriteListMainActivity,context);

        if(favouriteListMainActivity.size()<1) {
            ListenerMenu listenerMenu = new ListenerMenu();
            listenerMenu.onNavigationItemSelected(navigationView.getMenu().getItem(1));
            navigationView.getMenu().getItem(1).setChecked(true);
        }else{
            if (favourites != null) {
                favourites.performClick();
            }
        }

        if (bookmarks != null) {
            bookmarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(newsController.getBookmarkNewsList(context).size()<1){
                        setSnackbar(getString(R.string.snack_bookmarks_empty));
                    }else{
                        bookmarks.setSelected(true);
                        if(favourites!=null){
                            favourites.setSelected(false);
                        }
                        FragmentSavedContainer fragmentSavedContainer = new FragmentSavedContainer();
                        Bundle arguments = new Bundle();
                        arguments.putString(FragmentSavedContainer.SECTION,getString(R.string.bookmarks));
                        fragmentSavedContainer.setArguments(arguments);

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in,R.anim.slide_out,R.anim.slide_in,R.anim.slide_out);
                        fragmentTransaction.replace(R.id.fragment_container, fragmentSavedContainer).addToBackStack(null).commit();
                    }
                }
            });
        }

        if (history != null) {
            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(newsController.getHistoryNewsList(context).size()<1){
                        setSnackbar(getString(R.string.snack_historial_empty));
                    }else{
                        if (bookmarks != null && favourites!=null) {
                            bookmarks.setSelected(false);
                            favourites.setSelected(false);
                        }
                        FragmentSavedContainer fragmentSavedContainer = new FragmentSavedContainer();
                        Bundle arguments = new Bundle();
                        arguments.putString(FragmentSavedContainer.SECTION,getString(R.string.history));
                        fragmentSavedContainer.setArguments(arguments);

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in,R.anim.slide_out,R.anim.slide_in,R.anim.slide_out);
                        fragmentTransaction.replace(R.id.fragment_container, fragmentSavedContainer).addToBackStack(null).commit();
                    }
                }
            });
        }

        if (favourites != null) {
            favourites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsController newsController = new NewsController();
                    newsController.updateFavourites(favouriteListMainActivity,context);
                    List<RSSFeed> newFavouriteList = newsController.getFavouritesFromDB(context);

                    if(newFavouriteList.size()>0) {
                        favourites.setSelected(true);
                        if(bookmarks!=null){
                            bookmarks.setSelected(false);
                        }
                        fragmentFavouriteContainer = new FragmentFavouriteContainer();
                        fragmentManager = getSupportFragmentManager();

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in,R.anim.slide_out,R.anim.slide_in,R.anim.slide_out);
                        fragmentTransaction.replace(R.id.fragment_container,fragmentFavouriteContainer,"favourites");
                        fragmentTransaction.addToBackStack(null).commit();
                        fragmentFavouriteContainer.setRssFeedList(newFavouriteList);
                    }else{
                        setSnackbar(getString(R.string.snack_favourites_empty));
                    }
                }
            });
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favouriteListMainActivity.contains(currentRSSFeed)){
                    favouriteListMainActivity.remove(currentRSSFeed);
                    fab.setSelected(false);
                    setSnackbar(currentRSSFeed+getString(R.string.snack_favourites_remove));
                }else{
                    favouriteListMainActivity.add(currentRSSFeed);
                    setSnackbar(currentRSSFeed+getString(R.string.snack_favourites_add));
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
        fragmentTransaction.setCustomAnimations(R.anim.slide_in,R.anim.slide_out,R.anim.slide_in,R.anim.slide_out);
        fragmentTransaction.replace(R.id.fragment_container,fragmentNewsContainer);
        fragmentTransaction.addToBackStack(null).commit();
        resetColors();
    }

    public void populateNavigationDrawerMenu() {
        Menu menu = navigationView.getMenu();
        RSSFeedCategoryDAO rssFeedCategoryDAO = new RSSFeedCategoryDAO();
        rssFeedCategoryList = rssFeedCategoryDAO.getRSSFeedCategoryList(context);

        for (Integer i = 0; i < rssFeedCategoryList.size(); i++) {
            menu.add(R.id.navigation_drawer_menu_RSSFeedCategories, i, i, rssFeedCategoryList.get(i).getCategoryName());
            menu.setGroupCheckable(R.id.navigation_drawer_menu_RSSFeedCategories, true, true);
            menu.setGroupVisible(R.id.navigation_drawer_menu_RSSFeedCategories, true);
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



    private class ListenerMenu implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            RSSFeedCategory rssFeedCategory = rssFeedCategoryList.get(item.getItemId());

            fragmentRSSFeedContainer = new FragmentRSSFeedContainer();
            Bundle arguments = new Bundle();

            arguments.putString(FragmentRSSFeedContainer.RSSFEED_CATEGORYID, rssFeedCategory.getObjectId());
            arguments.putString(FragmentRSSFeedContainer.RSSFEED_TITLE, rssFeedCategory.getCategoryName());

            fragmentRSSFeedContainer.setArguments(arguments);
            fragmentManager = getSupportFragmentManager();

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in,R.anim.slide_out,R.anim.slide_in,R.anim.slide_out);
            fragmentTransaction.replace(R.id.fragment_container, fragmentRSSFeedContainer,"rss_feed");
            fragmentTransaction.addToBackStack("rss_feed").commit();
            fragmentRSSFeedContainer.setFavouriteList(favouriteListMainActivity);

            if (drawerLayout != null) {
                drawerLayout.closeDrawer(Gravity.LEFT);
            }

            if (bookmarks != null && favourites!=null) {
                bookmarks.setSelected(false);
                favourites.setSelected(false);
            }

            resetColors();

            setTitle(rssFeedCategory.getCategoryName());

            return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        NewsController newsController = new NewsController();
        newsController.updateFavourites(favouriteListMainActivity,this);
    }

    public void setWindowStatusBarColor(Integer color){
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP){
            window.setStatusBarColor(color);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resetColors();
        setToolbarAndFabVisibility(true);
    }

    public void setToolbarAndFabVisibility(Boolean trueOrFalse){


        if(trueOrFalse!=null){
            if(trueOrFalse){
                toolbar.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
            }else{
                toolbar.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            }
        }
    }

    public void resetColors(){
        if(drawerLayout!=null && toolbar!=null && window!=null){
            drawerLayout.setBackgroundColor(0xFF212121);
            toolbar.setBackgroundColor(0xFF212121);
            setWindowStatusBarColor(0xFF212121);
        }
    }

    public void setDrawerLayoutBackgroundColor(Integer color){
        if(drawerLayout!=null && color!=null && toolbar!=null && window!=null){
            drawerLayout.setBackgroundColor(color);
            setWindowStatusBarColor(color);
            toolbar.setBackgroundColor(color);
        }


    }

    public void setSnackbar(String snackbarMessage) {
        if (coordinatorLayout != null) {
            snackbar = Snackbar.make(coordinatorLayout, snackbarMessage, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(0x89000000);
            snackbar.show();
        }    }


}
