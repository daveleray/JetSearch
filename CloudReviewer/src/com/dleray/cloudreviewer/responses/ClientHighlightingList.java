package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

public class ClientHighlightingList {


    private String highlightListID;
	
	
	private String highlightListDisplayName;
	
	
	private String hexColor;
	
	
	private ArrayList<String> keywords=new ArrayList();


	public String getHighlightListID() {
		return highlightListID;
	}


	public void setHighlightListID(String highlightListID) {
		this.highlightListID = highlightListID;
	}


	public String getHighlightListDisplayName() {
		return highlightListDisplayName;
	}


	public void setHighlightListDisplayName(String highlightListDisplayName) {
		this.highlightListDisplayName = highlightListDisplayName;
	}


	public String getHexColor() {
		return hexColor;
	}


	public void setHexColor(String hexColor) {
		this.hexColor = hexColor;
	}


	public ArrayList<String> getKeywords() {
		return keywords;
	}


	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}
	
	
}
