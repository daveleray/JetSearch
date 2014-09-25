package com.dleray.cloudreviewer.structures.taggingcontrol;

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

@Api(name = "taglistendpoint", namespace = @ApiNamespace(ownerDomain = "dleray.com", ownerName = "dleray.com", packagePath = "cloudreviewer.structures.taggingcontrol"))
public class TagListEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listTagList")
	public CollectionResponse<TagList> listTagList(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<TagList> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(TagList.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<TagList>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (TagList obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<TagList> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getTagList")
	public TagList getTagList(@Named("id") String id) {
		PersistenceManager mgr = getPersistenceManager();
		TagList taglist = null;
		try {
			taglist = mgr.getObjectById(TagList.class, id);
			for(String s: taglist.getIssueTagIDs())
			{
				Double a=5.0;
			}
			
		} finally {
			mgr.close();
		}
		return taglist;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param taglist the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertTagList")
	public TagList insertTagList(TagList taglist) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsTagList(taglist)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(taglist);
		} finally {
			mgr.close();
		}
		return taglist;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param taglist the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateTagList")
	public TagList updateTagList(TagList taglist) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsTagList(taglist)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(taglist);
		} finally {
			mgr.close();
		}
		return taglist;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeTagList")
	public void removeTagList(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			TagList taglist = mgr.getObjectById(TagList.class, id);
			mgr.deletePersistent(taglist);
		} finally {
			mgr.close();
		}
	}

	private boolean containsTagList(TagList taglist) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(TagList.class, taglist.getControlID());
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
