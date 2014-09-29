package com.dleray.cloudreviewer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.endpoints.DocumentEndpoint;
import com.dleray.cloudreviewer.endpoints.IssueTagEndpoint;
import com.dleray.cloudreviewer.endpoints.UserTagEndpoint;
import com.dleray.cloudreviewer.structures.Document;
import com.dleray.cloudreviewer.structures.IssueTag;
import com.dleray.cloudreviewer.structures.UserTag;
import com.google.api.server.spi.response.CollectionResponse;

public class CSVServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		
        if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
        {
        	System.out.println("logging in from:" + req.getUserPrincipal());
        	resp.setContentType("text/plain");
    		resp.getWriter().println("PLEASE SIGN IN");
        	return;
        }
        
		resp.setContentType("application/download");
		resp.setHeader("Content-Disposition","attachment;filename=\"" + "test.csv" + "\"");
		    try
		    {
		    	DocumentEndpoint endpoint=new DocumentEndpoint();
		    	CollectionResponse<Document> output=endpoint.listDocument("", 1000);
		    	
		    	IssueTagEndpoint issues=new IssueTagEndpoint();
		    	CollectionResponse<IssueTag> tags=issues.listIssueTag("", 1000);
		    	ArrayList<IssueTag> allIssues=new ArrayList(tags.getItems());
		    	Collections.sort(allIssues,new IssueComparator());
		    	String outputString="";
		        for(IssueTag t: allIssues)
		        {
		        	outputString=outputString+","+t.getTagID();
		        }
		        UserTagEndpoint userEndpoint=new UserTagEndpoint();
		        for(Document d: output.getItems())
		        {
		        	outputString=outputString+"\n"+d.getDocumentIdentifier()+",";
		        	for(IssueTag t: allIssues)
		        	{
		        		System.out.println(d.getTagIDs());
		        		ArrayList<UserTag> doctags=userEndpoint.getUserTagsByDoc(d.getDocumentIdentifier());
		        		boolean foundTag=false;
		        		for(UserTag u: doctags)
		        		{
		        			if(u.getIssueTagID().contentEquals(t.getTagID()))
		        			{
		        				foundTag=true;
		        			}
		        		}
		        		if(foundTag)
		        		{
		        			outputString=outputString+"YES"+",";
		        		}
		        		else
		        		{
		        			outputString=outputString+"NO"+",";
		        		}
		        	}
		        }
		    	OutputStream outputStream = resp.getOutputStream();
		        outputStream.write(outputString.getBytes());
		        outputStream.flush();
		        outputStream.close();
		    }
		    catch(Exception e)
		    {
		        System.out.println(e.toString());
		    }
		    
	}

	private class IssueComparator implements Comparator<IssueTag>
	{

		@Override
		public int compare(IssueTag o1, IssueTag o2) {
			// TODO Auto-generated method stub
			return o1.getTagID().compareTo(o2.getTagID());
		}
		
	}
	
}
