package com.dleray.cloudreviewer.responses;

public class DocJSONResponse {

	private Integer batchSize;
	private String batchID;
	private String docIdentifier;
	private Integer docInBatchIndex;
	
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
	
}
