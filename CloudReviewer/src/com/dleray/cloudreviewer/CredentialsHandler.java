package com.dleray.cloudreviewer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;

public class CredentialsHandler implements CredentialStore {

	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	  private static HttpTransport httpTransport= new NetHttpTransport();
	  
	@Override
	public void delete(String arg0, Credential arg1) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean load(String arg0, Credential arg1) throws IOException {
		

	    try
	    {
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    PersistentCredentials persistentC=pm.getObjectById(PersistentCredentials.class,arg0);
	    
	    
	    	arg1.setAccessToken(persistentC.getAccessToken());
			arg1.setExpirationTimeMilliseconds(persistentC.getExpMilliseconds());
			arg1.setExpiresInSeconds(persistentC.getExpSeconds());
			arg1.setRefreshToken(persistentC.getRefreshToken());
			return true;
	    }
	    catch(Exception e)
	    {
	    
	    	return false;
	    }
		
	
	
	}

	public static Drive getGoogleDrive(HttpServlet servlet,String googleDriveName)
	{
	   
		try {
				Credential loadedCredential=getCredential(servlet);
			    Drive service = new Drive.Builder(httpTransport, JSON_FACTORY, loadedCredential).setApplicationName(
			              "DAVIDLERAY").build();
			    
			    return service;
			    
		} catch (Exception e) {
			return null;
		}
		
	}
	public static String getAccessToken(HttpServlet servlet)
	{
		return getCredential(servlet).getAccessToken();
	}
	private static Credential getCredential(HttpServlet servlet)
	{
		 try {
			GoogleClientSecrets clientSecrets;
			//InputStream fileStream=PDFNavigatorServlet.class.getResourceAsStream("client_secrets.json");
			String path = "/client_secrets.json";
			InputStream fileStream =  new FileInputStream(servlet.getServletConfig().getServletContext().getRealPath(path));
			Reader reader=new InputStreamReader(fileStream);
			clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,reader);
			if (clientSecrets.getDetails().getClientId().startsWith("Enter")
			        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			      System.out.println(
			          "Enter Client ID and Secret from https://code.google.com/apis/console/?api=drive "
			          + "into drive-cmdline-sample/src/main/resources/client_secrets.json");
			      System.exit(1);
			    }
			 
			    
			    ArrayList<String> scopes=new ArrayList();
			    scopes.add("https://www.googleapis.com/auth/drive");
			    
			    // set up authorization code flow
			    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
			        httpTransport, JSON_FACTORY, clientSecrets, scopes).setAccessType("offline").setCredentialStore(new CredentialsHandler()).build();
			    Credential loadedCredential=flow.loadCredential("imsquinn@gmail.com");
			    return loadedCredential;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		} 
	}
	@Override
	public void store(String arg0, Credential arg1) throws IOException {
		

	    PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(new PersistentCredentials(arg1, arg0));
        } finally {
            pm.close();
        }
        

	}
	
	


}
