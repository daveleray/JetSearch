package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.responses.ClientFolderStructure;
import com.dleray.cloudreviewer.structures.BatchFolder;
import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.DocumentBatch;
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
		   
		String nodeID=req.getParameter("id");
		System.out.println("Request for folders from:" + req.getUserPrincipal().getName());  
		CloudReviewerUser user=UserHandler.getCurrentUser();
		
	
		PersistenceManager pm=PMF.get().getPersistenceManager();
		Query q = pm.newQuery(BatchFolder.class);
		
		List<BatchFolder> output=(List<BatchFolder>) q.execute();

		
		Query q2 = pm.newQuery(DocumentBatch.class);
		List<DocumentBatch> output2=(List<DocumentBatch>) q2.execute();
		

		
		BatchFolder root=pm.getObjectById(BatchFolder.class,"root");
		
		ClientFolderStructure toSend=root.toClient(output,output2);
		

		
		
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
//	    StringBuilder buffer = new StringBuilder();
//	    BufferedReader reader = req.getReader();
//	    String line;
//	    while ((line = reader.readLine()) != null) {
//	        buffer.append(line);
//	    }
//	    String data = buffer.toString();
//	    String[] params=data.split("&");
//	    HashMap<String,String> paramMap=new HashMap();
//	    for(String p : params)
//	    {
//	    	String[] pairing=p.split("=");
//	    	if(pairing.length>1)
//	    	{
//	    		paramMap.put(pairing[0],pairing[1]);
//	    	}
//	    	
//	    }
		String parentFolder =req.getParameter("parent_folderid");
		String folderName=req.getParameter("foldername");
		String folderID = parentFolder+folderName;

		BatchFolder folderToStore=new BatchFolder();
		folderToStore.setBatchFolderID(folderID);
		folderToStore.setDisplayName(folderName);
		folderToStore.setParentFolderID(parentFolder);

		PersistenceManager pm=PMF.get().getPersistenceManager();
		pm.makePersistent(folderToStore);
		pm.close();
		System.out.println("Successfully created folder:" + folderName + " in folder:"+parentFolder);
		
	}
	
	

}
