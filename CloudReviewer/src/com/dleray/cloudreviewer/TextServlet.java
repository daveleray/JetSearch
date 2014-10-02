package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
        text=text.replace("\r", "");
        text=text.replace("  ", "  ");
        text=processHighlighting(text);
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println(text);
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
