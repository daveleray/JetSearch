package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

public class ClientBatchMetadata {

	private String batchID;
	private String displayName;
	private String assignedUser;
	
	private ArrayList<String> headers;
	private ArrayList<ClientBatchMetadataRow> rows;
	
	
	public String getBatchID() {
		return batchID;
	}


	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getAssignedUser() {
		return assignedUser;
	}


	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}


	public ArrayList<String> getHeaders() {
		return headers;
	}


	public void setHeaders(ArrayList<String> headers) {
		this.headers = headers;
	}


	public ArrayList<ClientBatchMetadataRow> getRows() {
		return rows;
	}


	public void setRows(ArrayList<ClientBatchMetadataRow> rows) {
		this.rows = rows;
	}

	public ClientBatchMetadataRow getRowInstance()
	{
		return new ClientBatchMetadataRow();
	}

	public class ClientBatchMetadataRow
	{
		public ClientBatchMetadataRow()
		{
			
		}
		private ArrayList<String> info;

		public ArrayList<String> getInfo() {
			return info;
		}

		public void setInfo(ArrayList<String> info) {
			this.info = info;
		}
		
		
	}
}
