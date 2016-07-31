package ar.com.thomas.mydailynews.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.model.RSSFeedCategory;
import ar.com.thomas.mydailynews.model.RSSFeedCategoryContainer;
import ar.com.thomas.mydailynews.util.ResultListener;
import ar.com.thomas.mydailynews.view.MainActivity;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class RSSFeedCategoryDAO extends GenericDAO {
    private DatabaseReference mDatabase;

    RSSFeedCategoryContainer rssFeedCategoryContainer = new RSSFeedCategoryContainer();

    public void getRSSFeedCategoryList(final ResultListener<List<RSSFeedCategory>> listener){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RSSFeedCategoryContainer rssFeedCategoryContainer = dataSnapshot.getValue(RSSFeedCategoryContainer.class);
                listener.finish(rssFeedCategoryContainer.getResultsCat());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




//        RSSFeedCategoryContainer rssFeedCategoryContainer = (RSSFeedCategoryContainer) getObjectJSON(context,RSSFeedCategoryContainer.class,"RSSFeedCategory.json");


}
