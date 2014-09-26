package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.util.ajax.JSON;

import com.dleray.cloudreviewer.responses.DocJSONResponse;
import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.Document;
import com.dleray.cloudreviewer.structures.DocumentBatch;
import com.dleray.cloudreviewer.structures.UserTag;
import com.dleray.cloudreviewer.structures.UserTagEndpoint;
import com.google.gson.Gson;

public class GetNextDocServlet extends HttpServlet  {


	  
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
        
        System.out.println("GETNEXT DOC   "+ "USER:" + req.getUserPrincipal());

        if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
        {
        	System.out.println("logging in from:" + req.getUserPrincipal());
        	resp.setContentType("text/plain");
    		resp.getWriter().println("PLEASE SIGN IN");
        	return;
        }
        System.out.println("PROCESSING");
        CloudReviewerUser user=UserHandler.getCurrentUser();
        String currentID=req.getParameter("currentDocID");
        if(currentID==null || currentID.contentEquals("undefined"))
        {
        	currentID=user.getCurrentDoc();
        }
        System.out.println("Got past here");
        DocumentBatch batch=BatchHandler.getDefaultBatch();
        System.out.println("Got past here");
        Document d=DocumentHandler.getNextDocument(BatchHandler.getDefaultBatch(), null, currentID);
       
        DocJSONResponse response=DocHelper.getJSONResponse(d, batch);
    	resp.setContentType("application/json");
    	Gson gson=new Gson();
    	String sending=gson.toJson(response);
		resp.getWriter().println(sending);
		System.out.println("\tRESPONSE:"+sending);
    	return;
	}
	

	




}
