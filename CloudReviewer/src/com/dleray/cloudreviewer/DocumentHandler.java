package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import javax.jdo.PersistenceManager;

import com.dleray.cloudreviewer.endpoints.DocumentEndpoint;
import com.dleray.cloudreviewer.structures.Document;
import com.dleray.cloudreviewer.structures.DocumentBatch;
import com.dleray.cloudreviewer.structures.SortStrategy;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.drive.model.File;
import com.google.appengine.api.datastore.Text;

public class DocumentHandler {

	
	public static void processDriveFileList(java.util.List<File> allFiles,Drive gDrive)
	{
		Integer numFilesAttempt=0;
		Integer numFilesInStorage=0;
		Integer numNewFiles=0;
		PersistenceManager mgr=PMF.get().getPersistenceManager();
		
		for(File f: allFiles)
		{
			numFilesAttempt+=1;
			if(f.getMimeType().contains("folder"))
			{
				System.out.println("Skipping:" + f.getTitle());
				continue;
			}
			//System.out.println("Process:" + f.getOriginalFilename() + " id:" + f.getId() + " desc:"+ f.getDescription());
			if(f.getOriginalFilename()==null)
			{
				Double a=5.0;
			}
			
			String docIdentifier=f.getOriginalFilename().substring(0,f.getOriginalFilename().indexOf("."));

			Document testForDoc;
			try {
				testForDoc = DocHelper.getDocumentByID(docIdentifier, mgr);
				numFilesInStorage+=1;
			} catch (Exception e) {
				testForDoc=null;
			}
			if(testForDoc==null)
			{
				numNewFiles+=1;
				System.out.println("Adding new document:" + f.getOriginalFilename());
				testForDoc=new Document();
				testForDoc.setDocumentIdentifier(docIdentifier);
			}
			String alreadyID=testForDoc.getTypesTogoogleDriveIDMap().get(f.getFileExtension());
			if(alreadyID==null)
			{
				testForDoc.getTypesTogoogleDriveIDMap().put(f.getFileExtension(), f.getId());
				if(f.getFileExtension().contentEquals("txt"))
				{

					String text=DocHelper.getTextData(f, gDrive);
					//System.out.println("Added File txt of:" + f.getOriginalFilename() + " with size:" + f.getFileSize());
					Text t=new Text(text);
					testForDoc.setExtractedText(t);
				}
	
				mgr.makePersistent(testForDoc);
			}
		
		}
		System.out.println("NumFilesAttempted:"+numFilesAttempt);
		System.out.println("NumFilesInStorage:"+numFilesInStorage);
		System.out.println("NumFilesAdded:"+numNewFiles);
	
		
	}
	
	public static Document getNextDocument(DocumentBatch batchInput,SortStrategy strategy,String currentID)
	{
		//TODO: clunkmode
		DocumentEndpoint docEndpoint=new DocumentEndpoint();
		ArrayList<String> docIDList=getSortedDocIDs(batchInput);
		
		if(currentID==null)
		{
	
			return docEndpoint.getDocument(docIDList.get(0));
		}
		for(int i=0;i<docIDList.size();i++)
		{
			String thisDocInBatch=docIDList.get(i);
			if(thisDocInBatch.contentEquals(currentID) && i<docIDList.size()-1)
			{
				String correctDocID=docIDList.get(i+1);			
				return docEndpoint.getDocument(correctDocID);	
			}
		}
		System.out.println("Last doc");
		return docEndpoint.getDocument(docIDList.get(0));
	}
	public static Integer getDocInBatchIndex(DocumentBatch batchInput,SortStrategy strategy,String currentID)
	{
		//TODO: clunkmode
		DocumentEndpoint docEndpoint=new DocumentEndpoint();
		ArrayList<String> docIDList=getSortedDocIDs(batchInput);
		
	
		if(currentID==null)
		{
		
			return 0;
		}

		for(int i=0;i<docIDList.size();i++)
		{
			String thisDocInBatch=docIDList.get(i);

			if(thisDocInBatch.contentEquals(currentID))
			{
				return i;
			}
		}
		System.out.println("Not in batch:" + currentID);
		return 0;
	}
	public static Document getPreviousDocument(DocumentBatch batchInput,SortStrategy strategy,String currentID)
	{
		//TODO: clunkmode
		
		ArrayList<String> docIDList=getSortedDocIDs(batchInput);
		DocumentEndpoint docEndpoint=new DocumentEndpoint();
		
		for(int i=0;i<docIDList.size();i++)
		{
			String thisDocInBatch=docIDList.get(i);
			if(thisDocInBatch.contentEquals(currentID) && i>0)
			{
				String correctDocID=docIDList.get(i-1);
			
				return docEndpoint.getDocument(correctDocID);				
			}
		}
		System.out.println("First doc");
		return docEndpoint.getDocument(docIDList.get(0));
	}
	public static String getDocumentText(String docID,Drive googleDrive)
	{
		
		Document d=DocumentHandler.getDocument(docID);
		if(d.getExtractedText()==null)
		{
		    try {
				String googleID=d.getTypesTogoogleDriveIDMap().get("txt");
				Get request = googleDrive.files().get(googleID);
				File file = request.execute();
				String text=DocHelper.getTextData(file, googleDrive);
				d.setExtractedText(new Text(text));
				DocumentEndpoint ep=new DocumentEndpoint();
				ep.updateDocument(d);
			} catch (IOException e) {
				return null;
			}
		}
		return d.getExtractedText().getValue();
    
	}
	public static ArrayList<String> getSortedDocIDs(DocumentBatch batchInput)
	{
	
		
		HashSet<String> docIDs=batchInput.getDocIDCollection();
		ArrayList<String> docIDList=new ArrayList(docIDs);
		Collections.sort(docIDList);
		return docIDList;
	}

	public static Document getDocument(String currentID) {
		
		DocumentEndpoint docEndpoint=new DocumentEndpoint();
		return docEndpoint.getDocument(currentID);	
	}
}
