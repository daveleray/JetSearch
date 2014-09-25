package com.dleray.cloudreviewer.structures;

public class SortStrategy {

	private DocumentMetadata metadataToSort;
	private SortDirection direction;
	
	
	public enum SortDirection{
		ASCENDING,DESCENDING;
	}


	public DocumentMetadata getMetadataToSort() {
		return metadataToSort;
	}


	public void setMetadataToSort(DocumentMetadata metadataToSort) {
		this.metadataToSort = metadataToSort;
	}


	public SortDirection getDirection() {
		return direction;
	}


	public void setDirection(SortDirection direction) {
		this.direction = direction;
	}
	
	
	
}
