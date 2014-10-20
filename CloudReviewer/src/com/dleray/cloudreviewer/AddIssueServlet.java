package com.dleray.cloudreviewer;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.IssueTag;

public class AddIssueServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("Issue Tags Get");
	      if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
	        {
	        	System.out.println("logging in from:" + req.getUserPrincipal());
	        	resp.setContentType("text/plain");
	    		resp.getWriter().println("PLEASE SIGN IN");
	        	return;
	        }
	        CloudReviewerUser progUser=UserHandler.getCurrentUser();
	        
		String displayName=req.getParameter("displayName");

		
	
		IssueTag newTag=new IssueTag();
		newTag.setDisplayName(displayName);
		newTag.setProjectID(progUser.getActiveProject());
		
		try {
			PersistenceManager pm=PMF.get().getPersistenceManager();
			pm.makePersistent(newTag);
			System.out.println("added:" + newTag.getId());
			pm.close();
		} catch (Exception e) {
			System.out.println("unable to add:" + newTag.getId());
		}
		
	}

}
