package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

public class ClientFolderStructure {

	
	private String folderName;
	
	private ArrayList<ClientFolderStructure> subFolders=new ArrayList();

	private ArrayList<String> simpleFolderDisplay=new ArrayList();
	private ArrayList<String> simpleFolderIDs=new ArrayList();
	
	private ArrayList<ClientBatch> files=new ArrayList();

	private String folderID;
	
	public ArrayList<ClientFolderStructure> getSubFolders() {
		return subFolders;
	}

	public void setSubFolders(ArrayList<ClientFolderStructure> subFolders) {
		this.subFolders = subFolders;
		
	}

	public void updateSimples()
	{
		simpleFolderDisplay=processAllDisplays(this,"");
		simpleFolderIDs=processAllIDs(this);
	}
	private static ArrayList<String> processAllDisplays(ClientFolderStructure someFolder, String ongoingString)
	{
		ArrayList<String> output=new ArrayList();

	
			String outputstr=ongoingString+"/"+someFolder.folderName;
			output.add(outputstr);
	
	
			for(ClientFolderStructure s: someFolder.subFolders)
			{
				String ongoing=ongoingString+"/"+someFolder.folderName;
				output.addAll(processAllDisplays(s,ongoing));
			}
		
		return output;
	}
	private static ArrayList<String> processAllIDs(ClientFolderStructure someFolder)
	{
		ArrayList<String> output=new ArrayList();
			output.add(someFolder.folderID);
			for(ClientFolderStructure s: someFolder.subFolders)
			{
				output.addAll(processAllIDs(s));
			}
		
		return output;
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

	public ArrayList<String> getSimpleFolderDisplay() {
		return simpleFolderDisplay;
	}



	public ArrayList<String> getSimpleFolderIDs() {
		return simpleFolderIDs;
	}

	public void setFolderID(String batchFolderID) {
		// TODO Auto-generated method stub
		this.folderID=batchFolderID;
	}


	
}
