package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.responses.ClientTagList;
import com.dleray.cloudreviewer.responses.TaggingControlsResponse;
import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagList;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;

public class TaggingControlsServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("getting TaggingControls");
		
		   if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
	        {
	        	System.out.println("logging in from:" + req.getUserPrincipal());
	        	resp.setContentType("text/plain");
	    		resp.getWriter().println("PLEASE SIGN IN");
	        	return;
	        }
		CloudReviewerUser user=PMFManager.getUser();   
		ArrayList<ClientTagList> toClient=new ArrayList();
		HashMap<String,Boolean> tagListStatus=new HashMap();
		 user.setActiveProject("imsquinn@gmail.com");
		 System.out.println("user active project:" + user.getActiveProject());
		 
		for(TagList t:PMFManager.getTaggingPanelsForProject(user.getActiveProject()))
		{
			toClient.add(t.toClient());
			if(user.getActiveTaggingPanels().contains(t.getControlID()+""))
			{
				tagListStatus.put(t.getControlID()+"", true);
			}
			else
			{
				tagListStatus.put(t.getControlID()+"", false);
			}
		}
		
		TaggingControlsResponse output=new TaggingControlsResponse();
		output.getTaglists().addAll(toClient);
		
		output.setTagListStatus(tagListStatus);
		
		
	  	resp.setContentType("application/json");
    	Gson gson=new Gson();
    	String sending=gson.toJson(output);
		resp.getWriter().println(sending);
    	return;
	}

	@Override
	/** add or remove a taglist from the current user
	 * 
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String taglistid=		 req.getParameter("taglistid");
		 String status=req.getParameter("status");
		 
		 PersistenceManager pm=PMF.get().getPersistenceManager();
		 

		  UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();
		CloudReviewerUser output=pm.getObjectById(CloudReviewerUser.class,user.getEmail());
		

		 if(status.contentEquals("off"))
		 {
			 output.getActiveTaggingPanels().remove(taglistid);
			 pm.makePersistent(output);
		 }
		 else if(status.contentEquals("on"))
		 {
			 output.getActiveTaggingPanels().add(taglistid);
			 pm.makePersistent(output);
		 }
		pm.close();
	}

}
