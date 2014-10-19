package com.dleray.cloudreviewer.structures;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class UserTag implements Serializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String usertagID;
	
	@Persistent
	private String document;
	
	@Persistent
	private String username;

	@Persistent
	private String issueTagID;
	
	@Persistent
	private long dateInMili;
	
	
	public long getDateInMili() {
		return dateInMili;
	}

	public void setDateInMili(long dateInMili) {
		this.dateInMili = dateInMili;
	}

	public String getIssueTagID() {
		return issueTagID;
	}

	public void setAppliedTag(String issueTagID) {
		this.issueTagID = issueTagID;
	}

	public String getUsertagID() {
		return usertagID;
	}

	public void setUsertagID(String usertagID) {
		this.usertagID = usertagID;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
