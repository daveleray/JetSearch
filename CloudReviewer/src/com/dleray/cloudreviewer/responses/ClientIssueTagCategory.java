package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

public class ClientIssueTagCategory {

	private String categoryName;

	
	private ArrayList<ClientIssueTagSubCategory> subcategories=new ArrayList();


	private String categoryID;

	public ArrayList<ClientIssueTagSubCategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(ArrayList<ClientIssueTagSubCategory> subcategories) {
		this.subcategories = subcategories;
	}

	public String getCategoryName() {
		return categoryName;
	}

	
	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
		this.categoryID=categoryName.replace(" ","");
	}


	
}
