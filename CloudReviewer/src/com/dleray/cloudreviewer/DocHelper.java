package com.dleray.cloudreviewer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

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
