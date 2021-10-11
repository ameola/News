package com.ameola.news;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class RSSFeedParser extends AsyncTask<Void, Void, Feed> {
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";

    final TextView mTextView;
    final URL url;
    final String authToken;

    public RSSFeedParser(TextView textView, String feedUrl, String authToken) {
        try {
            this.url = new URL(feedUrl);
            this.mTextView = textView;
            this.authToken = authToken;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public Feed doInBackground(Void... empty) {
        Feed feed = null;

        try {
            boolean isFeedHeader = true;

            // Set header values intial to the empty string
            String description = "";
            String title = "";
            String link = "";
            String language = "";
            String copyright = "";
            String author = "";
            String pubdate = "";
            String guid = "";

            //creating a XmlPull parse Factory instance
            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            XmlPullParser parser = parserFactory.newPullParser();
            // Setup a new eventReader
            InputStream in = read();
            parser.setInput(in, null);

            int eventType = parser.getEventType();

            String text = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.START_TAG:
                        if (tagName.equals(ITEM) && isFeedHeader) {
                            feed = new Feed(title, link, description, language,
                                    copyright, pubdate);
                            isFeedHeader = false;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        switch (tagName) {
                            case ITEM:
                                FeedMessage message = new FeedMessage();
                                message.setAuthor(author);
                                message.setDescription(description);
                                message.setGuid(guid);
                                message.setLink(link);
                                message.setTitle(title);
                                feed.getMessages().add(message);
                                break;
                            case TITLE:
                                title = text;
                                break;
                            case DESCRIPTION:
                                description = text;
                                break;
                            case LINK:
                                link = text;
                                break;
                            case GUID:
                                guid = text;
                                break;
                            case LANGUAGE:
                                language = text;
                                break;
                            case AUTHOR:
                                author = text;
                                break;
                            case PUB_DATE:
                                pubdate = text;
                                break;
                            case COPYRIGHT:
                                copyright = text;
                                break;
                        }
                        break;
                }

                parser.next();
                eventType = parser.getEventType();
            }
        } catch (XmlPullParserException | IOException e) {
            throw new RuntimeException(e);
        }

        return feed;
    }

    private InputStream read() {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("auth_token", authToken);
            return conn.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onPostExecute(Feed feed) {
        mTextView.setText(feed.title);
    }
}