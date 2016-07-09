package ar.com.thomas.mydailynews.view;

import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.dao.RSSFeedCategoryDAO;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.model.RSSFeedCategory;
import ar.com.thomas.mydailynews.view.FavouriteFlow.FragmentFavouriteContainer;
import ar.com.thomas.mydailynews.view.FragmentSavedContainer.FragmentSavedContainer;
import ar.com.thomas.mydailynews.view.NewsFlow.FragmentNewsContainer;
import ar.com.thomas.mydailynews.view.RSSFeedFlow.FragmentRSSFeedContainer;
import ar.com.thomas.mydailynews.view.RSSFeedFlow.FragmentRSSFeedViewPager;

public class MainActivity extends AppCompatActivity implements FragmentRSSFeedViewPager.FragmentCalls, FragmentRSSFeedContainer.FavouriteCalls{

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

    public void setSnackbar(String snackbarMessage) {
        if (coordinatorLayout != null) {
            snackbar = Snackbar.make(coordinatorLayout, snackbarMessage, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(0x89000000);
            snackbar.show();
        }    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        setSnackbar(getString(R.string.welcome));

        favouriteListMainActivity = new ArrayList<>();

        final NewsController newsController = new NewsController();
        newsController.clearNewsDB(context);


        List<RSSFeed> rssFeedList = newsController.getFavouritesFromDB(context);
        for (RSSFeed rssFeed:rssFeedList){
            favouriteListMainActivity.add(rssFeed.getTitle());
        }
        newsController.updateFavourites(favouriteListMainActivity,context);

        Window window = getWindow();
        window.setStatusBarColor(0xFF424242);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new ListenerMenu());

        populateNavigationDrawerMenu();

        final Button favourites = (Button)findViewById(R.id.favourites_button);
        if (favourites != null) {
            favourites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsController newsController = new NewsController();
                    newsController.updateFavourites(favouriteListMainActivity,context);
                    List<RSSFeed> newFavouriteList = newsController.getFavouritesFromDB(context);

                    if(newFavouriteList.size()>0) {
                        favourites.setSelected(true);
                        fragmentFavouriteContainer = new FragmentFavouriteContainer();
                        fragmentManager = getSupportFragmentManager();

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container,fragmentFavouriteContainer,"favourites");
                        fragmentTransaction.addToBackStack(null).commit();
                        fragmentFavouriteContainer.setRssFeedList(newFavouriteList);
                    }else{
                        setSnackbar(getString(R.string.snack_favourites_empty));
                    }
                }
            });
        }

        if(favouriteListMainActivity.size()<1) {
            ListenerMenu listenerMenu = new ListenerMenu();
            listenerMenu.onNavigationItemSelected(navigationView.getMenu().getItem(1));
            navigationView.getMenu().getItem(1).setChecked(true);
        }else{
            if (favourites != null) {
                favourites.performClick();
            }
        }

        Button bookmarks = (Button) findViewById(R.id.bookmarked_button);

        if (bookmarks != null) {
            bookmarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(newsController.getBookmarkNewsList(context).size()<1){
                        setSnackbar(getString(R.string.snack_bookmarks_empty));
                    }else{
                        FragmentSavedContainer fragmentSavedContainer = new FragmentSavedContainer();
                        Bundle arguments = new Bundle();
                        arguments.putString(FragmentSavedContainer.SECTION,"Bookmarks");
                        fragmentSavedContainer.setArguments(arguments);

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in,R.anim.slide_out,R.anim.slide_in,R.anim.slide_out);
                        fragmentTransaction.replace(R.id.fragment_container, fragmentSavedContainer).addToBackStack(null).commit();
                    }
                }
            });
        }

        Button history = (Button) findViewById(R.id.history_button);

        if (history != null) {
            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(newsController.getHistoryNewsList(context).size()<1){
                        setSnackbar(getString(R.string.snack_historial_empty));
                    }else{
                        FragmentSavedContainer fragmentSavedContainer = new FragmentSavedContainer();
                        Bundle arguments = new Bundle();
                        arguments.putString(FragmentSavedContainer.SECTION,"History");
                        fragmentSavedContainer.setArguments(arguments);

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in,R.anim.slide_out,R.anim.slide_in,R.anim.slide_out);
                        fragmentTransaction.replace(R.id.fragment_container, fragmentSavedContainer).addToBackStack(null).commit();
                    }
                }
            });
        }
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

    @Override
    public void getFavNotifications(String rssFeed, FloatingActionButton fab) {

        if(favouriteListMainActivity.contains(rssFeed)){
            favouriteListMainActivity.remove(rssFeed);
            fab.setSelected(false);
            setSnackbar(rssFeed+getString(R.string.snack_favourites_remove));
        }else{
            favouriteListMainActivity.add(rssFeed);
            setSnackbar(rssFeed+getString(R.string.snack_favourites_add));
            fab.setSelected(true);
        }
        fragmentRSSFeedContainer.setFavouriteList(favouriteListMainActivity);
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
            fragmentTransaction.replace(R.id.fragment_container, fragmentRSSFeedContainer,"rss_container_tag");
            fragmentTransaction.addToBackStack("rss_container_tag").commit();
            fragmentRSSFeedContainer.setFavouriteList(favouriteListMainActivity);

            if (drawerLayout != null) {
                drawerLayout.closeDrawer(navigationView);
            }

            return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        NewsController newsController = new NewsController();
        newsController.updateFavourites(favouriteListMainActivity,this);
    }


}
