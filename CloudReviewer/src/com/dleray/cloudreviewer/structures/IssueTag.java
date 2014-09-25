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
	private String category;
	
	@Persistent 
	private String subCategory;
	
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

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

	public ClientIssueTag toClientIssueTag() {
		
		ClientIssueTag output=new ClientIssueTag();
		output.setCategory(category);
		output.setDisplayName(displayName);
		output.setSubCategory(subCategory);
		output.setTagID(tagID);
		return output;
	}
	
	
}
