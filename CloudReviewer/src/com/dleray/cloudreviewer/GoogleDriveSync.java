package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class GoogleDriveSync extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
        String googleDriveName=req.getParameter("sharedaccount");     
        Drive googleDrive=CredentialsHandler.getGoogleDrive(this, googleDriveName);
       
        //get file list
        java.util.List<File> result = new ArrayList<File>();
        Files.List request = googleDrive.files().list();
        request.setQ("trashed=false");
        request.setMaxResults(50);

     
        do {
            try {
              FileList files = request.execute();
              String tokenForWorker=files.getNextPageToken();
              if(tokenForWorker!=null)
              {
      			Queue queue = QueueFactory.getDefaultQueue();
  				System.out.println("farming:" + tokenForWorker);
	  				TaskOptions options=TaskOptions.Builder.withUrl("/tasks/syncworker");
		        options.param("drivetoken",tokenForWorker);
		        	queue.add(options);
              }
  	
              request.setPageToken(files.getNextPageToken());
            } catch (IOException e) {
              System.out.println("An error occurred: " + e);
              request.setPageToken(null);
            }
          } while (request.getPageToken() != null &&
                   request.getPageToken().length() > 0);
        
	
        System.out.println("done farming out syncs");

		
		return;
	}

}
