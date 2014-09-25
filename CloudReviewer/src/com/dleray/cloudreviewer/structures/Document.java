package com.dleray.cloudreviewer.structures;

import java.util.ArrayList;
import java.util.HashMap;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Document {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String documentIdentifier;
	
	@Persistent(defaultFetchGroup="true")
	private HashMap<String,String> typesTogoogleDriveIDMap=new HashMap();
	
	@Persistent(defaultFetchGroup="true")
	private ArrayList<String> tagIDs=new ArrayList();

	@Persistent(defaultFetchGroup="true")
	private java.util.HashMap<DocumentMetadata,String> metadataAndValues=new java.util.HashMap();
	
	@Persistent
	private Text extractedText;
	


	public Text getExtractedText() {
		return extractedText;
	}

	public void setExtractedText(Text extractedText) {
		this.extractedText = extractedText;
	}

	public HashMap<String, String> getTypesTogoogleDriveIDMap() {
		return typesTogoogleDriveIDMap;
	}

	public void setTypesTogoogleDriveIDMap(
			HashMap<String, String> typesTogoogleDriveIDMap) {
		this.typesTogoogleDriveIDMap = typesTogoogleDriveIDMap;
	}



	public String getDocumentIdentifier() {
		return documentIdentifier;
	}

	public void setDocumentIdentifier(String documentIdentifier) {
		this.documentIdentifier = documentIdentifier;
	}

	public ArrayList<String> getTagIDs() {
		return tagIDs;
	}

	public void setTagIDs(ArrayList<String> tagIDs) {
		this.tagIDs = tagIDs;
	}

	public java.util.HashMap<DocumentMetadata, String> getMetadataAndValues() {
		return metadataAndValues;
	}

	public void setMetadataAndValues(
			java.util.HashMap<DocumentMetadata, String> metadataAndValues) {
		this.metadataAndValues = metadataAndValues;
	}


	
	
}
