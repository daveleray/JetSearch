package com.dleray.cloudreviewer.endpoints;

import java.util.HashMap;
import java.util.HashSet;
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
import com.dleray.cloudreviewer.structures.Document;
import com.dleray.cloudreviewer.structures.DocumentBatch;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@Api(name = "documentbatchendpoint", namespace = @ApiNamespace(ownerDomain = "dleray.com", ownerName = "dleray.com", packagePath = "cloudreviewer.structures"))
public class DocumentBatchEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listDocumentBatch")
	public CollectionResponse<DocumentBatch> listDocumentBatch(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<DocumentBatch> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(DocumentBatch.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<DocumentBatch>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (DocumentBatch obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<DocumentBatch> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getDocumentBatch")
	public DocumentBatch getDocumentBatch(@Named("id") String id) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		
		PersistenceManager mgr = getPersistenceManager();
		DocumentBatch documentbatch = null;
		try {
			documentbatch = mgr.getObjectById(DocumentBatch.class, id);
		}

		finally {
			mgr.close();
		}
		return documentbatch;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param documentbatch the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertDocumentBatch")
	public DocumentBatch insertDocumentBatch(DocumentBatch documentbatch) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsDocumentBatch(documentbatch)) {
				throw new EntityExistsException("Object already exists");
			}
			HashSet<String> validatedCollection=new HashSet();
			System.out.println("attempting to validate:" + documentbatch.getDocIDCollection().size() + " docs");
			for(String s: documentbatch.getDocIDCollection())
			{
				try {
					Document document = mgr.getObjectById(Document.class, s);
					validatedCollection.add(s);
				} catch (Exception e) {
					System.out.println("warning: unable to find doc with id " + s);
				}
			}
			documentbatch.setDocIDCollection(validatedCollection);
			System.out.println("validated:" + documentbatch.getDocIDCollection().size() + " docs");
			mgr.makePersistent(documentbatch);
		} finally {
			mgr.close();
		}
		return documentbatch;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param documentbatch the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateDocumentBatch")
	public DocumentBatch updateDocumentBatch(DocumentBatch documentbatch) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return null;
		}
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsDocumentBatch(documentbatch)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(documentbatch);
		} finally {
			mgr.close();
		}
		return documentbatch;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeDocumentBatch")
	public void removeDocumentBatch(@Named("id") String id) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return;
		}
		PersistenceManager mgr = getPersistenceManager();
		try {
			DocumentBatch documentbatch = mgr.getObjectById(
					DocumentBatch.class, id);
			mgr.deletePersistent(documentbatch);
		} finally {
			mgr.close();
		}
	}

	private boolean containsDocumentBatch(DocumentBatch documentbatch) {
		if(!Auth.isAuthorized(AccessLevel.ADMIN))
		{
			return false;
		}
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(DocumentBatch.class,
					documentbatch.getDocbatchID());
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
