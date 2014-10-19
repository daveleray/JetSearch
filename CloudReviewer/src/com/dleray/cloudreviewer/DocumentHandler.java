package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.PersistenceManager;

import com.dleray.cloudreviewer.structures.Document;
import com.dleray.cloudreviewer.structures.DocumentBatch;
import com.dleray.cloudreviewer.structures.SortStrategy;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.appengine.api.datastore.Text;

public class DocumentHandler {

	public enum DocTypes{
		TEXT,TIFF,NATIVE,PRODUCED;
	}
	
	public static void processDriveFileList(java.util.List<File> allFiles,java.util.List<File> allFolders,Drive gDrive)
	{
		Integer numFilesAttempt=0;
		Integer numFilesInStorage=0;
		Integer numNewFiles=0;
		PersistenceManager mgr=PMF.get().getPersistenceManager();
		
	
		String textID=null;
		String tiffID=null;
		String nativeID=null;
		String producedID=null;
		for(File f: allFolders)
		{
			if(f.getTitle().contains(DocTypes.TEXT.toString()))
			{
				textID=f.getId();
			}
			if(f.getTitle().contains(DocTypes.TIFF.toString()))
			{
				tiffID=f.getId();
			}
			if(f.getTitle().contains(DocTypes.NATIVE.toString()))
			{
				nativeID=f.getId();
			}
			if(f.getTitle().contains(DocTypes.PRODUCED.toString()))
			{
				producedID=f.getId();
			}
		}
		for(File f: allFiles)
		{
			numFilesAttempt+=1;
			if(f.getMimeType().contains("folder"))
			{
				System.out.println("Skipping:" + f.getTitle());
				continue;
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
				testForDoc.setOriginalExtension(f.getFileExtension());
				testForDoc.setDocumentIdentifier(docIdentifier);
			}
			
			if(isSomeKindOfFile(f, textID))
			{
				String alreadyID=testForDoc.getTypesTogoogleDriveIDMap().get(DocTypes.TEXT.toString());
					//its not here already.  let's add it.
					testForDoc.getTypesTogoogleDriveIDMap().put(DocTypes.TEXT.toString(), f.getId());
					
					//verify F is actually a text file and then get content
					if(f.getFileExtension().contentEquals("txt"))
					{
						if(testForDoc.getExtractedText()==null || testForDoc.getExtractedText().getValue().contentEquals(""))
						{
							System.out.println("Getting text data from:" + f.getOriginalFilename());
							
							String text=DocHelper.getTextData(f, gDrive);
							//System.out.println("Added File txt of:" + f.getOriginalFilename() + " with size:" + f.getFileSize());
							Text t=new Text(text);
							testForDoc.setExtractedText(t);
						}
					
					}

			}
			else if (isSomeKindOfFile(f, tiffID))
			{
				String alreadyID=testForDoc.getTypesTogoogleDriveIDMap().get(DocTypes.TIFF.toString());

					//its not here already.  let's add it.
					testForDoc.getTypesTogoogleDriveIDMap().put(DocTypes.TIFF.toString(), f.getId());

			}
			else if (isSomeKindOfFile(f, nativeID))
			{
				String alreadyID=testForDoc.getTypesTogoogleDriveIDMap().get(DocTypes.NATIVE.toString());
				testForDoc.setOriginalExtension(f.getFileExtension());


					testForDoc.getTypesTogoogleDriveIDMap().put(DocTypes.NATIVE.toString(), f.getId());

			}
			else if (isSomeKindOfFile(f, producedID))
			{
				String alreadyID=testForDoc.getTypesTogoogleDriveIDMap().get(DocTypes.PRODUCED.toString());
					//its not here already.  let's add it.
					testForDoc.getTypesTogoogleDriveIDMap().put(DocTypes.PRODUCED.toString(), f.getId());

			}
			mgr.makePersistent(testForDoc);
		
		}
		System.out.println("NumFilesAttempted:"+numFilesAttempt);
		System.out.println("NumFilesInStorage:"+numFilesInStorage);
		System.out.println("NumFilesAdded:"+numNewFiles);
	
		
	}
	
	public static Document getDocByID(String id)
	{
		PersistenceManager pm=PMF.get().getPersistenceManager();
		Document document=pm.getObjectById(Document.class,id);
		
		if(document.getMetadataAndValuesNonSearchable()==null)
		{
			document.setMetadataAndValuesNonSearchable(new HashMap<String,Text>());
		}
		if(document.getMetadataAndValuesSearchable()==null)
		{
			document.setMetadataAndValuesSearchable(new HashMap<String,String>());
		}
		Set<String> force=document.getMetadataAndValuesNonSearchable().keySet();
		Set<String> force2=document.getMetadataAndValuesSearchable().keySet();
		for(String s: force)
		{
			document.getMetadataAndValuesNonSearchable().get(s);
		}
		for(String s: force2)
		{
			document.getMetadataAndValuesSearchable().get(s);
		}
		

		pm.close();
		return document;
	}
	public static Document getNextDocument(DocumentBatch batchInput,SortStrategy strategy,String currentID)
	{
		//TODO: clunkmode
		
		
		ArrayList<String> docIDList=getSortedDocIDs(batchInput);
		
		if(currentID==null)
		{
		
			return getDocByID(docIDList.get(0));
		}
		for(int i=0;i<docIDList.size();i++)
		{
			String thisDocInBatch=docIDList.get(i);
			if(thisDocInBatch.contentEquals(currentID) && i<docIDList.size()-1)
			{
				String correctDocID=docIDList.get(i+1);			
				return getDocByID(correctDocID);
			}
		}
		System.out.println("Last doc");
		return getDocByID(docIDList.get(docIDList.size()-1));
	}
	private static Boolean isSomeKindOfFile(File file,String kindOfFileDirectory)
	{
		for(ParentReference r: file.getParents())
		{
			if(r.getId().contentEquals(kindOfFileDirectory))
			{
				return true;
			}
		}
		return false;
	}
	public static Integer getDocInBatchIndex(DocumentBatch batchInput,SortStrategy strategy,String currentID)
	{
		//TODO: clunkmode

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

		
		for(int i=0;i<docIDList.size();i++)
		{
			String thisDocInBatch=docIDList.get(i);
			if(thisDocInBatch.contentEquals(currentID) && i>0)
			{
				String correctDocID=docIDList.get(i-1);
			
				return getDocByID(correctDocID);			
			}
		}
		System.out.println("First doc");
		return getDocByID(docIDList.get(0));
	}
	public static String getDocumentText(String docID,Drive googleDrive)
	{
		PersistenceManager pm=PMF.get().getPersistenceManager();
		Document d=pm.getObjectById(Document.class,docID);
		if(d!=null && d.getExtractedText()==null)
		{
		    try {
				String googleID=d.getTypesTogoogleDriveIDMap().get("txt");
				Get request = googleDrive.files().get(googleID);
				File file = request.execute();
				String text=DocHelper.getTextData(file, googleDrive);	
				d.setExtractedText(new Text(text));		
				pm.makePersistent(d);
				pm.close();
			} catch (IOException e) {
				return null;
			}
		}
		String output=d.getExtractedText().getValue();
		pm.close();
		return output;
    
	}
	public static ArrayList<String> getSortedDocIDs(DocumentBatch batchInput)
	{
	
		HashSet<String> docIDs=batchInput.getDocIDCollection();
		ArrayList<String> docIDList=new ArrayList(docIDs);
		Collections.sort(docIDList);
		return docIDList;
	}

	public static Document getDocument(String currentID) {
		
		return getDocByID(currentID);
	}
}
