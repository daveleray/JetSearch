package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;
import java.util.HashMap;

public class TaggingControlsResponse {

	ArrayList<ClientTagList> taglists=new ArrayList();
	HashMap<String,Boolean> tagListStatus=new HashMap();
	public ArrayList<ClientTagList> getTaglists() {
		return taglists;
	}
	public void setTaglists(ArrayList<ClientTagList> taglists) {
		this.taglists = taglists;
	}
	public HashMap<String, Boolean> getTagListStatus() {
		return tagListStatus;
	}
	public void setTagListStatus(HashMap<String, Boolean> tagListStatus) {
		this.tagListStatus = tagListStatus;
	}
	
}
