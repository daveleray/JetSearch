package com.dleray.cloudreviewer.endpoints;

import com.dleray.cloudreviewer.PMF;
import com.dleray.cloudreviewer.structures.IssueTag;
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

@Api(name = "issuetagendpoint", namespace = @ApiNamespace(ownerDomain = "dleray.com", ownerName = "dleray.com", packagePath = "cloudreviewer.structures"))
public class IssueTagEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listIssueTag")
	public CollectionResponse<IssueTag> listIssueTag(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<IssueTag> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(IssueTag.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<IssueTag>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (IssueTag obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<IssueTag> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getIssueTag")
	public IssueTag getIssueTag(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		IssueTag issuetag = null;
		try {
			issuetag = mgr.getObjectById(IssueTag.class, id);
		} finally {
			mgr.close();
		}
		return issuetag;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param issuetag the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertIssueTag")
	public IssueTag insertIssueTag(IssueTag issuetag) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsIssueTag(issuetag)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(issuetag);
		} finally {
			mgr.close();
		}
		return issuetag;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param issuetag the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateIssueTag")
	public IssueTag updateIssueTag(IssueTag issuetag) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsIssueTag(issuetag)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(issuetag);
		} finally {
			mgr.close();
		}
		return issuetag;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeIssueTag")
	public void removeIssueTag(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			IssueTag issuetag = mgr.getObjectById(IssueTag.class, id);
			mgr.deletePersistent(issuetag);
		} finally {
			mgr.close();
		}
	}

	private boolean containsIssueTag(IssueTag issuetag) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(IssueTag.class, issuetag.getId());
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
