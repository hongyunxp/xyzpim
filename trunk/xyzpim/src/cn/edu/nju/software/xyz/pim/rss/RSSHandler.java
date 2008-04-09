/*
 * XYZPIM, the pim on Android Platform
 *
 * Copyright (c) 2008, xyz team or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by xyz team.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package cn.edu.nju.software.xyz.pim.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

/**
 * @author savio 2008-4-8 下午09:39:13 Parses RSS feed and saves what we need from
 *         them
 */
/*
 * <?xml version="1.0" encoding="utf-8" ?> 
<rss version="2.0" xml:base="http://www.helloandroid.com" xmlns:dc="http://purl.org/dc/elements/1.1/">
    <channel>
        <title>Hello Android - Android OS news, tutorials, downloads</title> 
        <link>http://www.helloandroid.com</link> 
        <description /> 
        <language>en</language> 
        <item>
            <title>Biggest story of the year!</title> 
            <link>http://www.helloandroid.com/node/59</link> 
            <description>Here is a teaser for the story.</description> 
            <comments>http://www.helloandroid.com/node/59#comments</comments> 
            <pubDate>Sat, 17 Nov 2007 15:07:25 -0600</pubDate> 
            <dc:creator>hobbs</dc:creator> 
        </item>
    </channel>
</rss>
 */
public class RSSHandler extends DefaultHandler {
	// Used to define what elements we are currently in
	private boolean inItem = false;
	private boolean inTitle = false;
	private boolean inLink = false;
	private boolean inDiscription = false;
	private boolean inPubdate = false;

	// Feed and Article objects to use for temporary storage
	private Article currentArticle = new Article();
	private Feed currentFeed = new Feed();

	// Number of articles added so far
	private int articlesAdded = 0;

	// Number of articles to download
	private static final int ARTICLES_LIMIT = 200;

	// The possible values for targetFlag
	private static final int TARGET_FEED = 0;
	private static final int TARGET_ARTICLES = 1;

	// A flag to know if looking for Articles or Feed name
	private int targetFlag;

	private NewsDroidDB droidDB = null;

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (name.trim().equals("title"))
			inTitle = true;
		else if (name.trim().equals("item"))
			inItem = true;
		else if (name.trim().equals("link"))
			inLink = true;
		else if (name.trim().equals("pubDate"))
			inPubdate = true;
		else if (name.trim().equals("description"))
			inDiscription = true;
	}

	public void endElement(String uri, String name, String qName)
			throws SAXException {
		if (name.trim().equals("title"))
			inTitle = false;
		else if (name.trim().equals("item"))
			inItem = false;
		else if (name.trim().equals("link"))
			inLink = false;
		else if (name.trim().equals("pubDate"))
			inPubdate = false;
		else if (name.trim().equals("description"))
			inDiscription = false;

		// Check if looking for feed, and if feed is complete
		if (targetFlag == TARGET_FEED && currentFeed.Url != null
				&& currentFeed.Title != null) {

			// We know everything we need to know, so insert feed and exit
			droidDB.insertFeed(currentFeed.Title, currentFeed.Url);
			Log.i("XYZPIM", currentFeed.Title);
			Log.i("XYZPIM", currentFeed.Url.toString());
			throw new SAXException();
		}

		// Check if looking for article, and if article is complete
		if (targetFlag == TARGET_ARTICLES && currentArticle.Url != null
				&& currentArticle.Title != null && currentArticle.date != null
				&& currentArticle.Discription != null) {
			droidDB.insertArticle(currentFeed.FeedId, currentArticle.Title,
					currentArticle.Url, currentArticle.date,
					currentArticle.Discription);
			currentArticle.Title = null;
			currentArticle.Url = null;
			currentArticle.Discription = null;
			currentArticle.date = null;

			// Lets check if we've hit our limit on number of articles
			articlesAdded++;
			if (articlesAdded >= ARTICLES_LIMIT)
				throw new SAXException();
		}
	}

	public void characters(char ch[], int start, int length) {

		String chars = (new String(ch).substring(start, start + length));

		try {
			// If not in item, then title/link refers to feed
			if (!inItem) {
				if (inTitle)
					currentFeed.Title = chars;
				// if (inLink)
				// currentFeed.Url = new URL(chars);
			} else {
				if (inLink)
					currentArticle.Url = new URL(chars);
				if (inTitle)
					currentArticle.Title = chars;
				if (inPubdate)
					currentArticle.date = chars;
				if (inDiscription)
					currentArticle.Discription = chars;
			}
		} catch (MalformedURLException e) {
			Log.e("NewsDroid", e.toString());
		}
	}

	public void createFeed(Context ctx, URL url) {
		try {
			targetFlag = TARGET_FEED;
			droidDB = new NewsDroidDB(ctx);
			currentFeed.Url = url;

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(this);
			xr.parse(new InputSource(url.openStream()));

		} catch (IOException e) {
			Log.e("NewsDroid", e.toString());
		} catch (SAXException e) {
			Log.e("NewsDroid", e.toString());
		} catch (ParserConfigurationException e) {
			Log.e("NewsDroid", e.toString());
		}
	}

	public void updateArticles(Context ctx, Feed feed) {
		try {
			targetFlag = TARGET_ARTICLES;
			droidDB = new NewsDroidDB(ctx);
			currentFeed = feed;

			droidDB.deleteAricles(feed.FeedId);

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(this);
			xr.parse(new InputSource(currentFeed.Url.openStream()));

		} catch (IOException e) {
			Log.e("NewsDroid", e.toString());
		} catch (SAXException e) {
			Log.e("NewsDroid", e.toString());
		} catch (ParserConfigurationException e) {
			Log.e("NewsDroid", e.toString());
		}
	}
}
