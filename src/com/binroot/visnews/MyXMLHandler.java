package com.binroot.visnews;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyXMLHandler extends DefaultHandler {

	Boolean currentElement = false;
	
	
	String currentValue = null;
	public static SitesList sitesList = null;

	public static SitesList getSitesList() {
		return sitesList;
	}

	public static void setSitesList(SitesList sitesList) {
		MyXMLHandler.sitesList = sitesList;
	}

	/** Called when tag starts ( ex:- <name>AndroidPeople</name>
	 * -- <name> )*/
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) 
		throws SAXException {

		currentElement = true;

		if (localName.equals("go"))
		{
			sitesList = new SitesList();
		} 

	}

	/** Called when tag closing ( ex:- <name>AndroidPeople</name>
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)
	throws SAXException {

		currentElement = false;

		/** set value */
		
		if (localName.equalsIgnoreCase("title"))
			sitesList.setTitle(currentValue);
		else if (localName.equalsIgnoreCase("link"))
			sitesList.setLink(currentValue);
		else if (localName.equalsIgnoreCase("pubDate"))
			sitesList.setPubDate(currentValue);
		else if (localName.equalsIgnoreCase("category"))
			sitesList.setCategory(currentValue);
		else if (localName.equalsIgnoreCase("description")) {
			sitesList.setDescription(currentValue);
		}

	}

	/** Called to get tag characters ( ex:- <name>AndroidPeople</name>
	 * -- to get AndroidPeople Character ) */
	@Override
	public void characters(char[] ch, int start, int length)
	throws SAXException {

		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
		}

	}

}
