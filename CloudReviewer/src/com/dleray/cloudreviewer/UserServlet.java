package com.dleray.cloudreviewer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.CloudReviewerUserEndpoint;

public class UserServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
	     if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
	        {
	        	System.out.println("logging in from:" + req.getUserPrincipal());
	        	resp.setContentType("text/plain");
	    		resp.getWriter().println("PLEASE SIGN IN");
	        	return;
	        }
	     
        String panelID=req.getParameter("panelID");
        
        CloudReviewerUser user=new CloudReviewerUser();
        user.setUserEmail(req.getUserPrincipal().getName());
        user.setCurrentPanelID(panelID);
        CloudReviewerUserEndpoint endpoint=new CloudReviewerUserEndpoint();
        try {
			endpoint.insertCloudReviewerUser(user);
		} catch (Exception e) {
			endpoint.updateCloudReviewerUser(user);
		}
        
	}
}
