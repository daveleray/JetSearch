package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.responses.ClientFolderStructure;
import com.dleray.cloudreviewer.structures.BatchFolder;
import com.dleray.cloudreviewer.structures.BatchFolderEndpoint;
import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.DocumentBatch;
import com.dleray.cloudreviewer.structures.DocumentBatchEndpoint;
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
		System.out.println("\tRESPONSE:"+sending);
    	return;
	
	}

}
