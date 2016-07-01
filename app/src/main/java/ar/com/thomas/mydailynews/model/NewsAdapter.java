package ar.com.thomas.mydailynews.model;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ar.com.thomas.mydailynews.R;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class NewsAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    private List<News> newsList;
    private View.OnClickListener listener;
    Context context;

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public NewsAdapter(List<News> newsList, Context context){
        this.newsList = newsList;
        this.context = context;
    }

    public String selectedNewsID(Integer position){
        return newsList.get(position).getTitle();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_rssfeed_viewpager_detail,parent,false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(itemView);
        itemView.setOnClickListener(this);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        News news = newsList.get(position);
        NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
        newsViewHolder.bindNews(news, context);

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

        public NewsViewHolder(View view){
            super(view);
            textViewTitle=(TextView) view.findViewById(R.id.title_textview_fragmentRSSHolderDetail);
            imageViewImageUrl=(ImageView) view.findViewById(R.id.imageUrl_textview_fragmentRSSHolderDetail);
        }

        public void bindNews(News news, Context context){

            textViewTitle.setText(news.getTitle());
            Picasso.with(context).load(news.getImageUrl()).placeholder(R.drawable.unavailable_image).into(imageViewImageUrl);
        }
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

}
