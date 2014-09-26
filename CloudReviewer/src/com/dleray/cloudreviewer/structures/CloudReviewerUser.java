package com.dleray.cloudreviewer.structures;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class CloudReviewerUser {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String userEmail;
	
	@Persistent
	private String currentPanelID;
	
	@Persistent 
	private String currentBatch;
	
	@Persistent 
	private String currentDoc;
	

	public String getCurrentPanelID() {
		return currentPanelID;
	}

	public void setCurrentPanelID(String currentPanelID) {
		this.currentPanelID = currentPanelID;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getCurrentBatch() {
		return currentBatch;
	}

	public void setCurrentBatch(String currentBatch) {
		this.currentBatch = currentBatch;
	}

	public String getCurrentDoc() {
		return currentDoc;
	}

	public void setCurrentDoc(String currentDoc) {
		this.currentDoc = currentDoc;
	}
	
}
