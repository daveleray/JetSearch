package com.dleray.cloudreviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.dleray.cloudreviewer.structures.MetadataHolder;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class DocMetadataServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		   // checks if the request actually contains upload file
        if (!ServletFileUpload.isMultipartContent(req)) {
            // if not, we stop here
            PrintWriter writer = resp.getWriter();
            writer.println("Error: Form must has enctype=multipart/form-data.");
            writer.flush();
            return;
        }
        ServletFileUpload upload = new ServletFileUpload();
       
        String delimiter=",";
        ArrayList<Text> allTextInputs=new ArrayList();
        try {
			FileItemIterator iter = upload.getItemIterator(req);

			// Parse the request
			while(iter.hasNext())
			{
				FileItemStream item = iter.next();
			    String name =item.getFieldName();
			    InputStream stream = item.openStream();
			    if (item.isFormField()) {
			        if(name.equals("delimiter"))
			        {
			        	delimiter=Streams.asString(stream);
			        }
			    } 
			    else
			    {
			    	BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			    	String line=null;

			    	 while ((line = reader.readLine()) != null) {
			    		 allTextInputs.add(new Text(line));
			    	    }
			    }
			}
			

		
			

		} catch (FileUploadException e) {
		
		}
        MetadataHolder holder=new MetadataHolder();
        holder.setDelimiter(delimiter);
        holder.setLinesOfMetadata(allTextInputs);
		PersistenceManager mgr = PMF.get().getPersistenceManager();
		try {
			mgr.makePersistent(holder);
		       // Add the task to the default queue.
	        
			int docspertask=15;
			int numTasks=allTextInputs.size()/docspertask;
			for(int j=0;j<=numTasks;j++)
			{
				Queue queue = QueueFactory.getDefaultQueue();
		        TaskOptions options=TaskOptions.Builder.withUrl("/tasks/metadataworker");
		        int start=j*docspertask;
		        int end=Math.min((j+1)*docspertask,allTextInputs.size()-1);
		        options.param("holderid", holder.getId()+"");
		        options.param("start", start+"");
		        options.param("end", end+"");
		        queue.add(options);
		        System.out.println("added task:" + start + " to " + end);
			}
			

	        
		}
		finally
		{
			mgr.close();
		}
	}

}
