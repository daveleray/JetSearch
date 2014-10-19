package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.HighlightingList;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.gson.Gson;

public class HighlightingServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("Highlighting Get");
	      if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
	        {
	        	System.out.println("logging in from:" + req.getUserPrincipal());
	        	resp.setContentType("text/plain");
	    		resp.getWriter().println("PLEASE SIGN IN");
	        	return;
	        }
	        CloudReviewerUser progUser=UserHandler.getCurrentUser();
			System.out.println("got user");
	      
	        CollectionResponse<HighlightingList> lists;
		
	        PersistenceManager mgr=PMF.get().getPersistenceManager();
			Query query = mgr.newQuery(HighlightingList.class);
			List<HighlightingList> options = (List<HighlightingList>) query.execute();
				

	        
	        
	    	resp.setContentType("application/json");
	    	Gson gson=new Gson();
	    	String sending=gson.toJson(options);
	  
			resp.getWriter().println(sending);
			System.out.println("\tRESPONSE IS SIZE " + options.size() + " HIGHLIGHT LISTS");
	    	return;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
	}

}
