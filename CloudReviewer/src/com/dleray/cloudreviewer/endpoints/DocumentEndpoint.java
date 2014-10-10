package com.dleray.cloudreviewer.endpoints;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.dleray.cloudreviewer.Auth;
import com.dleray.cloudreviewer.Auth.AccessLevel;
import com.dleray.cloudreviewer.PMF;
import com.dleray.cloudreviewer.structures.Document;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@Api(name = "documentendpoint", namespace = @ApiNamespace(ownerDomain = "dleray.com", ownerName = "dleray.com", packagePath = "cloudreviewer.structures"))
public class DocumentEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listDocument")
	public CollectionResponse<Document> listDocument(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Document> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Document.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Document>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Document obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Document> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getDocument")
	public Document getDocument(@Named("id") String id) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = getPersistenceManager();
		Document document = null;
		try {
			document = mgr.getObjectById(Document.class, id);
			if(document.getMetadataAndValuesNonSearchable()==null)
			{
				document.setMetadataAndValuesNonSearchable(new HashMap<String,Text>());
			}
			if(document.getMetadataAndValuesSearchable()==null)
			{
				document.setMetadataAndValuesSearchable(new HashMap<String,String>());
			}
			Set<String> force=document.getMetadataAndValuesNonSearchable().keySet();
			Set<String> force2=document.getMetadataAndValuesSearchable().keySet();
			for(String s: force)
			{
				document.getMetadataAndValuesNonSearchable().get(s);
			}
			for(String s: force2)
			{
				document.getMetadataAndValuesSearchable().get(s);
			}
		}
		finally {
			mgr.close();
		}
		return document;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param document the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertDocument")
	public Document insertDocument(Document document) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsDocument(document)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(document);
		} finally {
			mgr.close();
		}
		return document;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param document the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateDocument")
	public Document updateDocument(Document document) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsDocument(document)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(document);
		} finally {
			mgr.close();
		}
		return document;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeDocument")
	public void removeDocument(@Named("id") String id) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return;
		}
		PersistenceManager mgr = getPersistenceManager();
		try {
			Document document = mgr.getObjectById(Document.class, id);
			mgr.deletePersistent(document);
		} finally {
			mgr.close();
		}
	}

	private boolean containsDocument(Document document) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return false;
		}
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Document.class, document.getDocumentIdentifier());
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