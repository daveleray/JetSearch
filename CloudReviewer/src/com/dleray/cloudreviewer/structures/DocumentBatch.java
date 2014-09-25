package com.dleray.cloudreviewer.structures;

import java.util.HashSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class DocumentBatch {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String docbatchID;
	
	@Persistent(defaultFetchGroup="true")
	private HashSet<String> docIDCollection=new HashSet();
	
	@Persistent
	private String userAssigned;

	public String getDocbatchID() {
		return docbatchID;
	}

	public void setDocbatchID(String docbatchID) {
		this.docbatchID = docbatchID;
	}

	public HashSet<String> getDocIDCollection() {
		return docIDCollection;
	}

	public void setDocIDCollection(HashSet<String> docIDCollection) {
		this.docIDCollection = docIDCollection;
	}

	public String getUserAssigned() {
		return userAssigned;
	}

	public void setUserAssigned(String userAssigned) {
		this.userAssigned = userAssigned;
	}
	
	public void addDocIDToBatch(String docID)
	{
		this.docIDCollection.add(docID);
	}
	public String toString()
	{
		return "BatchID:"+docbatchID + " which has:" + docIDCollection;
	}
	
}
