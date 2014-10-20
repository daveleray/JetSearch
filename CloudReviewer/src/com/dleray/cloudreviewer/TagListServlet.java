package com.dleray.cloudreviewer;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.responses.ClientTagList;
import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagList;
import com.google.gson.Gson;

public class TagListServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		long tagListID=Long.parseLong(req.getParameter("id"));
		 PersistenceManager pm=PMF.get().getPersistenceManager();
		 TagList thisList=pm.getObjectById(TagList.class,tagListID);
		 
		 ClientTagList output=thisList.toClient();
			
			resp.setContentType("application/json");
	    	Gson gson=new Gson();
	    	String sending=gson.toJson(output);
	  
			resp.getWriter().println(sending);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		CloudReviewerUser user=PMFManager.getUser();
		
	 String tagListName=req.getParameter("controlname");
	 PersistenceManager pm=PMF.get().getPersistenceManager();
		 
	 TagList tagList=new TagList();
		tagList.setDisplayName(tagListName);
		tagList.setProjectID(user.getActiveProject());
		pm.makePersistent(tagList);
		
		resp.getWriter().println(tagList.getControlID());
		 
	}

}
