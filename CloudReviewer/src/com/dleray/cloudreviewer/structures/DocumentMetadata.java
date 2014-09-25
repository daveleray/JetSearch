package com.dleray.cloudreviewer.structures;

import java.util.HashSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
/* A DocumentMetadata is a type of metadata associated for a document.  For example, Date is a DocumentMetadata or Hashcode is a DocumentMetadata.  
 * Metadata can either be string input, multi-select, or single select
 * 
 */
public class DocumentMetadata {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String metadataColumnID;
	
	@Persistent
	private String displayName;

	@Persistent(defaultFetchGroup="true")
	private HashSet<String> possibleValues=new HashSet();
	
	public String getMetadataColumnID() {
		return metadataColumnID;
	}

	public void setMetadataColumnID(String metadataColumnID) {
		this.metadataColumnID = metadataColumnID;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public HashSet<String> getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(HashSet<String> possibleValues) {
		this.possibleValues = possibleValues;
	}

	public enum MetaDataTypes{
		STRING,SINGLE_SELECT,MULTI_SELECT;
	}
	
}
