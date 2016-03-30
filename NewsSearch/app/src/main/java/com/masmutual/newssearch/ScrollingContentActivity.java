package com.masmutual.newssearch;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ScrollingContentActivity extends AppCompatActivity {

    int articlePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().set

        articlePosition = getIntent().getExtras().getInt("article");
        getSupportActionBar().setTitle(ArticlesFragment.articles.get(articlePosition).getHeadline());

        TextView dateTimeTextView = (TextView) findViewById(R.id.dateTimeTextView);
        dateTimeTextView.setText(ArticlesFragment.articles.get(articlePosition).getPosted());

        TextView contentTextView = (TextView) findViewById(R.id.contentTextView);
        contentTextView.setText(ArticlesFragment.articles.get(articlePosition).getContent());
    }
}
