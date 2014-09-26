package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

public class DocJSONResponse {

	private Integer batchSize;
	private String batchID;
	private String docIdentifier;
	private Integer docInBatchIndex;
	private ArrayList<String> appliedTags;
	
	public Integer getDocInBatchIndex() {
		return docInBatchIndex;
	}
	public void setDocInBatchIndex(Integer docInBatchIndex) {
		this.docInBatchIndex = docInBatchIndex;
	}
	public Integer getBatchSize() {
		return batchSize;
	}
	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}

	public String getBatchID() {
		return batchID;
	}
	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}
	public String getDocIdentifier() {
		return docIdentifier;
	}
	public void setDocIdentifier(String docIdentifier) {
		this.docIdentifier = docIdentifier;
	}
	public ArrayList<String> getAppliedTags() {
		return appliedTags;
	}
	public void setAppliedTags(ArrayList<String> appliedTags) {
		this.appliedTags = appliedTags;
	}
	
}
