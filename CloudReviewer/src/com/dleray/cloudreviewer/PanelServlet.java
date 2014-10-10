package com.dleray.cloudreviewer;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.TaggingPanel;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
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
/*		System.out.println("got user");
        TaggingPanelEndpoint tagPanelEndpoint=new TaggingPanelEndpoint();
        TaggingPanel panel;
        
        String panelID;
        if(req.getParameter("panelid")==null || req.getParameter("panelid").contentEquals("undefined"))
        {
        	panelID=progUser.getCurrentPanelID();
        }
        else
        {
        	panelID=req.getParameter("panelid");
        	progUser.setCurrentPanelID(panelID);
        	PersistenceManager pm=PMF.get().getPersistenceManager();
        	pm.makePersistent(progUser);
        	pm.close();
        }
		try {
			panel = tagPanelEndpoint.getTaggingPanel(progUser.getCurrentPanelID());
		} catch (Exception e) {*/
			Long defaultID=PanelHandler.initDefaultPanel();
			Key k=KeyFactory.createKey(TaggingPanel.class.getSimpleName(), defaultID);
			System.out.println("Default Panel ID:" + defaultID);
			progUser.setCurrentPanelID(defaultID+"");
			PersistenceManager pm=PMF.get().getPersistenceManager();
			pm.makePersistent(progUser);
			
			TaggingPanel panel = pm.getObjectById(TaggingPanel.class,k);
		//}
			pm.close();
        
    	resp.setContentType("application/json");
    	Gson gson=new Gson();
    	String sending=gson.toJson(panel.toClient());
  
		resp.getWriter().println(sending);
		System.out.println("\tRESPONSE:"+sending);
    	return;
	
	}



}
