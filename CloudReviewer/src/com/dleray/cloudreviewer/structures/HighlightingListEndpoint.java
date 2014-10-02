package com.dleray.cloudreviewer.structures;

import com.dleray.cloudreviewer.Auth;
import com.dleray.cloudreviewer.PMF;
import com.dleray.cloudreviewer.Auth.AccessLevel;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api(name = "highlightinglistendpoint", namespace = @ApiNamespace(ownerDomain = "dleray.com", ownerName = "dleray.com", packagePath = "cloudreviewer.structures"))
public class HighlightingListEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listHighlightingList")
	public CollectionResponse<HighlightingList> listHighlightingList(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<HighlightingList> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(HighlightingList.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<HighlightingList>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (HighlightingList obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<HighlightingList> builder()
				.setItems(execute).setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getHighlightingList")
	public HighlightingList getHighlightingList(@Named("id") String id) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = getPersistenceManager();
		HighlightingList highlightinglist = null;
		try {
			highlightinglist = mgr.getObjectById(HighlightingList.class, id);
		} finally {
			mgr.close();
		}
		return highlightinglist;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param highlightinglist the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertHighlightingList")
	public HighlightingList insertHighlightingList(
			HighlightingList highlightinglist) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsHighlightingList(highlightinglist)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(highlightinglist);
		} finally {
			mgr.close();
		}
		return highlightinglist;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param highlightinglist the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateHighlightingList")
	public HighlightingList updateHighlightingList(
			HighlightingList highlightinglist) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsHighlightingList(highlightinglist)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(highlightinglist);
		} finally {
			mgr.close();
		}
		return highlightinglist;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeHighlightingList")
	public void removeHighlightingList(@Named("id") String id) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return;
		}
		PersistenceManager mgr = getPersistenceManager();
		try {
			HighlightingList highlightinglist = mgr.getObjectById(
					HighlightingList.class, id);
			mgr.deletePersistent(highlightinglist);
		} finally {
			mgr.close();
		}
	}

	private boolean containsHighlightingList(HighlightingList highlightinglist) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return false;
		}
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(HighlightingList.class,
					highlightinglist.getId());
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
