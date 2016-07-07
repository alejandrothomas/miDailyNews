package ar.com.thomas.mydailynews.view.FragmentSavedContainer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.view.NewsFlow.FragmentNewsContainer;
import ar.com.thomas.mydailynews.view.RSSFeedFlow.NewsAdapter;

public class FragmentSavedContainer extends Fragment {

    private Context context;
    public static final String SECTION = "section";
    private View view;
    private RecyclerView recyclerView;
    private NewsController newsController;
    private NewsAdapter newsAdapter;
    private List<News> savedNewsList;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private String section;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_bookmark_container, container, false);
        context = getActivity();

        newsController = new NewsController();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewBookmark);
        recyclerView.setHasFixedSize(true);

        Bundle arguments = getArguments();
        section = arguments.getString(SECTION);

        if(section!=null){
            if(section.equals("Bookmarks")){
                savedNewsList = newsController.getBookmarkNewsList(context);
            }
            if(section.equals("History")){
                savedNewsList = newsController.getHistoryNewsList(context);
            }
        }


        newsAdapter = new NewsAdapter(savedNewsList, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(newsAdapter);

        BookmarksListener bookmarksListener = new BookmarksListener();

        newsAdapter.setOnClickListener(bookmarksListener);
        getActivity().setTitle(section);

        return view;
    }

    private class BookmarksListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Integer itemPosition = recyclerView.getChildAdapterPosition(v);

            FragmentNewsContainer fragmentNewsContainer = new FragmentNewsContainer();
            Bundle arguments = new Bundle();
            arguments.putString(FragmentNewsContainer.NEWS_TITLE_ID, null);
            arguments.putInt(FragmentNewsContainer.POSITION, itemPosition);
            arguments.putString(FragmentNewsContainer.RSS_FEED, section);

            fragmentNewsContainer.setArguments(arguments);


            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragmentNewsContainer);
            fragmentTransaction.addToBackStack(null).commit();

        }
    }
}
