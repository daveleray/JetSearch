package com.dleray.cloudreviewer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.endpoints.CloudReviewerUserEndpoint;
import com.dleray.cloudreviewer.endpoints.TaggingPanelEndpoint;
import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.TaggingPanel;
import com.google.gson.Gson;

public class PanelServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        System.out.println("GETPANEL "+ "USER:" + req.getUserPrincipal());

        if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
        {
        	System.out.println("logging in from:" + req.getUserPrincipal());
        	resp.setContentType("text/plain");
    		resp.getWriter().println("PLEASE SIGN IN");
        	return;
        }
        CloudReviewerUser progUser=UserHandler.getCurrentUser();
		System.out.println("got user");
        TaggingPanelEndpoint tagPanelEndpoint=new TaggingPanelEndpoint();
        TaggingPanel panel;
		try {
			panel = tagPanelEndpoint.getTaggingPanel(progUser.getCurrentPanelID());
		} catch (Exception e) {
			PanelHandler.initDefaultPanel();
			panel = tagPanelEndpoint.getTaggingPanel(progUser.getCurrentPanelID());
		}
        
        
    	resp.setContentType("application/json");
    	Gson gson=new Gson();
    	String sending=gson.toJson(panel.toClient());
  
		resp.getWriter().println(sending);
		System.out.println("\tRESPONSE:"+sending);
    	return;
	
	}



}
