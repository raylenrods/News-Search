package com.example.newssearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    viewAdapter adapter;
    Button findButton,button1;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        searchEditText = findViewById(R.id.searchEditText);
        findButton = findViewById(R.id.findButton);

        //OnClickListener for searching the news
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString();
                setupRecyclerView();
                getNews(query);
            }
        });
    }

    void setupRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new viewAdapter(articleList,MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    void getNews(String query){
        NewsApiClient newsApiClient = new NewsApiClient("710119f4520a4c25b4ab12e46322e7db");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {

                        runOnUiThread(()->{
                            articleList = response.getArticles();
                            adapter.updateData(articleList);
                            adapter.notifyDataSetChanged();
                        });

                        Log.i("Response recieved", response.toString());
                        response.getArticles().forEach((a)->{
                            Log.i("Article", a.getTitle());
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("Failure", throwable.getMessage());
                    }
                }
        );
    }
}

