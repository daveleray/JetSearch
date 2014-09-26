package com.dleray.cloudreviewer.responses;


public class ClientIssueTag {


    private String tagID;
	private String displayName;

	private String categoryDisplayName;
	private String subCategoryDisplayName;
	public String getTagID() {
		return tagID;
	}
	public void setTagID(String tagID) {
		this.tagID = tagID;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCategoryDisplayName() {
		return categoryDisplayName;
	}
	public void setCategoryDisplayName(String categoryDisplayName) {
		this.categoryDisplayName = categoryDisplayName;
	}
	public String getSubCategoryDisplayName() {
		return subCategoryDisplayName;
	}
	public void setSubCategoryDisplayName(String subCategoryDisplayName) {
		this.subCategoryDisplayName = subCategoryDisplayName;
	}

	
	
}
