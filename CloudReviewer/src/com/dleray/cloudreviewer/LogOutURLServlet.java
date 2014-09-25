package com.dleray.cloudreviewer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class LogOutURLServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		

		
	     try {
			UserService userService = UserServiceFactory.getUserService();

			    String thisURL = req.getRequestURI();
			    JSONObject json=new JSONObject();
			    
			    resp.setContentType("application/json");
			    if (req.getUserPrincipal() != null) {

			    	
			    	String loggingOut=userService.createLogoutURL("/index.html");
			    	json.put("URL",loggingOut);
			    	json.put("USER", req.getUserPrincipal());
			    	resp.getWriter().write(json.toString());
			    }
			    else
			    {

			    	String loggingIn=userService.createLoginURL("/index.html");
			    	json.put("URL",loggingIn);
			    	String output=json.toString();
			    	resp.getWriter().write(output);
			    }
		} catch (JSONException e) {
			e.printStackTrace();
		}
	        
		
	}
	
	

}
