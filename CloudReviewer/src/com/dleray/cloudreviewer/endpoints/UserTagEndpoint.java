package com.dleray.cloudreviewer.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.dleray.cloudreviewer.Auth;
import com.dleray.cloudreviewer.PMF;
import com.dleray.cloudreviewer.Auth.AccessLevel;
import com.dleray.cloudreviewer.structures.UserTag;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@Api(name = "usertagendpoint", namespace = @ApiNamespace(ownerDomain = "dleray.com", ownerName = "dleray.com", packagePath = "cloudreviewer.structures"))
public class UserTagEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listUserTag")
	public CollectionResponse<UserTag> listUserTag(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<UserTag> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(UserTag.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<UserTag>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (UserTag obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<UserTag> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getUserTag")
	public UserTag getUserTag(@Named("id") String id) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = getPersistenceManager();
		UserTag usertag = null;
		try {
			usertag = mgr.getObjectById(UserTag.class, id);
		} finally {
			mgr.close();
		}
		return usertag;
	}
	@ApiMethod(name = "getUserTagsByDoc")
	public ArrayList<UserTag> getUserTagsByDoc(@Named("docid") String docID)
	{
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = getPersistenceManager();
		ArrayList<UserTag> output=new ArrayList();
	
			Query q = mgr.newQuery(UserTag.class);
			q.setFilter("document == documentParam");
			q.setOrdering("dateInMili desc");
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
	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param usertag the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertUserTag")
	public UserTag insertUserTag(UserTag usertag) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsUserTag(usertag)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(usertag);
		} finally {
			mgr.close();
		}
		return usertag;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param usertag the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateUserTag")
	public UserTag updateUserTag(UserTag usertag) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsUserTag(usertag)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(usertag);
		} finally {
			mgr.close();
		}
		return usertag;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeUserTag")
	public void removeUserTag(@Named("id") String id) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return;
		}
		PersistenceManager mgr = getPersistenceManager();
		try {
			UserTag usertag = mgr.getObjectById(UserTag.class, id);
			mgr.deletePersistent(usertag);
		} finally {
			mgr.close();
		}
	}

	private boolean containsUserTag(UserTag usertag) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return false;
		}
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(UserTag.class, usertag.getUsertagID());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
