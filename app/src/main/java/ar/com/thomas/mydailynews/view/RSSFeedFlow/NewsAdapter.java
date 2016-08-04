package ar.com.thomas.mydailynews.view.RSSFeedFlow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.view.MainActivity;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class NewsAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private final static int FADE_DURATION = 250;
    private Context context;
    private List<News> newsList;
    private View.OnClickListener listener;
    private NewsController newsController;
    private List <News> bookmarkedNewsList;
    private Integer lastPosition = -1;

    public NewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public String selectedNewsID(Integer position){
        return newsList.get(position).getTitle();
    }

    public News getNews(Integer position) {
        return newsList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_rssfeed_viewpager_detail,parent,false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(itemView);
        itemView.setOnClickListener(this);

        newsController = new NewsController();
        bookmarkedNewsList = newsController.getBookmarkNewsList(context);

        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        final News news = newsList.get(position);
        final NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
        newsViewHolder.bindNews(news, context);

        setAnimation(holder.itemView,position);

        newsViewHolder.bookmarkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(bookmarkedNewsList.contains(news)){
                    ((MainActivity)context).setSnackbar(news.getTitle()+context.getResources().getString(R.string.snack_bookmarks_remove));
                    newsViewHolder.bookmarkButton.setSelected(false);
                    newsController.removeBookmark(context,news);
                    bookmarkedNewsList.remove(news);

                    notifyDataSetChanged();


                }else{
                    ((MainActivity)context).setSnackbar(news.getTitle()+context.getResources().getString(R.string.snack_bookmarks_add));
                    newsViewHolder.bookmarkButton.setSelected(true);
                    newsController.addBookmark(context,news);
                    bookmarkedNewsList.add(news);

                    notifyDataSetChanged();
                }
            }
        });
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
            alphaAnimation.setDuration(FADE_DURATION);
            ScaleAnimation anim = new ScaleAnimation(0.35f, 1.0f, 0.35f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(FADE_DURATION);
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public void onClick(View v) {
        if(listener !=null){
            listener.onClick(v);
        }
    }

    private static class NewsViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private ImageView imageViewImageUrl;
        private Button bookmarkButton;




        public NewsViewHolder(View view){
            super(view);
            textViewTitle=(TextView) view.findViewById(R.id.title_textview_fragmentRSSHolderDetail);
            imageViewImageUrl=(ImageView) view.findViewById(R.id.imageUrl_textview_fragmentRSSHolderDetail);
            bookmarkButton=(Button)view.findViewById(R.id.bookmark_button);

        }

        public void bindNews(News news, Context context){

            bookmarkButton.setSelected(false);

            NewsController newsController = new NewsController();
            List<News> newsListBookmarked = newsController.getBookmarkNewsList(context);

            if(newsListBookmarked.contains(news)){
                bookmarkButton.setSelected(true);
            }

            textViewTitle.setText(news.getTitle());

            if(news.getImageUrl()==null || news.getImageUrl().isEmpty()){
                imageViewImageUrl.setImageResource(R.drawable.placeholder_unavailable_image);
            }else{
                Picasso.with(context).load(news.getImageUrl()).resize(0,200).into(imageViewImageUrl);
            }
        }


    }

}
