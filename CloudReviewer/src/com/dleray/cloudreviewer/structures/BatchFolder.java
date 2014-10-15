package com.dleray.cloudreviewer.structures;

import java.util.Collection;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.dleray.cloudreviewer.responses.ClientFolderStructure;

@PersistenceCapable
public class BatchFolder {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String batchFolderID;
		
	@Persistent
	private String parentFolderID;
	
	@Persistent
	private String displayName;

	public String getBatchFolderID() {
		return batchFolderID;
	}

	public void setBatchFolderID(String batchFolderID) {
		this.batchFolderID = batchFolderID;
	}

	public String getParentFolderID() {
		return parentFolderID;
	}

	public void setParentFolderID(String parentFolderID) {
		this.parentFolderID = parentFolderID;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public ClientFolderStructure toClient(Collection<BatchFolder> fullSet,Collection<DocumentBatch> fullBatches) {
		
		ClientFolderStructure output=new ClientFolderStructure();
		output.setFolderName(displayName);
		output.setFolderID(batchFolderID);
		for(BatchFolder f: fullSet)
		{
			if(f.parentFolderID.contentEquals(this.batchFolderID))
			{
				output.getChildren().add(f.toClient(fullSet, fullBatches));
			}
		}
		for(DocumentBatch b: fullBatches)
		{
			if(b.getFolderID()!=null && b.getFolderID().contentEquals(this.batchFolderID))
			{
				output.getChildren().add(b.toClientFolder());
			}
		}
		output.updateSimples();
		
		return output;
		
	}

	
	
}
