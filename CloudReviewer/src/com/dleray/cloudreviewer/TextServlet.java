package com.dleray.cloudreviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.structures.Document;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.drive.model.File;

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
    
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println(text);
        resp.setHeader("docID", d.getDocumentIdentifier());
        return;

       // resp.getWriter().println(text);
        
	}
	

	

}
