package com.binroot.visnews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity {

	MapView mapView;
	MapController mapController;
	ArrayList<NewsBlock> newsBlocks;
	ArrayList<Country> countryList;
	
	List<Overlay> mapOverlays;
	int progress = 0;
	
	InputStream citiesStream;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		/*
		 * Setting up Stream
		 */
		citiesStream = getResources().openRawResource(R.raw.cities_small);
		InputStream citiesStream2 = getResources().openRawResource(R.raw.cities_small2);
		citiesStream = new SequenceInputStream(citiesStream, citiesStream2);

		/*
		 * MapView setup
		 */
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setFocusable(true);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);

		mapController = mapView.getController();

		mapOverlays = mapView.getOverlays();

		
		/*
		 * News Parsing
		 */
		NewsParser news = new NewsParser();
		news.initRequest();
		Log.v("NEWS HTTP", news.getFullHTML());
		newsBlocks = news.getNewsBlocks();

		/*
		 * Init new thread to load CSV
		 */
		new Thread(new Runnable() {
			public void run() {
				try {
					loadCountries();
					loadCSV();
				} catch (IOException e) {
					Log.v("ERR", "IO Exception - Countries");
				}
				
			}
		}).start();
		
		
	}

	private void loadCountries() throws IOException {
		countryList = new ArrayList<Country>(); // Total list of countries
		InputStream countriesStream = getResources().openRawResource(R.raw.countries_small);
		BufferedReader countriesScanner = new BufferedReader(new InputStreamReader(countriesStream));


		String line;
		Log.v("TICK", "1 - Starting to store all countries");

		countriesScanner.readLine();
		while((line = countriesScanner.readLine()) != null) {
			String splitArr[] = line.split(",");

			countryList.add(new Country(
					splitArr[0],
					splitArr[1],
					splitArr[2],
					splitArr[3],
					splitArr[4]
			));
		}
	}
	
	private void loadCSV() throws IOException {
		
		Log.v("TICK", "2 - Starting newsblock parsing");
		
		for(int i=0; i<newsBlocks.size(); i++) {
			
			Country finalCountry = null;
			City finalCity = null;
			
			String headline = newsBlocks.get(i).getTitle();

			if(headline.contains(" - ")) {
				int headlineStopIndex = headline.lastIndexOf(" - ");
				headline = headline.substring(0, headlineStopIndex);
			}
			
			String [] splitHeadline = headline.split(" ");
			
			// Go through all words in a headline

			for(int j=0; j<splitHeadline.length; j++) {

				//remove sugar coating
				
				String word = splitHeadline[j].trim();
				String word2 = "";
				if(j+1<splitHeadline.length) {
					word2 = splitHeadline[j+1].trim();
				}

				word = cleanWord(word);
				word2 = cleanWord(word2);

				System.out.print(word + " ");

				for(Country country : countryList) {
					if(country.getCountry().equals("\""+word+"\"") ||
							country.getNationalitySingular().equals("\""+word+"\"") ||
							country.getNationalityPlural().equals("\""+word+"\"")) {
						System.out.print("<(country) ");
						finalCountry = country;
						break;
					}
					else if(country.getCountry().equals("\""+word+" "+word2+"\"") ||
							country.getNationalitySingular().equals("\""+word+" "+word2+"\"") ||
							country.getNationalityPlural().equals("\""+word+" "+word2+"\"")) {
						System.out.print("<(country)> ");
						finalCountry = country;
						break;
					}
				}
			}


			if(finalCountry==null) {
				System.out.println("ERR: no country!");
			}
			else {
				
				System.out.println("Final country: "+finalCountry.toString());
				
				String capital = finalCountry.getCapital().replace(" ", "");
				System.out.println("capital: "+capital);
				
				
				String latStr = "";
				String lonStr = "";
				String cityStr = "";
				
				String line2="";
				
				citiesStream = getResources().openRawResource(R.raw.cities_small);
				InputStream citiesStream2 = getResources().openRawResource(R.raw.cities_small2);
				citiesStream = new SequenceInputStream(citiesStream, citiesStream2);
				BufferedReader citiesScanner = new BufferedReader(new InputStreamReader(citiesStream));

				citiesScanner.readLine();
				System.out.println("about to search through cities ");
				while ((line2 = citiesScanner.readLine()) != null) {
					String splitArr[] = line2.split(",");
					String countryIdStr = splitArr[0];
					cityStr = splitArr[1];
					if(cityStr.equals(capital) && countryIdStr.equals(finalCountry.getCountryId())) {
						latStr = splitArr[2];
						lonStr = splitArr[3];
						System.out.println("FOUND CITY "+cityStr+" "+latStr+", "+lonStr);
						break;
					}
				}
				System.out.println("finished searching through cities");
				System.out.println("lat lon = "+ latStr +", "+lonStr);
				double lat = Double.parseDouble(latStr.replace("\"", ""));
				double lon= Double.parseDouble(lonStr.replace("\"", ""));
				System.out.println(lat+", "+lon);
				Log.v("YAY!!!", lat+", "+lon);
				int iLat = (int) ( lat * 1000000 );
				int iLon = (int) ( lon * 1000000 );
				GeoPoint newpoint = new GeoPoint(iLat, iLon);
				OverlayItem newOverlayitem = new OverlayItem(newpoint, cityStr, newsBlocks.get(i).getTitle());
				
				Drawable d = LoadImageFromWebOperations(newsBlocks.get(i).getImgUrl());
				NewsOverlay itemizedoverlay = new NewsOverlay(d);
				itemizedoverlay.setContext(this);
				itemizedoverlay.setNews(newsBlocks.get(i));
				itemizedoverlay.addOverlay(newOverlayitem);

				mapOverlays.add(itemizedoverlay);
				mapController.animateTo(newpoint);           
				mapView.postInvalidate();
			}
		}
		
	}

	private Drawable LoadImageFromWebOperations(String url) 
	{ 
		try { 
			InputStream is = (InputStream) new URL(url).getContent(); 
			Drawable d = Drawable.createFromStream(is, "src name"); 
			return d; 
		}
		catch (Exception e) { 
			System.out.println("Exc="+e); 
			return null; 
		} 
	}

	public static String cleanWord(String word) {
		if(word.contains(":")) 
			word = word.replace(":", "");
		if(word.contains(",")) 
			word = word.replace(",", "");
		if(word.contains(".")) 
			word = word.replace(".", "");
		if(word.contains(";")) 
			word = word.replace(";", "");
		if(word.contains("'s"))
			word = word.replace("'s", "");
		if(word.contains("'"))
			word = word.replace("'", "");
		if(word.contains("\""))
			word = word.replace("\"", "");
		if(word.contains("/"))
			word = word.replace("/", "");
		if(word.contains("!"))
			word = word.replace("!", "");
		if(word.contains("?"))
			word = word.replace("?", "");
		if(word.contains("("))
			word = word.replace("(", "");
		if(word.contains(")"))
			word = word.replace(")", "");
		if(word.contains("<"))
			word = word.replace("<", "");
		if(word.contains(">"))
			word = word.replace(">", "");
		if(word.contains("*"))
			word = word.replace("*", "");

		return word;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}