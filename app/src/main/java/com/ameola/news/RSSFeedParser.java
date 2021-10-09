package com.ameola.news;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class RSSFeedParser {
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";

    final URL url;

    public RSSFeedParser(String feedUrl) {
        try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public Feed readFeed() {
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
            FeedMessage currentMessage;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                String text;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase(ITEM)) {
                            currentMessage = new FeedMessage();
                            break;
                        }
                        break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                    case XmlPullParser.END_TAG:
                        switch (tagname) {
                            case ITEM:
                                feed = new Feed(title, link, description, language,
                                    copyright, pubdate);
                                break;
                            case TITLE:
                                title = getCharacterData(event, eventReader);
                                break;
                            case DESCRIPTION:
                                description = getCharacterData(event, eventReader);
                                break;
                            case LINK:
                                link = getCharacterData(event, eventReader);
                                break;
                            case GUID:
                                guid = getCharacterData(event, eventReader);
                                break;
                            case LANGUAGE:
                                language = getCharacterData(event, eventReader);
                                break;
                            case AUTHOR:
                                author = getCharacterData(event, eventReader);
                                break;
                            case PUB_DATE:
                                pubdate = getCharacterData(event, eventReader);
                                break;
                            case COPYRIGHT:
                                copyright = getCharacterData(event, eventReader);
                                break;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
                        FeedMessage message = new FeedMessage();
                        message.setAuthor(author);
                        message.setDescription(description);
                        message.setGuid(guid);
                        message.setLink(link);
                        message.setTitle(title);
                        feed.getMessages().add(message);
                        event = eventReader.nextEvent();
                        continue;
                    }
                }
            }
        } catch (XMLStreamException | XmlPullParserException e) {
            throw new RuntimeException(e);
        }
        return feed;
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}