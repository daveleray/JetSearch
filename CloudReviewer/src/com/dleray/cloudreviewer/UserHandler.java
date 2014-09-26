package com.dleray.cloudreviewer;

import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.CloudReviewerUserEndpoint;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


public class UserHandler {

	public static CloudReviewerUser getCurrentUser()
	{
		 UserService userService = UserServiceFactory.getUserService();
	     User user = userService.getCurrentUser();
        CloudReviewerUser progUser;
        CloudReviewerUserEndpoint userEndpoint=new CloudReviewerUserEndpoint();
		try {
			progUser = userEndpoint.getCloudReviewerUser(user.getEmail());
		} catch (Exception e) {
			progUser=new CloudReviewerUser();
			progUser.setCurrentPanelID("taggingPanel-1");
			progUser.setUserEmail(user.getEmail());
			userEndpoint.insertCloudReviewerUser(progUser);
		}
		return progUser;
	}
}
