package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.responses.DocJSONResponse;
import com.dleray.cloudreviewer.structures.Document;
import com.dleray.cloudreviewer.structures.DocumentBatch;
import com.dleray.cloudreviewer.structures.UserTag;
import com.dleray.cloudreviewer.structures.UserTagEndpoint;
import com.google.gson.Gson;

public class GetPreviousDocServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
        
		   System.out.println("GETPREV DOC"+ "USER:" + req.getUserPrincipal());
        if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
        {
        	resp.setContentType("text/plain");
    		resp.getWriter().println("PLEASE SIGN IN");
        	return;
        }
        
    
        String currentID=req.getParameter("currentDocID");
        if(currentID==null || currentID.contentEquals("undefined"))
        {
        	currentID=null;
        }
        DocumentBatch batch=BatchHandler.getDefaultBatch();
        Document d=DocumentHandler.getPreviousDocument(batch, null, currentID);
        DocJSONResponse response=DocHelper.getJSONResponse(d, batch);
    	resp.setContentType("application/json");
    	Gson gson=new Gson();
    	String sending=gson.toJson(response);
  
		resp.getWriter().println(sending);
		System.out.println("\tRESPONSE:"+sending);
    	return;
	}
	
}
