package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.responses.ClientTaggingControl;
import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagList;
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


		
			List<TagList> output=PMFManager.getTaggingPanelsForProject(progUser.getActiveProject());
			ArrayList<ClientTaggingControl> options=new ArrayList();
			for(TagList p: output)
			{
				options.add(p.toClient());
			}
	        
	        
	    	resp.setContentType("application/json");
	    	Gson gson=new Gson();
	    	String sending=gson.toJson(options);
	  
			resp.getWriter().println(sending);
	    	return;
	}

}
