package com.dleray.cloudreviewer.structures;

import com.dleray.cloudreviewer.PMF;

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

@Api(name = "taggingpanelendpoint", namespace = @ApiNamespace(ownerDomain = "dleray.com", ownerName = "dleray.com", packagePath = "cloudreviewer.structures"))
public class TaggingPanelEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listTaggingPanel")
	public CollectionResponse<TaggingPanel> listTaggingPanel(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<TaggingPanel> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(TaggingPanel.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<TaggingPanel>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (TaggingPanel obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<TaggingPanel> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getTaggingPanel")
	public TaggingPanel getTaggingPanel(@Named("id") String id) {
		PersistenceManager mgr = getPersistenceManager();
		TaggingPanel taggingpanel = null;
		try {
			taggingpanel = mgr.getObjectById(TaggingPanel.class, id);
		} finally {
			mgr.close();
		}
		return taggingpanel;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param taggingpanel the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertTaggingPanel")
	public TaggingPanel insertTaggingPanel(TaggingPanel taggingpanel) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsTaggingPanel(taggingpanel)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(taggingpanel);
		} finally {
			mgr.close();
		}
		return taggingpanel;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param taggingpanel the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateTaggingPanel")
	public TaggingPanel updateTaggingPanel(TaggingPanel taggingpanel) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsTaggingPanel(taggingpanel)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(taggingpanel);
		} finally {
			mgr.close();
		}
		return taggingpanel;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeTaggingPanel")
	public void removeTaggingPanel(@Named("id") String id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			TaggingPanel taggingpanel = mgr.getObjectById(TaggingPanel.class,
					id);
			mgr.deletePersistent(taggingpanel);
		} finally {
			mgr.close();
		}
	}

	private boolean containsTaggingPanel(TaggingPanel taggingpanel) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(TaggingPanel.class, taggingpanel.getTaggingID());
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
