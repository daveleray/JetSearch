package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class SyncWorker extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	      String googleDriveName=req.getParameter("sharedaccount");     
	      Drive googleDrive=CredentialsHandler.getGoogleDrive(this, googleDriveName);
	      
	      String pageToken=req.getParameter("drivetoken");
	        //get file list
	        java.util.List<File> result = new ArrayList<File>();
	        Files.List request = googleDrive.files().list();
	        request.setQ("trashed=false");
	        request.setMaxResults(50);
	        request.setPageToken(pageToken);
	        FileList files = request.execute();
	        result.addAll(files.getItems());		
	        System.out.println("detected this many GDrive Files:" + result.size());
	        
	    	List<File> allfolders=new ArrayList<File>();
			Files.List folderrequest=googleDrive.files().list();
			folderrequest.setQ("trashed=false AND mimeType='application/vnd.google-apps.folder'");
			FileList allFolders=folderrequest.execute();
			List<File> folderList=new ArrayList<File>();
			folderList.addAll(allFolders.getItems());
			DocumentHandler.processDriveFileList(result,folderList,googleDrive);
			System.out.println("SyncWorker has finished:" + pageToken);
	}
}
