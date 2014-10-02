package com.dleray.cloudreviewer.structures;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class IssueTagCategory {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String categoryID;
	
	@Persistent
	private String categoryDisplayName;

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public String getCategoryDisplayName() {
		return categoryDisplayName;
	}

	public void setCategoryDisplayName(String categoryDisplayName) {
		this.categoryDisplayName = categoryDisplayName;
	}
	
	
}
