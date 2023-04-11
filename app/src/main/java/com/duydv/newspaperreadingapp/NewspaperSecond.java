package com.duydv.newspaperreadingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewspaperSecond extends AppCompatActivity {
    private WebView webviewNews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspaper_second);

        webviewNews = this.<WebView>findViewById(R.id.webviewNews);

        Intent intent = getIntent();
        String link = intent.getStringExtra("link");

        webviewNews.loadUrl(link);

        webviewNews.setWebViewClient(new WebViewClient());
    }
}