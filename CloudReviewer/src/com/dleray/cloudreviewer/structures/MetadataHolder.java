package com.dleray.cloudreviewer.structures;

import java.util.ArrayList;
import java.util.HashMap;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;


@PersistenceCapable
public class MetadataHolder {

	  @PrimaryKey
	    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	    private Long id;
	
	@Persistent(defaultFetchGroup="true")
	private ArrayList<Text> linesOfMetadata=new ArrayList();

	@Persistent
	private String delimiter;
	
	
	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ArrayList<Text> getLinesOfMetadata() {
		return linesOfMetadata;
	}

	public void setLinesOfMetadata(ArrayList<Text> linesOfMetadata) {
		this.linesOfMetadata = linesOfMetadata;
	}
	
	

}
