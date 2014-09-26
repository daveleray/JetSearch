package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

public class ClientFolderStructure {

	
	private String folderName;
	
	private ArrayList<ClientFolderStructure> subFolders=new ArrayList();


	
	private ArrayList<ClientBatch> files=new ArrayList();
	
	public ArrayList<ClientFolderStructure> getSubFolders() {
		return subFolders;
	}

	public void setSubFolders(ArrayList<ClientFolderStructure> subFolders) {
		this.subFolders = subFolders;
	}

	public ArrayList<ClientBatch> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<ClientBatch> files) {
		this.files = files;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	
	
}
