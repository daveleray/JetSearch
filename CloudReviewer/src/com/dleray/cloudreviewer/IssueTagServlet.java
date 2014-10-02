package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.endpoints.IssueTagEndpoint;
import com.dleray.cloudreviewer.responses.ClientIssueTag;
import com.dleray.cloudreviewer.structures.IssueTag;
import com.dleray.cloudreviewer.structures.TaggingPanel;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagList;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.gson.Gson;

public class IssueTagServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		IssueTagEndpoint endpoint=new IssueTagEndpoint();
		CollectionResponse<IssueTag> output=endpoint.listIssueTag("", 1000);
		ArrayList<ClientIssueTag> toClient=new ArrayList();
		for(IssueTag t: output.getItems())
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
		
		String taglistid=req.getParameter("taglistid");
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
