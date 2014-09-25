package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.structures.UserTag;
import com.dleray.cloudreviewer.structures.UserTagEndpoint;

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
		 String user=req.getUserPrincipal().getName();
		 UserTagEndpoint endpoint=new UserTagEndpoint();
		 UserTag tag=new UserTag();
		 tag.setUsertagID(currentDoc+currentTag);
		 tag.setDocument(currentDoc);
		 tag.setDateInMili(System.currentTimeMillis());
		 tag.setUsername(user);
		 tag.setAppliedTag(currentTag);
		 
		 ArrayList<UserTag> allTagsSoFar=endpoint.getUserTagsByDoc(currentDoc);
		 boolean alreadyTagged=false;
		 for(UserTag u: allTagsSoFar)
		 {
			 if(u.getIssueTagID().equals(currentTag))
			 {
				 alreadyTagged=true;
				 endpoint.removeUserTag(u.getUsertagID());
			 }
		 }
		 if(!alreadyTagged)
		 {
			 endpoint.insertUserTag(tag);
		 }
		 
	}

	
}
