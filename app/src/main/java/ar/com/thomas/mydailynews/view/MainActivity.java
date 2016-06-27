package ar.com.thomas.mydailynews.view;

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
import ar.com.thomas.mydailynews.dao.RSSFeedCategoryDAO;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.model.RSSFeedCategory;
import ar.com.thomas.mydailynews.view.NewsActivity.FragmentNewsContainer;
import ar.com.thomas.mydailynews.view.RSSFeedsActivity.FragmentRSSFeedContainer;
import ar.com.thomas.mydailynews.view.RSSFeedsActivity.FragmentRSSFeedViewPager;

public class MainActivity extends AppCompatActivity implements FragmentRSSFeedViewPager.FragmentCalls{

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private List<RSSFeedCategory> rssFeedCategoryList;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new ListenerMenu());

        populateNavigationDrawerMenu();
    }


    @Override
    public void getNotifications(News selectedNews, Integer newsPosition, List<News>newsList) {

        FragmentNewsContainer fragmentNewsContainer = new FragmentNewsContainer();
        Bundle arguments = new Bundle();
        arguments.putString(FragmentNewsContainer.NEWS_TITLE, selectedNews.getTitle());
        arguments.putString(FragmentNewsContainer.NEWS_SUBTITLE, selectedNews.getSubtitle());
        arguments.putString(FragmentNewsContainer.RSS_SOURCE, selectedNews.getRSSFeed());
        arguments.putInt(FragmentNewsContainer.POSITION, newsPosition);

        fragmentNewsContainer.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragmentNewsContainer).commit();
        setTitle(selectedNews.getRSSFeed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setTitle(R.string.app_name);
    }

    private void selectedMenuItem(MenuItem item){
        RSSFeedCategory rssFeedCategory = rssFeedCategoryList.get(item.getItemId());
        FragmentRSSFeedContainer fragmentRSSFeedContainer = new FragmentRSSFeedContainer();
        Bundle arguments = new Bundle();
        arguments.putString(FragmentRSSFeedContainer.RSSFEED_CATEGORYID, rssFeedCategory.getObjectId());
        fragmentRSSFeedContainer.setArguments(arguments);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragmentRSSFeedContainer);
        fragmentTransaction.commit();
        setTitle(item.toString());
        Toast.makeText(this, item.toString(), Toast.LENGTH_LONG).show();
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
