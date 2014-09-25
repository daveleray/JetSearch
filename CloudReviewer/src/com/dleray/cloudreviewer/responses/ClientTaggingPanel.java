package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

import javax.jdo.annotations.Persistent;

public class ClientTaggingPanel {

	private String pannelID;
	
	private ArrayList<ClientTaggingControl> taggingControls=new ArrayList();

	public String getPannelID() {
		return pannelID;
	}

	public void setPannelID(String pannelID) {
		this.pannelID = pannelID;
	}

	public ArrayList<ClientTaggingControl> getTaggingControls() {
		return taggingControls;
	}

	public void setTaggingControls(ArrayList<ClientTaggingControl> taggingControls) {
		this.taggingControls = taggingControls;
	}
	
	
	
}
