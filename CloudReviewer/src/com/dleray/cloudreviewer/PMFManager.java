package com.dleray.cloudreviewer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.dleray.cloudreviewer.structures.CloudReviewerUser;
import com.dleray.cloudreviewer.structures.Document;
import com.dleray.cloudreviewer.structures.HighlightingList;
import com.dleray.cloudreviewer.structures.IssueTag;
import com.dleray.cloudreviewer.structures.UserTag;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagList;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class PMFManager {

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}
	
	public static List<TagList> getAllTagLists()
	{		
		PersistenceManager pm=PMF.get().getPersistenceManager();
		Query q = pm.newQuery(TagList.class);
		
		List<TagList> output=(List<TagList>) q.execute();
		return output;
	}
	
	public static ArrayList<TagList> getTaggingPanelsForProject(String projectID)
	{
		PersistenceManager mgr = getPersistenceManager();
		ArrayList<TagList> output=new ArrayList();
	
			Query q = mgr.newQuery(TagList.class);
			q.setFilter("projectID == projectIDParam");
			
			q.declareParameters("String projectIDParam");
		
			try {
				  List<TagList> results = (List<TagList>) q.execute(projectID);
				  if (!results.isEmpty()) {
				    for (TagList p : results) {
				      output.add(p);
				    }
				  } else {
				    // Handle "no results" case
				  }
				} finally {
				  q.closeAll();
				}
			return output;
			
	}
	public static ArrayList<UserTag> getUserTagsForDoc(String docID)
	{
		PersistenceManager mgr = getPersistenceManager();
		ArrayList<UserTag> output=new ArrayList();
	
			Query q = mgr.newQuery(UserTag.class);
			q.setFilter("document == documentParam");
			
			q.declareParameters("String documentParam");
		
			try {
				  List<UserTag> results = (List<UserTag>) q.execute(docID);
				  if (!results.isEmpty()) {
				    for (UserTag p : results) {
				      output.add(p);
				    }
				  } else {
				    // Handle "no results" case
				  }
				} finally {
				  q.closeAll();
				}
			return output;
			
	}
	public static void removeTag(String tagID)
	{
		PersistenceManager mgr = getPersistenceManager();
		try {
			UserTag usertag = mgr.getObjectById(UserTag.class, tagID);
			mgr.deletePersistent(usertag);
		} finally {
			mgr.close();
		}
	}
	public static void addTag(UserTag tag)
	{
		PersistenceManager mgr = getPersistenceManager();
		mgr.makePersistent(tag);
		mgr.close();
	}

	public static List<HighlightingList> getHighlightingLists() {
		PersistenceManager pm=PMF.get().getPersistenceManager();
		Query q = pm.newQuery(HighlightingList.class);
		
		List<HighlightingList> output=(List<HighlightingList>) q.execute();
		return output;
	}
	public static List<IssueTag> getIssueTags()
	{
		
		PersistenceManager pm=PMF.get().getPersistenceManager();
		Query q = pm.newQuery(IssueTag.class);
		
		List<IssueTag> output=(List<IssueTag>) q.execute();
		pm.close();
		return output;
	}
	public static List<Document> getDocuments()
	{
		
		PersistenceManager pm=PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Document.class);
		
		List<Document> output=(List<Document>) q.execute();
		pm.close();
		return output;
	}
	public static List<CloudReviewerUser> getUsers()
	{
		
		PersistenceManager pm=PMF.get().getPersistenceManager();
		Query q = pm.newQuery(CloudReviewerUser.class);
		
		List<CloudReviewerUser> output=(List<CloudReviewerUser>) q.execute();
		pm.close();
		return output;
	}
	public static CloudReviewerUser getUser()
	{
		  UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();
		PersistenceManager pm=PMF.get().getPersistenceManager();
		CloudReviewerUser output=pm.getObjectById(CloudReviewerUser.class,user.getEmail());
		output.setActiveProject("imsquinn@gmail.com");
		HashSet<String> tagPanelsForce=output.getActiveTaggingPanels();
		HashSet<String> highlightForce=output.getHighlightingIDs();
		pm.makePersistent(output);
		pm.close();
		return output;
	}
	public static void updateAddUser(CloudReviewerUser user)
	{
		PersistenceManager pm=PMF.get().getPersistenceManager();
		pm.makePersistent(user);
		pm.close();
	}

	public static List<IssueTag> getIssueTagsForProject(String activeProject) {
		PersistenceManager mgr = getPersistenceManager();
		ArrayList<IssueTag> output=new ArrayList();
	
			Query q = mgr.newQuery(IssueTag.class);
			q.setFilter("projectID == projectIDParam");
			
			q.declareParameters("String projectIDParam");
		
			try {
				  List<IssueTag> results = (List<IssueTag>) q.execute(activeProject);
				  if (!results.isEmpty()) {
				    for (IssueTag p : results) {
				      output.add(p);
				    }
				  } else {
				    // Handle "no results" case
				  }
				} finally {
				  q.closeAll();
				}
			return output;
	}
}
