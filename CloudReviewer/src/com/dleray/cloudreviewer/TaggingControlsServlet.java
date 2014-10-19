package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.responses.ClientTagList;
import com.dleray.cloudreviewer.structures.TaggingPanel;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagList;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.gson.Gson;

public class TaggingControlsServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ArrayList<ClientTagList> toClient=new ArrayList();
		for(TagList t:PMFManager.getAllTagLists())
		{
			toClient.add(t.toClient());
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
		
		String taglistid=		 req.getParameter("taglistid");
		String panelid=req.getParameter("panelid");
		 String status=req.getParameter("status");
		 PersistenceManager pm=PMF.get().getPersistenceManager();
		 
		 TaggingPanel output=pm.getObjectById(TaggingPanel.class,panelid);
		 if(output.getTaggingControlIds().contains(taglistid) && status.contentEquals("off"))
		 {
			 output.getTaggingControlIds().remove(taglistid);
			 pm.makePersistent(output);
		 }
		 else if(!output.getTaggingControlIds().contains(taglistid) && status.contentEquals("on"))
		 {
			 output.getTaggingControlIds().add(taglistid);
			 pm.makePersistent(output);
		 }
		pm.close();
	}

}
