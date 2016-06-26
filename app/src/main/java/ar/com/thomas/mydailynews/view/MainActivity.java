package ar.com.thomas.mydailynews.view;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.view.NewsActivity.FragmentNewsContainer;
import ar.com.thomas.mydailynews.view.RSSFeedsActivity.FragmentRSSFeedContainer;
import ar.com.thomas.mydailynews.view.RSSFeedsActivity.FragmentRSSFeedViewPager;

public class MainActivity extends AppCompatActivity implements FragmentRSSFeedViewPager.FragmentCalls{

    private NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);
        navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new ListenerMenu());

        FragmentRSSFeedContainer fragmentRSSFeedContainer = new FragmentRSSFeedContainer();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragmentRSSFeedContainer);
        fragmentTransaction.commit();
    }

    @Override
    public void getNotifications(News selectedNews, Integer newsPosition, List<News>newsList) {

        FragmentNewsContainer fragmentNewsContainer = new FragmentNewsContainer();
        Bundle arguments = new Bundle();

        arguments.putString(FragmentNewsContainer.NEWS_TITLE, selectedNews.getTitle());
        arguments.putString(FragmentNewsContainer.NEWS_SUBTITLE, selectedNews.getSubtitle());
        arguments.putString(FragmentNewsContainer.RSS_SOURCE, selectedNews.getRssSource());
        arguments.putInt(FragmentNewsContainer.POSITION, newsPosition);

        fragmentNewsContainer.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragmentNewsContainer).commit();
        setTitle(selectedNews.getRssSource());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setTitle(R.string.app_name);
    }

    private void selectedMenuItem(MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_item_1:
                Toast.makeText(this, getString(R.string.nav_item_1), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_2:
                Toast.makeText(this, getString(R.string.nav_item_2), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_3:
                Toast.makeText(this, getString(R.string.nav_item_3), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_4:
                Toast.makeText(this, getString(R.string.nav_item_4), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_5:
                Toast.makeText(this, getString(R.string.nav_item_5), Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_item_6:
                Toast.makeText(this, getString(R.string.nav_item_6), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_7:
                Toast.makeText(this, getString(R.string.nav_item_7), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_8:
                Toast.makeText(this, getString(R.string.nav_item_8), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_9:
                Toast.makeText(this, getString(R.string.nav_item_9), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_10:
                Toast.makeText(this, getString(R.string.nav_item_10), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_11:
                Toast.makeText(this, getString(R.string.nav_item_11), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_12:
                Toast.makeText(this, getString(R.string.nav_item_12), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_13:
                Toast.makeText(this, getString(R.string.nav_item_13), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_14:
                Toast.makeText(this, getString(R.string.nav_item_14), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_15:
                Toast.makeText(this, getString(R.string.nav_item_15), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_16:
                Toast.makeText(this, getString(R.string.nav_item_16), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_17:
                Toast.makeText(this, getString(R.string.nav_item_17), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_18:
                Toast.makeText(this, getString(R.string.nav_item_18), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_19:
                Toast.makeText(this, getString(R.string.nav_item_19), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_20:
                Toast.makeText(this, getString(R.string.nav_item_20), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, getString(R.string.nav_item_1), Toast.LENGTH_SHORT).show();
        }
    }

    private class ListenerMenu implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            selectedMenuItem(item);
            return true;
        }
    }
}
