package com.binroot.visnews;

public class NewsBlock {
	private String title = "";
	
	private String description = "";
	private String link = "";
	private String pubDate = "";
	private String category = "";
	private String imgUrl = "";
	
	private double lat = 0;
	private double lon = 0;
	private City city = null;
	private Country country = null;
	
	public NewsBlock(String title, String link, String category, String pubDate, String description) {
		this.title = title;
		this.link = link;
		this.pubDate = pubDate;
		this.category = category;
		this.description = description;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return description;
	}
	public void setBody(String body) {
		this.description = body;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}


	public String toString() {
		return "NewsBlock ["+"img="+imgUrl+", category=" + category + ", description="
				+ description + ", link=" + link + ", pubDate=" + pubDate
				+ ", title=" + title + "]";
	}
	
	
	
}
