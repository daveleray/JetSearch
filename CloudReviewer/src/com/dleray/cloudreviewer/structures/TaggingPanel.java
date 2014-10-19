package com.dleray.cloudreviewer.structures;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.dleray.cloudreviewer.PMF;
import com.dleray.cloudreviewer.responses.ClientTaggingPanel;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagList;

@PersistenceCapable
public class TaggingPanel {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long taggingID;
	
	@Persistent
	private String displayName;
	
	@Persistent(defaultFetchGroup="true")
	private ArrayList<String> taggingControlIds=new ArrayList();

	

	
	public Long getTaggingID() {
		return taggingID;
	}


	public void setTaggingID(Long taggingID) {
		this.taggingID = taggingID;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	

	public ArrayList<String> getTaggingControlIds() {
		return taggingControlIds;
	}

	public void setTaggingControlIds(ArrayList<String> taggingControlIds) {
		this.taggingControlIds = taggingControlIds;
	}

	public ClientTaggingPanel toClient() {
		ClientTaggingPanel output=new ClientTaggingPanel();
		output.setPannelID(taggingID+"");
		PersistenceManager pm=PMF.get().getPersistenceManager();
		
		for(String s: taggingControlIds)
		{
			TagList control=pm.getObjectById(TagList.class,s);
			output.getTaggingControls().add(control.toClient());
		}
		if(displayName!=null)
		{
			output.setDisplayName(displayName);
		}
		else
		{
			output.setDisplayName(taggingID+"");
		}
		pm.close();
		return output;
	}
	
	
	
}
