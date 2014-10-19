package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.Document;
import com.dleray.cloudreviewer.structures.HighlightingList;
import com.google.api.services.drive.Drive;

public class TextServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        
		System.out.println("TEXTSERVLET ASKING FOR:" + req.getParameter("docID"));
		

        if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
        {
        	resp.setContentType("text/plain");
    		resp.getWriter().println("PLEASE SIGN IN");
        	return;
        }
        
		//user sends a docID.  send back the relevant PDF.
		String currentID=req.getParameter("docID");
		String type="txt";
        Document d=DocumentHandler.getDocument(currentID);
       
		PersistenceManager pm=PMF.get().getPersistenceManager();
		CloudReviewerUser userFromDB=pm.getObjectById(CloudReviewerUser.class,req.getUserPrincipal().getName());
		
        //has access
        String googleDriveName="imsquinn@gmail.com";       
        Drive googleDrive=CredentialsHandler.getGoogleDrive(this, googleDriveName);
        
        String text=DocumentHandler.getDocumentText(d.getDocumentIdentifier(), googleDrive);
        text=text.replace("\n", "<br>");
        if(text.contains("\r"))
        {
        	System.out.println("weird char detected");
        }

        
        text=text.replace("\r", "");
        text=text.replace("  ", "  ");
      
        text=processHighlighting(text);
        ArrayList<String> alllines=new ArrayList(Arrays.asList(text.split("<br>")));
        for(int i=0;i<alllines.size();i++)
        {
        	String s=alllines.get(i);
        	if(s.contains("From:"))
        	{
        		String s1=s.substring(0,s.indexOf("From:"));
        		String s2=s.substring(s.indexOf("From:"));
        		if(s1.length()>1)
        		{
        			alllines.remove(i);
            		alllines.add(i, s2);
            		alllines.add(i,s1);
            		i=i+1;
        		}
        		
        	}
        }
        String headerColor="#FFFFCC";
        boolean foundFrom=false;
        
        ArrayList<ArrayList<String>> emailChunks=new ArrayList();
        int currentChunkIndex=0;
        emailChunks.add(new ArrayList<String>());
        
        for(int i=0;i<alllines.size();i++)
        {
        	String s=alllines.get(i);
        	if(s.contains("From:") || foundFrom)
        	{
        		if(s.contains("From:"))
        		{
        			currentChunkIndex+=1;
        			emailChunks.add(new ArrayList<String>());
        			//System.out.println("\tLine " + i + " starts FROM block:" + s);
        		}
        		else
        		{
        			//System.out.println("\tLine " + i + " continues FROM block:" + s);
        		}
        		s="<div class=\"email_line\" style=\"width:100%; background-color:"+headerColor+"\">"+s+"</div>";
        		foundFrom=true;
        		if(s.contains("Subject:") || s.contains("Attachment"))
        		{
        			if(i!=alllines.size()-1 && !alllines.get(i+1).contains("Attachment"))
        			{
        				foundFrom=false;
            			//System.out.println("\tLine " + i + " ends FROM block:" + s);
        			}
        	
        		}
        	}
        	else
        	{
        		s="<div>"+s+"</div>";	
        	}
        	emailChunks.get(currentChunkIndex).add(s);
        	alllines.set(i, s);
        }
        resp.setContentType("text/html; charset=UTF-8");
        if(userFromDB.getInvertEmails())
        {
        //	System.out.println("reverse detected");
        	Collections.reverse(emailChunks);
        }
        
        for(ArrayList<String> chunk1: emailChunks)
        {
        	for(String s: chunk1)
        	{
        		 resp.getWriter().println(s);
        	} 	 
        	resp.getWriter().println("<br>");
        }
      /*  for(String s: alllines)
        {
        	resp.getWriter().println(s);
        }*/
        resp.setHeader("docID", d.getDocumentIdentifier());
        return;

       // resp.getWriter().println(text);
        
	}
	
	private String processHighlighting(String input)
	{
		HashMap<String,ArrayList<String>> highlightSet=getHighlightingSet();
		for(String color: highlightSet.keySet())
		{
			for(String word: highlightSet.get(color))
			{
				if(word.contains("\\W"))
				{
					input=input.replaceAll(word, "TEST");
				}
				else
				{
					input=input.replaceAll("\\b"+word+"\\b", "<span style=\"background-color: #"+color+"\">"+word+"</span>");
				}
				
			}
		}
		return input;
	}
	
	private HashMap<String,ArrayList<String>> getHighlightingSet(){
		
		HashMap<String,ArrayList<String>> output=new HashMap();
		

		List<HighlightingList> allHighlights=PMFManager.getHighlightingLists();
		
		for(HighlightingList list: allHighlights)
		{
			ArrayList<String> firstList=new ArrayList();
			for(String kw: list.getKeywords())
			{
				firstList.add(kw);
			}
			if(output.get(list.getHexColor())==null)
					{
				output.put(list.getHexColor(),firstList);
					}
			else
			{
				output.get(list.getHexColor()).addAll(firstList);
			}
			
		}
		
		return output;
			
	
	}
	

}
