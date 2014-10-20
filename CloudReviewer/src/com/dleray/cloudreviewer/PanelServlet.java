package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.responses.ClientTaggingControl;
import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagList;
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

		PersistenceManager pm=PMF.get().getPersistenceManager();
		ArrayList<ClientTaggingControl> output=new ArrayList();
		
		HashSet<String> toRemove=new HashSet();
		for(String s: progUser.getActiveTaggingPanels())
		{
			try {
				TagList tagList=pm.getObjectById(TagList.class,Long.parseLong(s));
				output.add(tagList.toClient());
			} catch (Exception e) {
				System.out.println("something went wrong with panel:" + s);
				toRemove.add(s);
			}
		}
		for(String s: toRemove)
		{
			progUser.getActiveTaggingPanels().remove(s);
		}
		pm.makePersistent(progUser);
		//}
			pm.close();
        
    	resp.setContentType("application/json");
    	Gson gson=new Gson();
    	String sending=gson.toJson(output);
  
		resp.getWriter().println(sending);
		System.out.println("\tRESPONSE:"+sending);
    	return;
	
	}



}
