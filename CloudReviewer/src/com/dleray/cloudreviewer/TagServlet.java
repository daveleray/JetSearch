package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.structures.UserTag;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class TagServlet extends HttpServlet {



	@Override
	/** toggles the tagging for given @docid and @tag
	 * 
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
        if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
        {
        	System.out.println("logging in from:" + req.getUserPrincipal());
        	resp.setContentType("text/plain");
    		resp.getWriter().println("PLEASE SIGN IN");
        	return;
        }
        
        
		 String currentDoc=req.getParameter("docid");
		 String currentTag=req.getParameter("tag");
		 if(currentDoc==null || currentTag==null)
		 {
			 throw new ServletException("INVALID TAG INPUT!!");
		 }
		 
		 String applyTag=req.getParameter("applied");
		
		 System.out.println("\nDetected tag request:" + applyTag + " for user:" + req.getUserPrincipal().getName() + "and  " + currentDoc + " // " + currentTag);
		 
		 
		 String key = currentDoc+"taginfo";
		 ArrayList<UserTag> allTagsSoFar;
				    ArrayList<UserTag> value;

				    // Using the synchronous cache
				    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
				    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
				    value = (ArrayList<UserTag>) syncCache.get(key); // read from cache
				    if (value == null) {
				      allTagsSoFar=PMFManager.getUserTagsForDoc(currentDoc);
				      syncCache.put(key, allTagsSoFar); // populate cache
				      System.out.println("populate cache");
				    }
				    else
				    {
				    	System.out.println("read from cache");
				    	allTagsSoFar=value;
				    }
				    
		 
		 boolean alreadyTagged=false;
		 UserTag prexistingUserTag=null;
		 String user=req.getUserPrincipal().getName();
		 for(UserTag u: allTagsSoFar)
		 {
		//	 System.out.println(u.getUsertagID() + " Consider tag:" + u.getDocument() + " // " + u.getIssueTagID() + " // " + u.getUsername());
			 if(u.getIssueTagID()==null)
			 {
	
				 PMFManager.removeTag(u.getUsertagID());
				 allTagsSoFar.remove(u);
				 System.out.println("null tag removed");
			 }
			 else if(u.getIssueTagID().equals(currentTag) && u.getDocument().equals(currentDoc))
			 {
				 System.out.println("detected match with " + u.getIssueTagID() + " and doc " + u.getDocument());
				 alreadyTagged=true;
				 prexistingUserTag=u;
				// endpoint.removeUserTag(u.getUsertagID());
				// System.out.println("tag removed as identical");
			 }
		 }
		 System.out.println("done looping over tags");
		 if(applyTag.contentEquals("true"))
		 {
			 if(!alreadyTagged)
			 {
				 System.out.println("Add tag that does not exist:" + currentDoc + " // " + currentTag + " // " + user);
				 UserTag tag=new UserTag();
				 tag.setUsertagID(currentDoc+currentTag);
				 tag.setDocument(currentDoc);
				 tag.setDateInMili(System.currentTimeMillis());
				 tag.setUsername(user);
				 tag.setAppliedTag(currentTag);
				 PMFManager.addTag(tag);
				 allTagsSoFar.add(tag);
				 System.out.println("tag added");
			 }
			 else
			 {
				 System.out.println("tag already exists. not taking action");
			 }
		 }
		 else
		 {
			 if(alreadyTagged)
			 {
				 PMFManager.removeTag(prexistingUserTag.getUsertagID());
				 allTagsSoFar.remove(prexistingUserTag);
				 System.out.println("Remove tag from database:" + currentDoc + " // " + currentTag + " // " + user);
			 }
		 }
	
		 
		 			//update memcache
				    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
				    syncCache.put(key, allTagsSoFar); // populate cache
				
		 
	}

	
}
