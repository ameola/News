package com.ameola.news;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.ameola.news.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private TextView mTextView;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mTextView = binding.text;

        RSSFeedParser parser = new RSSFeedParser(mTextView, "https://us-central1-compact-moment-206418.cloudfunctions.net/newsfeed/feed.rss", "55a5dd7f-2af4-489d-b11b-c84974154a50");
        parser.execute();
    }
}