package ar.com.thomas.mydailynews.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.dao.RSSFeedCategoryDAO;
import ar.com.thomas.mydailynews.model.RSSFeedCategory;
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
    private List<String> favouriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this,getString(R.string.welcome),Toast.LENGTH_LONG).show();


        favouriteList = new ArrayList<>();
        //agregar metodo para popular la lista de favoritos desde la base de datos

        Window window = getWindow();
        window.setStatusBarColor(0xFF37474F);

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

        context = this;

        navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new ListenerMenu());

        populateNavigationDrawerMenu();
        selectedMenuItem(navigationView.getMenu().getItem(15));
        navigationView.getMenu().getItem(15).setChecked(true);
    }

    @Override
    public void getNotifications(String newsClickedID, Integer itemPosition, String rssFeedID) {

        FragmentNewsContainer fragmentNewsContainer = new FragmentNewsContainer();
        Bundle arguments = new Bundle();
        arguments.putString(FragmentNewsContainer.NEWS_TITLE_ID, newsClickedID);
        arguments.putInt(FragmentNewsContainer.POSITION, itemPosition);
        arguments.putString(FragmentNewsContainer.RSS_SOURCE, rssFeedID);

        fragmentNewsContainer.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragmentNewsContainer).commit();
    }


    @SuppressLint("CommitTransaction")
    private void selectedMenuItem(MenuItem item){

        RSSFeedCategory rssFeedCategory = rssFeedCategoryList.get(item.getItemId());

        FragmentRSSFeedContainer fragmentRSSFeedContainer = new FragmentRSSFeedContainer();
        Bundle arguments = new Bundle();

        arguments.putString(FragmentRSSFeedContainer.RSSFEED_CATEGORYID, rssFeedCategory.getObjectId());
        arguments.putString(FragmentRSSFeedContainer.RSSFEED_TITLE, rssFeedCategory.getCategoryName());

        fragmentRSSFeedContainer.setArguments(arguments);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragmentRSSFeedContainer);
        fragmentTransaction.commit();

        if (drawerLayout != null) {
            drawerLayout.closeDrawer(navigationView);
        }
    }

    public void populateNavigationDrawerMenu() {
        Menu menu = navigationView.getMenu();
        RSSFeedCategoryDAO rssFeedCategoryDAO = new RSSFeedCategoryDAO();
        rssFeedCategoryList = rssFeedCategoryDAO.getRSSFeedCategoryList(this);

        for (Integer i = 0; i < rssFeedCategoryList.size(); i++) {
            menu.add(R.id.navigation_drawer_menu_RSSFeedCategories, i, i, rssFeedCategoryList.get(i).getCategoryName());
            menu.setGroupCheckable(R.id.navigation_drawer_menu_RSSFeedCategories, true, true);
            menu.setGroupVisible(R.id.navigation_drawer_menu_RSSFeedCategories, true);
        }
    }

    @Override
    public void getFavNotifications(String rssFeed) {

        if(favouriteList.contains(rssFeed)){
            favouriteList.remove(rssFeed);
            Toast.makeText(this, rssFeed + " ha sido removido de la lista de favoritos.",Toast.LENGTH_SHORT).show();
        }else{
            favouriteList.add(rssFeed);
            Toast.makeText(this, rssFeed + " ha sido agregado de la lista de favoritos.",Toast.LENGTH_SHORT).show();

        }
    }

    private class ListenerMenu implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            selectedMenuItem(item);
            return true;
        }
    }

    public void displayFavourites(View view){
        Toast.makeText(this,String.valueOf(favouriteList.size()), Toast.LENGTH_SHORT).show();
    }
}
