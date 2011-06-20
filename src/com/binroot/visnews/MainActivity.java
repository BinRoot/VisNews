package com.binroot.visnews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

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
	boolean found = true;
	List<Overlay> mapOverlays;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setFocusable(true);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);

		mapController = mapView.getController();

		mapOverlays = mapView.getOverlays();

		Drawable mapPinDrawable = this.getResources().getDrawable(R.drawable.icon);

		NewsOverlay itemizedoverlay = new NewsOverlay(mapPinDrawable);

		GeoPoint point = new GeoPoint(19240000,-99120000);
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");

		GeoPoint point2 = new GeoPoint(35410000, 139460000);
		OverlayItem overlayitem2 = new OverlayItem(point2, "Sekai, konichiwa!", "I'm in Japan!");

		itemizedoverlay.addOverlay(overlayitem);
		itemizedoverlay.addOverlay(overlayitem2);
		mapOverlays.add(itemizedoverlay);
		

		//---- News Parsing

		NewsParser news = new NewsParser();
		news.initRequest();
		Log.v("NEWS HTTP", news.getFullHTML());
		newsBlocks = news.getNewsBlocks();


		new Thread(new Runnable() {
			public void run() {
				try {
					loadCSV();
				} catch (IOException e) {
					Log.v("ERR", "IO Exception");
				}
			}
		}).start();

	
		
	}

	
	
	private void loadCSV() throws IOException {

		ArrayList<City> cityList = new ArrayList<City>();
		ArrayList<Country> countryList = new ArrayList<Country>();
		
/*
		InputStream citiesStream = getResources().openRawResource(R.raw.cities1);
		BufferedReader citiesScanner = new BufferedReader(new InputStreamReader(citiesStream));
		InputStream citiesStream2 = getResources().openRawResource(R.raw.cities2);
		InputStream citiesStream3 = getResources().openRawResource(R.raw.cities3);
		
		citiesStream2 = new SequenceInputStream(citiesStream2, citiesStream3);
		citiesStream = new SequenceInputStream(citiesStream, citiesStream2);
		
		InputStream countriesStream = getResources().openRawResource(R.raw.countries);
		BufferedReader countriesScanner = new BufferedReader(new InputStreamReader(countriesStream));
*/
		InputStream citiesStream = getResources().openRawResource(R.raw.cities_small);
		InputStream citiesStream2 = getResources().openRawResource(R.raw.cities_small2);
		citiesStream = new SequenceInputStream(citiesStream, citiesStream2);
		BufferedReader citiesScanner = new BufferedReader(new InputStreamReader(citiesStream));
		InputStream countriesStream = getResources().openRawResource(R.raw.countries_small);
		BufferedReader countriesScanner = new BufferedReader(new InputStreamReader(countriesStream));

		
		
		String line;
		citiesScanner.readLine();
		while ((line = citiesScanner.readLine()) != null) {
			String splitArr[] = line.split(",");

			cityList.add(new City(
					splitArr[0],
					splitArr[1],
					splitArr[2],
					splitArr[3]
			));
		}

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

		
		//String headline = "State media: China's torrential rains kill 2 - CNN International";
		
		Drawable mapPinDrawable = this.getResources().getDrawable(R.drawable.icon);
		NewsOverlay itemizedoverlay = new NewsOverlay(mapPinDrawable);;
		for(int i=0; i<newsBlocks.size(); i++) {
			
			ArrayList<Country> foundCountries = new ArrayList<Country>();
			ArrayList<City> foundCities = new ArrayList<City>();
			Country finalCountry = null;
			City finalCity = null;
			
			String headline = newsBlocks.get(i).getTitle();
			
			//System.out.println("old headline: "+headline);
			if(headline.contains(" - ")) {
				int headlineStopIndex = headline.lastIndexOf(" - ");
				headline = headline.substring(0, headlineStopIndex);
			}
			//System.out.println("new headline: "+headline);
			
			String [] splitHeadline = headline.split(" ");
			for(int j=0; j<splitHeadline.length; j++) {
				
				//remove sugar coating
				
				String word = splitHeadline[j].trim();
				String word2 = "";
				if(j+1<splitHeadline.length)
					word2 = splitHeadline[j+1].trim();
				
				word = cleanWord(word);
				word2 = cleanWord(word2);
				
				System.out.print(word + " ");
				
				for(City city : cityList) {
					if(city.getCity().equals("\""+word+"\"")) {
						System.out.print("<(city) ");
						foundCities.add(city);
					}
					else if(city.getCity().equals("\""+word+" "+word2+"\"")) {
						System.out.print("<(city)> ");
						foundCities.add(city);
						break;
					}
				}
				for(Country country : countryList) {
					if(country.getCountry().equals("\""+word+"\"") ||
							country.getNationalitySingular().equals("\""+word+"\"") ||
							country.getNationalityPlural().equals("\""+word+"\"")) {
						System.out.print("<(country) ");
						foundCountries.add(country);
						break;
					}
					else if(country.getCountry().equals("\""+word+" "+word2+"\"") ||
							country.getNationalitySingular().equals("\""+word+" "+word2+"\"") ||
							country.getNationalityPlural().equals("\""+word+" "+word2+"\"")) {
						System.out.print("<(country)> ");
						foundCountries.add(country);
						break;
					}
				}
			}
			System.out.println();
			System.out.println("found countires: "+foundCountries.toString());
			System.out.println("found cities: "+foundCities.toString());
			
			for(Country country : foundCountries) {
				String countryId = country.getCountryId();
				finalCountry = country;
				for(City city : foundCities) {
					String cityCountryId = city.getCountryId();
				
					if(countryId.equals(cityCountryId)) {
						finalCity = city;
						break;
					}
				}
			}

			if(foundCities.isEmpty() && foundCountries.isEmpty()) {
				System.out.println("ERR: no city or country!");
			}
			else {
				System.out.println("1. finalCountry: "+finalCountry+", finalCity "+finalCity);
				
				if(finalCountry==null) {
					
					if(!foundCountries.isEmpty())
						finalCountry = foundCountries.get(0);
					
					for(City city : foundCities) {
						String cityCountryId = city.getCountryId();
						System.out.println("looking for country id "+cityCountryId);
						for(Country country : countryList) {
							if(country.getCountryId().equals(cityCountryId)) {
								System.out.println("FOUND! "+country.getCountry());
								finalCountry = country;
								break;
							}
						}
					}
				}
				System.out.println("2. finalCountry: "+finalCountry+", finalCity "+finalCity);
				
				if(finalCity==null) {
					
					if(!foundCities.isEmpty())
						finalCity = foundCities.get(0);
					
					String capital = finalCountry.getCapital();
					System.out.println("capital: "+capital);
					for(City city : cityList) {
						if(city.getCity().equals(capital.replace(" ", "")) &&
								city.getCountryId().equals(finalCountry.getCountryId())) {
							finalCity = city;
						}
					}
				}
				System.out.println("3. finalCountry: "+finalCountry+", finalCity "+finalCity);
				
				System.out.println("Pointing map to city: "+finalCity.toString());
				System.out.println("which is in country: "+finalCountry.toString());
				double lat = Double.parseDouble(finalCity.getLatitude().replace("\"", ""));
				double lon= Double.parseDouble(finalCity.getLongtidue().replace("\"", ""));
				System.out.println(lat+", "+lon);
				Log.v("YAY!!!", lat+", "+lon);
				int iLat = (int) ( lat * 1000000 );
				int iLon = (int) ( lon * 1000000 );
				GeoPoint newpoint = new GeoPoint(iLat, iLon);
				OverlayItem newOverlayitem = new OverlayItem(newpoint, finalCity.getCity(), newsBlocks.get(i).getTitle());

				itemizedoverlay.addOverlay(newOverlayitem);
				mapOverlays.add(itemizedoverlay);
			}
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