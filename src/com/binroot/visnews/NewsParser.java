package com.binroot.visnews;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.TextView;

public class NewsParser extends Activity {
	
	final String url = "http://news.google.com/news?ned=us&topic=w&output=rss";
	String fullHTML = "";
	ArrayList<NewsBlock> newsBlocks = new ArrayList<NewsBlock>();
	
	public String getFullHTML() {
		return fullHTML;
	}

	public void setFullHTML(String fullHTML) {
		this.fullHTML = fullHTML;
	}

	public String getUrl() {
		return url;
	}

	public void initRequest() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
	
			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder str = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null)
			{
			    str.append(line);
			}
			in.close();
			fullHTML = str.toString();
		}
		catch (ClientProtocolException e) {
			Log.v("ERR", "Client Protocol Exception");
		} catch (IOException e) {
			Log.v("ERR", "IO Exception");
		}
	}
	
	public ArrayList<NewsBlock> getNewsBlocks() {
		String simpleHTML="";
		SitesList sitesList = null;
		try {
			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			/** Send URL to parse XML Tags 
			URL sourceUrl = new URL(
			"http://news.google.com/news?ned=us&topic=w&output=rss");
			//and do sourceUrl.openStream() 
			*/
			
			int startIndex = fullHTML.indexOf("<item>");
			int stopIndex = fullHTML.lastIndexOf("</item>");
			stopIndex += 7;
			
			simpleHTML = fullHTML.substring(startIndex, stopIndex);
			simpleHTML = "<go>"+simpleHTML+"</go>";
			
			InputStream is = new ByteArrayInputStream(simpleHTML.getBytes("UTF-8"));
			
			/** Create handler to handle XML Tags ( extends DefaultHandler ) */
			MyXMLHandler myXMLHandler = new MyXMLHandler();
			xr.setContentHandler(myXMLHandler);
			xr.parse(new InputSource(is));

		} catch (Exception e) {
			System.out.println("XML Pasing Exception = " + e);
		}

		/** Get result from MyXMLHandler SitlesList Object */
		sitesList = MyXMLHandler.sitesList;

		/** Assign textview array lenght by arraylist size */

		/** Set the result text in textview and add it to layout */
		
		String titleStr = sitesList.getTitle().toString();
		Log.v("XML", titleStr);
		
		int desCursor = 0;
		for(int i=0; i<sitesList.getTitle().size(); i++) {
			String title = sitesList.getTitle().get(i);
			String link = sitesList.getLink().get(i);
			String category = sitesList.getCategory().get(i);
			String pubDate = sitesList.getPubDate().get(i);
			
			int desLength = "<descripiton>".length();
			int desStartIndex = simpleHTML.indexOf("<description>", desCursor);
			desStartIndex += desLength;
			int desStopIndex = simpleHTML.indexOf("</description", desCursor);
			String description = simpleHTML.substring(desStartIndex, desStopIndex);
			desCursor = desStopIndex+1;

			
			int urlStart = description.indexOf("img src=");
			urlStart = urlStart + 14;
			int urlStop = description.indexOf(".jpg");
			urlStop = urlStop + 4;
			System.out.println("cutting from "+urlStart+" to "+urlStop);
			String imgUrl = description.substring(urlStart, urlStop);
			System.out.println("img= "+imgUrl);
			
			NewsBlock newNews = new NewsBlock(title, link, category, pubDate, description);
			newNews.setImgUrl(imgUrl);
			newsBlocks.add(newNews);
			String toStr = newNews.toString();
			Log.v("NEWSBLOCKS", toStr);
		}
		
		return newsBlocks;
		
	}

}


