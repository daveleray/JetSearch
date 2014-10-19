package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.HighlightingList;

public class SettingsServlet extends HttpServlet {


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
        
        System.out.println("CHANGE SETTINGS   "+ "USER:" + req.getUserPrincipal());

        if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
        {
        	System.out.println("logging in from:" + req.getUserPrincipal());
        	resp.setContentType("text/plain");
    		resp.getWriter().println("PLEASE SIGN IN");
        	return;
        }
        
		PersistenceManager pm=PMF.get().getPersistenceManager();
		CloudReviewerUser userFromDB=pm.getObjectById(CloudReviewerUser.class,req.getUserPrincipal().getName());
		if(userFromDB==null)
		{
			System.out.println("creating new clouduser");
			userFromDB=new CloudReviewerUser();
			userFromDB.setUserEmail(req.getUserPrincipal().getName());
		}
		String wants_inverse=req.getParameter("invertemails");
		if(wants_inverse.contentEquals("true"))
		{
			userFromDB.setInvertEmails(true);
		}
		else
		{
			userFromDB.setInvertEmails(false);
		}
		pm.makePersistent(userFromDB);
		//pm.makePersistent(user);
		
		
		pm.close();
		System.out.println("Updated email inversion");
		return;
		
	}

}
