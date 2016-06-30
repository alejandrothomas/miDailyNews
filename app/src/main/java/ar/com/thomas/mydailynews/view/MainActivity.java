package ar.com.thomas.mydailynews.view;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.RSSFeedController;
import ar.com.thomas.mydailynews.dao.RSSFeedCategoryDAO;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.model.RSSFeedCategory;
import ar.com.thomas.mydailynews.view.NewsFlow.FragmentNewsContainer;
import ar.com.thomas.mydailynews.view.RSSFeedFlow.FragmentRSSFeedContainer;
import ar.com.thomas.mydailynews.view.RSSFeedFlow.FragmentRSSFeedViewPager;

public class MainActivity extends AppCompatActivity implements FragmentRSSFeedViewPager.FragmentCalls{

    private List<RSSFeedCategory> rssFeedCategoryList;
    private NavigationView navigationView;
    Context context;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new ListenerMenu());

        populateNavigationDrawerMenu();
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
    }

    private class ListenerMenu implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            selectedMenuItem(item);
            return true;
        }
    }

    public void populateNavigationDrawerMenu(){
        Menu menu = navigationView.getMenu();
        RSSFeedCategoryDAO rssFeedCategoryDAO = new RSSFeedCategoryDAO();
        rssFeedCategoryList = rssFeedCategoryDAO.getRSSFeedCategoryList(this);

        for (Integer i=0; i<rssFeedCategoryList.size();i++){
            menu.add(R.id.navigation_drawer_menu_RSSFeedCategories, i, i,rssFeedCategoryList.get(i).getCategoryName());
            menu.setGroupCheckable(R.id.navigation_drawer_menu_RSSFeedCategories,true,true);
            menu.setGroupVisible(R.id.navigation_drawer_menu_RSSFeedCategories,true);
        }
    }
}
