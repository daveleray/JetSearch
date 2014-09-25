package com.dleray.cloudreviewer.structures;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.dleray.cloudreviewer.responses.ClientTaggingPanel;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagList;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagListEndpoint;

@PersistenceCapable
public class TaggingPanel {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String taggingID;
	
	@Persistent(defaultFetchGroup="true")
	private ArrayList<String> taggingControlIds=new ArrayList();

	public String getTaggingID() {
		return taggingID;
	}

	public void setTaggingID(String taggingID) {
		this.taggingID = taggingID;
	}

	public ArrayList<String> getTaggingControlIds() {
		return taggingControlIds;
	}

	public void setTaggingControlIds(ArrayList<String> taggingControlIds) {
		this.taggingControlIds = taggingControlIds;
	}

	public ClientTaggingPanel toClient() {
		ClientTaggingPanel output=new ClientTaggingPanel();
		output.setPannelID(taggingID);
		TagListEndpoint endpoint=new TagListEndpoint();
		for(String s: taggingControlIds)
		{
			TagList control=endpoint.getTagList(s);
			output.getTaggingControls().add(control.toClient());
		}
		return output;
	}
	
	
	
}
