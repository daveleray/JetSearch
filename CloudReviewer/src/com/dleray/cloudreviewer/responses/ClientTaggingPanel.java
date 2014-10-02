package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

import javax.jdo.annotations.Persistent;

public class ClientTaggingPanel {

	private String taggingID;
	
	private String displayName;
	
	private ArrayList<ClientTaggingControl> taggingControls=new ArrayList();

	public String getPannelID() {
		return taggingID;
	}

	public void setPannelID(String pannelID) {
		this.taggingID = pannelID;
	}

	public ArrayList<ClientTaggingControl> getTaggingControls() {
		return taggingControls;
	}

	public void setTaggingControls(ArrayList<ClientTaggingControl> taggingControls) {
		this.taggingControls = taggingControls;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	
	
}
