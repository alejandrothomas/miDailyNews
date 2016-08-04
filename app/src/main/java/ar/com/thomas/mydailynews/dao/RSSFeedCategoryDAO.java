package ar.com.thomas.mydailynews.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import ar.com.thomas.mydailynews.model.RSSFeedCategory;
import ar.com.thomas.mydailynews.model.RSSFeedCategoryContainer;
import ar.com.thomas.mydailynews.util.ResultListener;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class RSSFeedCategoryDAO extends GenericDAO {

    public void getRSSFeedCategoryList(final ResultListener<List<RSSFeedCategory>> listener){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.orderByKey().equalTo("resultsCat").addListenerForSingleValueEvent(new ValueEventListener() {
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
}
