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
	    private Long id;
	
	@Persistent
	private String projectID;
	
	public String getProjectID() {
		return projectID;
	}





	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}





	@Persistent
	private String displayName;
	










	public Long getId() {
		return id;
	}





	public void setId(Long id) {
		this.id = id;
	}





	public String getDisplayName() {
		return displayName;
	}





	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}











	public ClientIssueTag toClientIssueTag() {
		
		ClientIssueTag output=new ClientIssueTag();

	
		output.setDisplayName(displayName);
		output.setTagID(id+"");
		return output;
	}
	
	
}
