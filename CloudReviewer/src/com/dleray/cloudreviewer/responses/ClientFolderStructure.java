package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;

public class ClientFolderStructure {

	private boolean isFolder=true;
	
	private String text;
	private String icon=null;


	private ArrayList<String> simpleFolderDisplay=new ArrayList();
	private ArrayList<String> simpleFolderIDs=new ArrayList();
	
	private ArrayList<ClientFolderStructure> children=new ArrayList();
	private String id;
	



	public ArrayList<ClientFolderStructure> getChildren() {
		return children;
	}
	public boolean isFolder() {
		return isFolder;
	}
	public void setFolder(boolean isFolder) {
		if(!isFolder)
		{
			icon="glyphicon glyphicon-file";
		}
		this.isFolder = isFolder;
	}
	
	public void updateSimples()
	{
		simpleFolderDisplay=processAllDisplays(this,"");
		simpleFolderIDs=processAllIDs(this);
	}
	private static ArrayList<String> processAllDisplays(ClientFolderStructure someFolder, String ongoingString)
	{
		ArrayList<String> output=new ArrayList();

	
			String outputstr=ongoingString+"/"+someFolder.text;
			output.add(outputstr);
	
	
			for(ClientFolderStructure s: someFolder.children)
			{
				if(s.isFolder)
				{
					String ongoing=ongoingString+"/"+someFolder.text;
					output.addAll(processAllDisplays(s,ongoing));
				}
				
			}
		
		return output;
	}
	private static ArrayList<String> processAllIDs(ClientFolderStructure someFolder)
	{
		ArrayList<String> output=new ArrayList();
			output.add(someFolder.id);
			for(ClientFolderStructure s: someFolder.children)
			{
				if(s.isFolder)
				{
					output.addAll(processAllIDs(s));
				}
				
			}
		
		return output;
	}
	
	


	public void setChildren(ArrayList<ClientFolderStructure> children) {
		this.children = children;
	}
	public String getFolderName() {
		return text;
	}

	public void setFolderName(String folderName) {
		this.text = folderName;
	}

	public ArrayList<String> getSimpleFolderDisplay() {
		return simpleFolderDisplay;
	}



	public ArrayList<String> getSimpleFolderIDs() {
		return simpleFolderIDs;
	}

	public void setFolderID(String batchFolderID) {
		// TODO Auto-generated method stub
		this.id=batchFolderID;
	}


	
}
