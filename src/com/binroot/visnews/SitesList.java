package com.binroot.visnews;

import java.util.ArrayList;

/** Contains getter and setter method for varialbles */
public class SitesList {

	/** Variables */
	private ArrayList<String> name = new ArrayList<String>();
	private ArrayList<String> website = new ArrayList<String>();

	private ArrayList<String> title = new ArrayList<String>();
	private ArrayList<String> link = new ArrayList<String>();
	private ArrayList<String> category = new ArrayList<String>();
	private ArrayList<String> pubDate = new ArrayList<String>();
	private ArrayList<String> description = new ArrayList<String>();
	public ArrayList<String> getName() {
		return name;
	}
	public void setName(String name) {
		this.name.add(name);
	}
	public ArrayList<String> getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website.add(website);
	}
	public ArrayList<String> getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title.add(title);
	}
	public ArrayList<String> getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link.add(link);
	}
	public ArrayList<String> getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category.add(category);
	}
	public ArrayList<String> getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate.add(pubDate);
	}
	public ArrayList<String> getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description.add(description);
	}
	
	

}