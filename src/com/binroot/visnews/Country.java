package com.binroot.visnews;

public class Country {
	String CountryId;
	String Country;
	String Capital;
	String NationalitySingular;
	String NationalityPlural;
	
	
	public Country(String countryId, String country,
			String capital, String nationalitySingular,
			String nationalityPlural) {
	
		CountryId = countryId;
		Country = country;
		Capital = capital;
		NationalitySingular = nationalitySingular;
		NationalityPlural = nationalityPlural;
	}

	public String getCountryId() {
		return CountryId;
	}

	public void setCountryId(String countryId) {
		CountryId = countryId;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	
	public String getCapital() {
		return Capital;
	}

	public void setCapital(String capital) {
		Capital = capital;
	}

	
	public String getNationalitySingular() {
		return NationalitySingular;
	}

	public void setNationalitySingular(String nationalitySingular) {
		NationalitySingular = nationalitySingular;
	}

	public String getNationalityPlural() {
		return NationalityPlural;
	}

	public void setNationalityPlural(String nationalityPlural) {
		NationalityPlural = nationalityPlural;
	}
	

	public String toString() {
		return "Country [Capital=" + Capital + ", Country=" + Country
				+ ", CountryId=" + CountryId + ", NationalityPlural="
				+ NationalityPlural + ", NationalitySingular="
				+ NationalitySingular;
	}
	
	
}
