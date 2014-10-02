package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.endpoints.TaggingPanelEndpoint;
import com.dleray.cloudreviewer.responses.ClientTaggingPanel;
import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.TaggingPanel;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.gson.Gson;

public class PanelOptionsServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		System.out.println("PanelOptions Get");
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
	        CollectionResponse<TaggingPanel> panels;
		
			panels = tagPanelEndpoint.listTaggingPanel("", 1000);
				
			ArrayList<ClientTaggingPanel> options=new ArrayList();
			for(TaggingPanel p: panels.getItems())
			{
				options.add(p.toClient());
			}
	        
	        
	    	resp.setContentType("application/json");
	    	Gson gson=new Gson();
	    	String sending=gson.toJson(options);
	  
			resp.getWriter().println(sending);
			System.out.println("\tRESPONSE IS SIZE " + options.size() + " PANELS");
	    	return;
	}

}
