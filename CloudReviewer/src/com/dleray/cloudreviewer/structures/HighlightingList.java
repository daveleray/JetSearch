package com.dleray.cloudreviewer.structures;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.dleray.cloudreviewer.responses.ClientHighlightingList;

@PersistenceCapable
public class HighlightingList {

	 @PrimaryKey
	    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	    private Long id;
	
	@Persistent
	private String highlightListDisplayName;
	
	@Persistent
	private String hexColor;
	
	@Persistent(defaultFetchGroup="true")
	private ArrayList<String> keywords=new ArrayList();

	public String getHexColor() {
		return hexColor;
	}

	public void setHexColor(String hexColor) {
		this.hexColor = hexColor;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHighlightListDisplayName() {
		return highlightListDisplayName;
	}

	public void setHighlightListDisplayName(String highlightListDisplayName) {
		this.highlightListDisplayName = highlightListDisplayName;
	}

	public ArrayList<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}

	public ClientHighlightingList toClient() {
		
		ClientHighlightingList output=new ClientHighlightingList();
		output.setHexColor(hexColor);
		output.setHighlightListDisplayName(highlightListDisplayName);
		output.setHighlightListID(id+"");
		output.setKeywords(keywords);
		return output;
	}
	
	
	
}
