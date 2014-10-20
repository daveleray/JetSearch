package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

public class ClientTagList extends ClientTaggingControl {

	private String id;
	private String displayName;
	
	
	private ArrayList<ClientIssueTag> tagList=new ArrayList();

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public ArrayList<ClientIssueTag> getTagList() {
		return tagList;
	}

	public void setTagList(ArrayList<ClientIssueTag> tagList) {
		this.tagList = tagList;
	}

	
}
