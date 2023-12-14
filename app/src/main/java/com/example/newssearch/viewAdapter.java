package com.example.newssearch;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kwabenaberko.newsapilib.models.Article;
import java.util.List;
import java.util.Locale;

public class viewAdapter extends RecyclerView.Adapter<viewAdapter.NewsHolder> {

     TextToSpeech t1;
     Context context;
    List<Article> articleList;

    viewAdapter(List<Article> articleList, Context context) {

        this.articleList =articleList;
        this.context = context;

        t1 = new TextToSpeech(context, status -> {
            if (status != TextToSpeech.ERROR)
                t1.setLanguage(Locale.ENGLISH);
        });
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row,parent,false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        Article article = articleList.get(position);
        holder.title.setText(article.getTitle());

        //OnClickListener for text to speech
        holder.itemView.findViewById(R.id.button1).setOnClickListener(v -> {
            String textToRead = article.getTitle();
            t1.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null, null);
        });

        //OnClickListener for opening the webpage
        holder.itemView.findViewById(R.id.button2).setOnClickListener((v -> {
            Intent intent = new Intent(v.getContext(),webpage.class) ;
            intent.putExtra("url",article.getUrl());
            v.getContext().startActivity(intent);
        }));

    }
    void updateData(List<Article> data){
        articleList.clear();
        articleList.addAll(data);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class NewsHolder extends RecyclerView.ViewHolder{

        TextView title;
        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.article_title);
        }
    }
}
