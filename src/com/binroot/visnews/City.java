package com.binroot.visnews;

public class City {
	String countryId = "";
	String city = "";
	String latitude = "";
	String longtidue = "";

	public City(String countryId, String city, String latitude, String longtidue) {
		this.countryId = countryId;
		this.city = city;
		this.latitude = latitude;
		this.longtidue = longtidue;
	}

	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongtidue() {
		return longtidue;
	}
	public void setLongtidue(String longtidue) {
		this.longtidue = longtidue;
	}

	public String toString() {
		return "City [city=" + city + ", countryId="
		+ countryId + ", latitude=" + latitude + ", longtidue="
		+ longtidue + "]";
	}
}
