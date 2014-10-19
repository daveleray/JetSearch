package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.DocumentHandler.DocTypes;
import com.dleray.cloudreviewer.structures.Document;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class PDFServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        
		System.out.println("PDFSERVLET ASKING FOR:" + req.getParameter("docID"));
		

        if(req.getUserPrincipal()==null || !req.getUserPrincipal().getName().contains("daveleray"))
        {
        	resp.setContentType("text/plain");
    		resp.getWriter().println("PLEASE SIGN IN");
        	return;
        }
        
		//user sends a docID.  send back the relevant PDF.
		String currentID=req.getParameter("docID");
		String doctype=req.getParameter("doctype");
		
		DocumentHandler.DocTypes type=DocTypes.TEXT;
		if(doctype.contentEquals("bates"))
		{
			type=DocTypes.PRODUCED;
		}
		else if(doctype.contentEquals("native"))
		{
			type=DocTypes.NATIVE;
		}
		else if(doctype.contentEquals("tiff"))
		{
			type=DocTypes.TIFF;
		}
        Document d=DocumentHandler.getDocument(currentID);
       
        //has access
        String googleDriveName="imsquinn@gmail.com";       
        Drive googleDrive=CredentialsHandler.getGoogleDrive(this, googleDriveName);
        

        //get file list
        String googleID=d.getTypesTogoogleDriveIDMap().get(type.toString());
        System.out.println("request google doc #:" + googleID);
        Get request = googleDrive.files().get(googleID);
        File file = request.execute();
        String a=file.getEmbedLink();
        System.out.println(a);
        DocHelper.sendPDFFileToBrowser(file, googleDrive, resp);
        resp.setHeader("docID", d.getDocumentIdentifier());
        return;

	}

}
