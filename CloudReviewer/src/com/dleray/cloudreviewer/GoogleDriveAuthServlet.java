package com.dleray.cloudreviewer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.structures.CloudReviewerProject;
import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.About;

public class GoogleDriveAuthServlet extends HttpServlet {

	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	  private static HttpTransport httpTransport= new NetHttpTransport();

	  
	  /** Directory to store user credentials. */
	  private static final java.io.File DATA_STORE_DIR =
	      new java.io.File(System.getProperty("user.home"), ".store/drive_sample");
	  
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		      
      GoogleAuthorizationCodeFlow flow=getGlobalFlow();
      System.out.println("building attempt from:" + req.getUserPrincipal().getName());
      Credential loadedCredential=flow.loadCredential(req.getUserPrincipal().getName());     
      if(loadedCredential==null)
      {
      	if(req.getParameter("code")!=null)
      	{
      		System.out.println("detected authcode.  just need to store and then load now after token is received");
      		GoogleTokenResponse response=flow.newTokenRequest(req.getParameter("code")).setRedirectUri(req.getRequestURL().toString()).execute();
      		flow.createAndStoreCredential(response, req.getUserPrincipal().getName());
      		loadedCredential=flow.loadCredential(req.getUserPrincipal().getName());
      	}
      	else
      	{
      		//go to client and ask for auth
      	  	GoogleAuthorizationCodeRequestUrl url=flow.newAuthorizationUrl();
      	  	url.setRedirectUri(req.getRequestURL().toString());  	  	
      	  	System.out.println("SENDING REDIRECT FOR AUTH:" + url.getRedirectUri());
          	resp.sendRedirect(url.build());
          	return;
      	}
    
      }
      else
      {
    	  System.out.println("LOADED STORED CREDENTIALS FOR:" + req.getUserPrincipal().getName());
      }
      Drive service = new Drive.Builder(httpTransport, JSON_FACTORY, loadedCredential).setApplicationName(
              "DAVIDLERAY").build();
      About about=service.about();
      
      PersistenceManager pm=PMF.get().getPersistenceManager();
      
      CloudReviewerProject project;
	try {
		project = pm.getObjectById(CloudReviewerProject.class,req.getUserPrincipal().getName());
		 resp.getWriter().println("Project already exists for account:" + req.getUserPrincipal().getName());
	} catch (Exception e) {
  	  project=new CloudReviewerProject();
  	  project.setSharedDriveAccount(req.getUserPrincipal().getName());
  	  project.getUserToPermissionsMap().put(req.getUserPrincipal().getName(),Auth.AccessLevel.ADMIN.toString());
  	  pm.makePersistent(project);
  	  System.out.println("Added project:" + req.getUserPrincipal().getName());
	}
 
      CloudReviewerUser user;
	try {
		user = pm.getObjectById(CloudReviewerUser.class,req.getUserPrincipal().getName());
	} catch (Exception e) {
	 
	    	  user=new CloudReviewerUser();
	    	  user.setUserEmail(req.getUserPrincipal().getName());
	    	  user.setActiveProject(project.getSharedDriveAccount());
	    	  pm.makePersistent(user);
	    	  System.out.println("Added user:" + req.getUserPrincipal().getName());
	      
	}

      pm.close();
      resp.getWriter().println("Setup new project for account:" + req.getUserPrincipal().getName());
      resp.getWriter().println(about.toString());
      
      

      
	}

	
	/** Authorizes the installed application to access user's protected data. */
	  private GoogleAuthorizationCodeFlow getGlobalFlow()  {
	    // load client secrets
		
	    GoogleClientSecrets clientSecrets;
		try {
			//InputStream fileStream=PDFNavigatorServlet.class.getResourceAsStream("client_secrets.json");
			String path = "/client_secrets.json";
			InputStream fileStream =  new FileInputStream(this.getServletConfig().getServletContext().getRealPath(path));
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
			        httpTransport, JSON_FACTORY, clientSecrets, scopes).setAccessType("offline").setCredentialStore(new CredentialsHandler())
			        .build();
			    // authorize
			    System.out.println("successfully built google authorization flow");
			    return flow;
		} catch (IOException e) {
			return null;
		}
	    

	  }




}
