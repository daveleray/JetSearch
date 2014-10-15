package com.dleray.cloudreviewer.structures;

import java.util.HashSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.dleray.cloudreviewer.responses.ClientBatch;
import com.dleray.cloudreviewer.responses.ClientFolderStructure;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class DocumentBatch {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String docbatchID;
	
	@Persistent(defaultFetchGroup="true")
	private HashSet<Text> docIDCollection=new HashSet();
	
	@Persistent
	private String folderID;
	
	@Persistent
	private String batchName;
	
	
	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	@Persistent
	private String userAssigned;

	public String getDocbatchID() {
		return docbatchID;
	}

	public void setDocbatchID(String docbatchID) {
		this.docbatchID = docbatchID;
	}

	public HashSet<String> getDocIDCollection() {
		HashSet<String> output=new HashSet();
		for(Text t: this.docIDCollection)
		{
			output.add(t.getValue());
		}
		return output;
	}

	public void setDocIDCollection(HashSet<String> docIDCollection) {
		this.docIDCollection=new HashSet();
		for(String s: docIDCollection)
		{
			this.docIDCollection.add(new Text(s));
		}
	}

	public String getUserAssigned() {
		return userAssigned;
	}

	public String getFolderID() {
		return folderID;
	}

	public void setFolderID(String folderID) {
		this.folderID = folderID;
	}

	public void setUserAssigned(String userAssigned) {
		this.userAssigned = userAssigned;
	}
	
	public void addDocIDToBatch(String docID)
	{
		this.docIDCollection.add(new Text(docID));
	}
	public String toString()
	{
		return "BatchID:"+docbatchID + " which has:" + docIDCollection;
	}

	public ClientBatch toClient() {
		ClientBatch output=new ClientBatch();
		output.setBatchID(this.docbatchID);
		output.setDisplayName(this.batchName);
		return output;
	}
	public ClientFolderStructure toClientFolder()
	{
		ClientFolderStructure output=new ClientFolderStructure();
		output.setFolderName(this.batchName);
		output.setFolderID(this.docbatchID);
		output.setFolder(false);
		return output;
	}
	
}
