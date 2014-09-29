package com.dleray.cloudreviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.endpoints.DocumentBatchEndpoint;
import com.dleray.cloudreviewer.endpoints.DocumentEndpoint;
import com.dleray.cloudreviewer.responses.ClientBatchMetadata;
import com.dleray.cloudreviewer.responses.DocJSONResponse;
import com.dleray.cloudreviewer.responses.ClientBatchMetadata.ClientBatchMetadataRow;
import com.dleray.cloudreviewer.structures.Document;
import com.dleray.cloudreviewer.structures.DocumentBatch;
import com.google.gson.Gson;

public class BatchServlet extends HttpServlet{

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		 String selectedFolder=req.getParameter("batch");
		 DocumentBatchEndpoint endpoint=new DocumentBatchEndpoint();
		 DocumentBatch batch=endpoint.getDocumentBatch(selectedFolder);
		 ArrayList<String> docIDs=DocumentHandler.getSortedDocIDs(batch);
		 
		 ClientBatchMetadata output=new ClientBatchMetadata();
		 output.setAssignedUser(batch.getUserAssigned());
		 output.setBatchID(batch.getDocbatchID());
		 output.setDisplayName(batch.getBatchName());
		 
		 HashSet<String> allHeaders=new HashSet();
		 
		 DocumentEndpoint docendpoint=new DocumentEndpoint();
		 ArrayList<Document> allDocs=new ArrayList();
		 for(String doc: docIDs)
		 {
			 Document d=docendpoint.getDocument(doc);
			 allDocs.add(d);
			 for(String s: d.getMetadataAndValuesNonSearchable().keySet())
			 {
				 allHeaders.add(s);
			 }
			 for(String s: d.getMetadataAndValuesSearchable().keySet())
			 {
				 allHeaders.add(s);
			 }
		 }
		 ArrayList<String> sortedHeaders=new ArrayList(allHeaders);
		 Collections.sort(sortedHeaders);
		
		 
		 
		 
		 ArrayList<ClientBatchMetadataRow> outputRows=new ArrayList();
		 Integer num=0;
		 for(Document d: allDocs)
		 {
			 num+=1;
			 ClientBatchMetadataRow thisRow=output.getRowInstance();
			 ArrayList<String> info=new ArrayList();
			 info.add(num+"");
			 info.add(d.getDocumentIdentifier());
			 for(String s: sortedHeaders)
			 {
				 if(d.getMetadataAndValuesSearchable().keySet().contains(s))
				 {
					 info.add(d.getMetadataAndValuesSearchable().get(s));
				 }
				 else if(d.getMetadataAndValuesNonSearchable().keySet().contains(s))
				 {
					 info.add(d.getMetadataAndValuesNonSearchable().get(s).getValue());
				 }
				 else
				 {
					 info.add("");
				 }
			 }
			 thisRow.setInfo(info);
			 outputRows.add(thisRow);
		 }
		 sortedHeaders.add(0,"ID");
		 sortedHeaders.add(0,"#");
		 output.setHeaders(sortedHeaders);
		 output.setRows(outputRows);
	
	    	resp.setContentType("application/json");
	    	Gson gson=new Gson();
	    	String sending=gson.toJson(output);
	    	resp.getWriter().println(sending);
			System.out.println("\tRESPONSE:"+sending);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		   // Read from request
	    StringBuilder buffer = new StringBuilder();
	    BufferedReader reader = req.getReader();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        buffer.append(line);
	    }
	    String data = buffer.toString();
	    String[] params=data.split("&");
	    HashMap<String,String> paramMap=new HashMap();
	    for(String p : params)
	    {
	    	String[] pairing=p.split("=");
	    	if(pairing.length>1)
	    	{
	    		paramMap.put(pairing[0],pairing[1]);
	    	}
	    	
	    }
		String batchName = paramMap.get("batchname");
		String folderID=paramMap.get("folderid");
		String docbatchID = batchName+folderID;
		
		String alldocs=paramMap.get("docids");
		String[] enterParsedDocs=alldocs.split("%0D%0A");
		String[] commaParsedDocs=alldocs.split(",");
		
		HashSet<String> docIDCollection=new HashSet();
		if(enterParsedDocs.length>1)
		{
			for(String s: enterParsedDocs)
			{
				docIDCollection.add(s);
			}
		}
		else
		{
			for(String s: commaParsedDocs)
			{
				docIDCollection.add(s);
			}
		}
		System.out.println("CREATING NEW BATCH WITH:" + docIDCollection.size() + " DOCUMENTS");
		
		
		DocumentBatch documentbatch=new DocumentBatch();
		documentbatch.setBatchName(batchName);
		documentbatch.setDocbatchID(docbatchID);
		documentbatch.setDocIDCollection(docIDCollection);
		documentbatch.setFolderID(folderID);
		
		DocumentBatchEndpoint batchEndpoint=new DocumentBatchEndpoint();
		batchEndpoint.insertDocumentBatch(documentbatch);
		System.out.println("Successfully created:" + batchName + " in folder:"+folderID);

	}

}
