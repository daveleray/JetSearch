package com.dleray.cloudreviewer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.dleray.cloudreviewer.responses.DocJSONResponse;
import com.dleray.cloudreviewer.structures.Document;
import com.dleray.cloudreviewer.structures.DocumentBatch;
import com.dleray.cloudreviewer.structures.UserTag;
import com.dleray.cloudreviewer.structures.UserTagEndpoint;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.gson.Gson;

public class DocHelper {

	public static void sendPDFFileToBrowser(File f,Drive googleDrive, HttpServletResponse resp)
	{
    	try {
			byte[] buffer = new byte[(int) f.getFileSize().intValue()];
			GenericUrl url = new GenericUrl(f.getDownloadUrl());
			HttpResponse response = googleDrive.getRequestFactory()
			            .buildGetRequest(url).execute();
			InputStream is = response.getContent();
			byte[] bytes = getBytes(is);
			

			resp.setContentType("application/pdf");
			resp.addHeader("Content-Disposition", "attachment; filename=" + f.getOriginalFilename());
			resp.setContentLength((int) bytes.length);

			OutputStream responseOutputStream = resp.getOutputStream();

			responseOutputStream.write(bytes);
		} catch (IOException e) {
			System.out.println("Error");
			e.printStackTrace(System.out);
		}
	}
	public static String getTextData(File f,Drive googleDrive)
	{
		try {
			byte[] buffer = new byte[(int) f.getFileSize().intValue()];
			GenericUrl url = new GenericUrl(f.getDownloadUrl());
			HttpResponse response = googleDrive.getRequestFactory()
			            .buildGetRequest(url).execute();
			InputStream is = response.getContent();
			//byte[] bytes = getBytes(is);
			StringWriter writer = new StringWriter();
			IOUtils.copy(is, writer, "UTF-8");
			String theString = writer.toString();
			return theString;
		} catch (IOException e) {
			return null;
		}
	}
	public static DocJSONResponse getJSONResponse(Document d,DocumentBatch batch)
	{
	      
	   	 UserTagEndpoint endpoint=new UserTagEndpoint();	 
		 ArrayList<UserTag> allTagsSoFar=endpoint.getUserTagsByDoc(d.getDocumentIdentifier());
		 ArrayList<String> appliedTags=new ArrayList();
		 for(UserTag u: allTagsSoFar)
		 {
			 appliedTags.add(u.getIssueTagID());
		 }
	        DocJSONResponse response=new DocJSONResponse();
	        response.setBatchID(batch.getDocbatchID());
	        response.setBatchSize(batch.getDocIDCollection().size());
	        response.setAppliedTags(appliedTags);
	        response.setDocInBatchIndex(DocumentHandler.getDocInBatchIndex(BatchHandler.getDefaultBatch(), null, d.getDocumentIdentifier()));
	        response.setDocIdentifier(d.getDocumentIdentifier());

	    	return response;
	}
	public static byte[] getBytes(InputStream is)
	{
		
				try {
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();

					int nRead;
					byte[] data = new byte[16384];

					while ((nRead = is.read(data, 0, data.length)) != -1) {
					  buffer.write(data, 0, nRead);
					}

					buffer.flush();

					return buffer.toByteArray();
				} catch (IOException e) {
					return null;
				}
	}

	
}
