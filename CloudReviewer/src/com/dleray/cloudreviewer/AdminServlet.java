package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.structures.CloudReviewerProject;
import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.google.gson.Gson;

public class AdminServlet extends HttpServlet {

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
		   PersistenceManager pmf=PMF.get().getPersistenceManager();
		   CloudReviewerUser user=pmf.getObjectById(CloudReviewerUser.class,req.getUserPrincipal().getName());
		   CloudReviewerProject proj=pmf.getObjectById(CloudReviewerProject.class,user.getActiveProject());
		   Set<String> accessLevels=proj.getUserToPermissionsMap().keySet();
		   
			resp.setContentType("application/json");
	    	Gson gson=new Gson();
	    	String sending=gson.toJson(proj.toAdminPanel());
	  
			resp.getWriter().println(sending);
			pmf.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PersistenceManager pm=PMF.get().getPersistenceManager();
		Query q = pm.newQuery(CloudReviewerUser.class);
		
		List<CloudReviewerUser> output=(List<CloudReviewerUser>) q.execute();
		
		for(CloudReviewerUser u: output)
		{
			u.setActiveProject("imsquinn@gmail.com");
		}
		pm.close();
	}

}
