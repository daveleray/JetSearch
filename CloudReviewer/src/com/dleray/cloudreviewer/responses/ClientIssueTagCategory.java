package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

public class ClientIssueTagCategory {

	private String categoryName;
	
	private ArrayList<ClientIssueTagSubCategory> subcategories=new ArrayList();

	public ArrayList<ClientIssueTagSubCategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(ArrayList<ClientIssueTagSubCategory> subcategories) {
		this.subcategories = subcategories;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	
}
