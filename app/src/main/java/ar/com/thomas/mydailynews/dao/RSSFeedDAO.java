package ar.com.thomas.mydailynews.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.model.RSSFeedContainer;
import ar.com.thomas.mydailynews.util.ResultListener;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class RSSFeedDAO extends GenericDAO {


    public void getRSSFeedList(final ResultListener<List<RSSFeed>> listener){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.orderByKey().equalTo("resultsRss").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RSSFeedContainer rssFeedContainer = dataSnapshot.getValue(RSSFeedContainer.class);
                listener.finish(rssFeedContainer.getResultsRss());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
