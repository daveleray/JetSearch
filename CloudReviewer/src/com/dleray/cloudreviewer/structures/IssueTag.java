package com.dleray.cloudreviewer.structures;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.dleray.cloudreviewer.responses.ClientIssueTag;

@PersistenceCapable
public class IssueTag {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String tagID;
	
	@Persistent
	private String displayName;
	
	@Persistent
	private String categoryDisplay;
	
	@Persistent 
	private String subCategoryDisplay;
	




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







	public String getCategoryDisplay() {
		return categoryDisplay;
	}





	public void setCategoryDisplay(String categoryDisplay) {
		this.categoryDisplay = categoryDisplay;
	}





	public String getSubCategoryDisplay() {
		return subCategoryDisplay;
	}





	public void setSubCategoryDisplay(String subCategoryDisplay) {
		this.subCategoryDisplay = subCategoryDisplay;
	}





	public ClientIssueTag toClientIssueTag() {
		
		ClientIssueTag output=new ClientIssueTag();
		output.setCategoryDisplayName(categoryDisplay);
	
		output.setDisplayName(displayName);

		output.setSubCategoryDisplayName(subCategoryDisplay);
		output.setTagID(tagID);
		return output;
	}
	
	
}
