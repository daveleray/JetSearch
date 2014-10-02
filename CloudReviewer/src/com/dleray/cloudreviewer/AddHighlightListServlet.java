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
import com.dleray.cloudreviewer.structures.HighlightingListEndpoint;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;

public class AddHighlightListServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		  System.out.println("GETNEXT DOC   "+ "USER:" + req.getUserPrincipal());

	        if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
	        {
	        	System.out.println("logging in from:" + req.getUserPrincipal());
	        	resp.setContentType("text/plain");
	    		resp.getWriter().println("PLEASE SIGN IN");
	        	return;
	        }
	        Long id=Long.parseLong(req.getParameter("id"));
	    	PersistenceManager pm=PMF.get().getPersistenceManager();
	    	HighlightingList list=pm.getObjectById(HighlightingList.class,id);
	    	
	    	resp.setContentType("application/json");
	    	Gson gson=new Gson();
	    	String sending=gson.toJson(list.toClient());
	  
			resp.getWriter().println(sending);
			System.out.println("\tRESPONSE:"+sending);
	    	return;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
        
        System.out.println("GETNEXT DOC   "+ "USER:" + req.getUserPrincipal());

        if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
        {
        	System.out.println("logging in from:" + req.getUserPrincipal());
        	resp.setContentType("text/plain");
    		resp.getWriter().println("PLEASE SIGN IN");
        	return;
        }
        
		PersistenceManager pm=PMF.get().getPersistenceManager();

		String color=req.getParameter("color");
		String name=req.getParameter("name");
		String kw=req.getParameter("kw");
		String id=req.getParameter("id");
		HighlightingList list=new HighlightingList();
		if(color==null)
		{
			list.setHexColor("ffff00");
			list.setHighlightListDisplayName("default");
			list.setKeywords(new ArrayList());
		}
		else
		{
			list=pm.getObjectById(HighlightingList.class,Long.parseLong(id));
			list.setHexColor(color);
			list.setHighlightListDisplayName(name);
			String[] allKW=kw.split("\n");
			ArrayList<String> newKW=new ArrayList();
			for(String s: allKW)
			{
				newKW.add(s);
			}
			list.setKeywords(newKW);
			
		}
		
	
		
		String userEmail=req.getUserPrincipal().getName();
		CloudReviewerUser user=pm.getObjectById(CloudReviewerUser.class,userEmail);
		user.getHighlightingIDs().add(list.getId()+"");
		pm.makePersistent(list);
		//pm.makePersistent(user);
		
		resp.getWriter().println(list.getId());
		System.out.println("\tADD/UPDATE:"+list.getId());
		pm.close();
		return;
		
	}

}
