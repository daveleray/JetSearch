package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

public class ClientIssueTagSubCategory {

	private String subCategoryName;

	private String subCategoryID;
	
	private ArrayList<ClientIssueTag> allTags=new ArrayList();

	public ArrayList<ClientIssueTag> getAllTags() {
		return allTags;
	}

	public void setAllTags(ArrayList<ClientIssueTag> allTags) {
		this.allTags = allTags;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
		this.subCategoryID=subCategoryName.replace(" ","");
	}

	public String getSubCategoryID() {
		return subCategoryID;
	}


	
	
}
