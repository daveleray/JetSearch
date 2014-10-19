package com.dleray.cloudreviewer;

import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


public class UserHandler {

	public static CloudReviewerUser getCurrentUser()
	{
		return PMFManager.getUser();
	}
}
