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

        RSSFeedParser parser = new RSSFeedParser("http://rss.cnn.com/rss/cnn_topstories.rss");
        parser.readFeed();
        mTextView = binding.text;
    }
}