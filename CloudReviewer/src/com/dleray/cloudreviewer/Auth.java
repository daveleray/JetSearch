package com.dleray.cloudreviewer;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Auth {

	public static boolean isAuthorized(AccessLevel level)
	{
	       UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();
	        if(user!=null)
	        {
	       //     System.out.println("AUTH ATTEMPT FROM:" + user.getEmail());
	        }
	    
	        if(user!=null && user.getEmail().contains("daveleray"))
	        {
	        	return true;
	        }
	        else
	        {
	        	return false;
	        }
	}
	
	public enum AccessLevel{
		ADMIN,USER,PUBLIC;
	}
}
