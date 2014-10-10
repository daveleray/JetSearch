package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.structures.Document;
import com.dleray.cloudreviewer.structures.HighlightingList;
import com.dleray.cloudreviewer.structures.HighlightingListEndpoint;
import com.google.api.server.spi.response.CollectionResponse;
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
        for(int i=0;i<alllines.size();i++)
        {
        	String s=alllines.get(i);
        	if(s.equals(""))
        	{
        		s="&nbsp";
        		System.out.println("Line " + i + " is BLANK LINE");
        	}
        	else
        	{
        		System.out.println("Line " + i + " is NOT BLANK LINE");
        	}

        	if(s.contains("From:") || foundFrom)
        	{
        		if(s.contains("From:"))
        		{
        			System.out.println("\tLine " + i + " starts FROM block:" + s);
        		}
        		else
        		{
        			System.out.println("\tLine " + i + " continues FROM block:" + s);
        		}
        		s="<div class=\"email_line\" style=\"width:100%; background-color:"+headerColor+"\">"+s+"</div>";
        		foundFrom=true;
        		if(s.contains("Subject:") || s.contains("Attachment"))
        		{
        			if(!alllines.get(i+1).contains("Attachment"))
        			{
        				foundFrom=false;
            			System.out.println("\tLine " + i + " ends FROM block:" + s);
        			}
        	
        		}
        	}
        	else
        	{
        		s="<div>"+s+"</div>";	
        	}
        	alllines.set(i, s);
        }
        resp.setContentType("text/html; charset=UTF-8");
        for(String s: alllines)
        {
        	  resp.getWriter().println(s);
        	  //resp.getWriter().println("<br>");
        }
      
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
		
		HighlightingListEndpoint endpoint=new HighlightingListEndpoint();
		CollectionResponse<HighlightingList> lists=endpoint.listHighlightingList("", 1000);
		
		for(HighlightingList list: lists.getItems())
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
