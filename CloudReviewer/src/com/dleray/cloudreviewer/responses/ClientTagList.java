package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

public class ClientTagList extends ClientTaggingControl {

	private String id;
	
	private ArrayList<ClientIssueTagCategory> categoryList=new ArrayList();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<ClientIssueTagCategory> getCategories() {
		return categoryList;
	}

	public void setTagList(ArrayList<ClientIssueTagCategory> tagList) {
		this.categoryList = tagList;
	}

	
}
