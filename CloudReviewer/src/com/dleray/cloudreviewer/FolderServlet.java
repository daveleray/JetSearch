package com.dleray.cloudreviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.endpoints.BatchFolderEndpoint;
import com.dleray.cloudreviewer.endpoints.DocumentBatchEndpoint;
import com.dleray.cloudreviewer.responses.ClientFolderStructure;
import com.dleray.cloudreviewer.structures.BatchFolder;
import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.DocumentBatch;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.gson.Gson;

public class FolderServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		
	
		   if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
	        {
	        	System.out.println("logging in from:" + req.getUserPrincipal());
	        	resp.setContentType("text/plain");
	    		resp.getWriter().println("PLEASE SIGN IN");
	        	return;
	        }
		System.out.println("Request for folders from:" + req.getUserPrincipal().getName());  
		CloudReviewerUser user=UserHandler.getCurrentUser();
		
		BatchFolderEndpoint batchEndpoint=new BatchFolderEndpoint();
		CollectionResponse<BatchFolder> output=batchEndpoint.listBatchFolder("", 1000);
		
		DocumentBatchEndpoint batchDocEndpoint=new DocumentBatchEndpoint();
		CollectionResponse<DocumentBatch> thisBatch=batchDocEndpoint.listDocumentBatch("", 1000);
		
		BatchFolder root=batchEndpoint.getBatchFolder("root");
		ClientFolderStructure toSend=root.toClient(output.getItems(), thisBatch.getItems());
		
		
		resp.setContentType("application/json");
    	Gson gson=new Gson();
    	String sending=gson.toJson(toSend);
  
		resp.getWriter().println(sending);
		//System.out.println("\tFOLDER RESPONSE:"+sending);

    	return;
	
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
		String parentFolder = paramMap.get("parent_folderid");
		String folderName=paramMap.get("foldername");
		String folderID = parentFolder+folderName;

		BatchFolder folderToStore=new BatchFolder();
		folderToStore.setBatchFolderID(folderID);
		folderToStore.setDisplayName(folderName);
		folderToStore.setParentFolderID(parentFolder);

		BatchFolderEndpoint endpoint=new BatchFolderEndpoint();
		endpoint.insertBatchFolder(folderToStore);
		System.out.println("Successfully created folder:" + folderName + " in folder:"+parentFolder);
		
	}
	
	

}
