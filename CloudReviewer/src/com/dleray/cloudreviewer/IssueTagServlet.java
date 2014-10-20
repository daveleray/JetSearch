package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.responses.ClientIssueTag;
import com.dleray.cloudreviewer.structures.BatchFolder;
import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.IssueTag;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagList;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.gson.Gson;


public class IssueTagServlet extends HttpServlet {

	@Override
	/** get issue tags associated with current project
	 * 
	 * @author David
	 *
	 */
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
	        progUser.setActiveProject("imsquinn@gmail.com");
	        
		List<IssueTag> output=PMFManager.getIssueTagsForProject(progUser.getActiveProject());
		
		ArrayList<ClientIssueTag> toClient=new ArrayList();
		for(IssueTag t: output)
		{
			toClient.add(t.toClientIssueTag());
		}
	  	resp.setContentType("application/json");
    	Gson gson=new Gson();
    	String sending=gson.toJson(toClient);
		resp.getWriter().println(sending);
    	return;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		long taglistid=Long.parseLong(req.getParameter("taglistid"));
		String issueid=req.getParameter("issueid");
		 String status=req.getParameter("status");
		 PersistenceManager pm=PMF.get().getPersistenceManager();
		 System.out.println("issue toggle request:" + taglistid + " / " + issueid + " / " + status);
		 TagList output=pm.getObjectById(TagList.class,taglistid);
		 if(output.getIssueTagIDs().contains(issueid) && status.contentEquals("off"))
		 {
			 output.getIssueTagIDs().remove(issueid);
			 pm.makePersistent(output);
		 }
		 else if(!output.getIssueTagIDs().contains(issueid) && status.contentEquals("on"))
		 {
			 output.getIssueTagIDs().add(issueid);
			 pm.makePersistent(output);
		 }
		 
		pm.close();
	}

}
