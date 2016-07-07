package ar.com.thomas.mydailynews.view.RSSFeedFlow;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.view.MainActivity;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class NewsAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    private Context context;
    private List<News> newsList;
    private View.OnClickListener listener;
    private NewsController newsController;
    private List <News> bookmarkedNewsList;


    public NewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    public NewsAdapter(){

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

        newsViewHolder.bookmarkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(bookmarkedNewsList.contains(news)){
                    newsViewHolder.bookmarkButton.setSelected(false);
                    newsController.removeBookmark(context,news);
                    bookmarkedNewsList.remove(news);
                    notifyDataSetChanged();
                    Toast.makeText(context, news.getTitle() + " ha sido removido de los Bookmarks.",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, news.getTitle() + " ha sido agregado de los Bookmarks.",Toast.LENGTH_SHORT).show();
                    newsViewHolder.bookmarkButton.setSelected(true);
                    newsController.addBookmark(context,news);
                    bookmarkedNewsList.add(news);
                    notifyDataSetChanged();
                }
            }
        });
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

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
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
            if (news.getImageUrl()==null){
                imageViewImageUrl.setImageResource(R.drawable.placeholder_unavailable_image);
            }else{
                Picasso.with(context).load(news.getImageUrl()).into(imageViewImageUrl);
            }
        }
    }
}