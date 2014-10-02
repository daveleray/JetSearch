package com.dleray.cloudreviewer;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.endpoints.IssueTagEndpoint;
import com.dleray.cloudreviewer.structures.IssueTag;

public class AddIssueServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		String displayName=req.getParameter("displayName");
		String subCategoryDisplay=req.getParameter("subCategoryDisplay");
		String categoryDisplay=req.getParameter("categoryDisplay");
		
	
		IssueTag newTag=new IssueTag();
		newTag.setDisplayName(displayName);
		newTag.setSubCategoryDisplay(subCategoryDisplay);
		newTag.setCategoryDisplay(categoryDisplay);
		
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
