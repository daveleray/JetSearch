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

@Api(name = "documentmetadataendpoint", namespace = @ApiNamespace(ownerDomain = "dleray.com", ownerName = "dleray.com", packagePath = "cloudreviewer.structures"))
public class DocumentMetadataEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listDocumentMetadata")
	public CollectionResponse<DocumentMetadata> listDocumentMetadata(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<DocumentMetadata> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(DocumentMetadata.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<DocumentMetadata>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (DocumentMetadata obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<DocumentMetadata> builder()
				.setItems(execute).setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getDocumentMetadata")
	public DocumentMetadata getDocumentMetadata(@Named("id") String id) {
		PersistenceManager mgr = getPersistenceManager();
		DocumentMetadata documentmetadata = null;
		try {
			documentmetadata = mgr.getObjectById(DocumentMetadata.class, id);
		} finally {
			mgr.close();
		}
		return documentmetadata;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param documentmetadata the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertDocumentMetadata")
	public DocumentMetadata insertDocumentMetadata(
			DocumentMetadata documentmetadata) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsDocumentMetadata(documentmetadata)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(documentmetadata);
		} finally {
			mgr.close();
		}
		return documentmetadata;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param documentmetadata the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateDocumentMetadata")
	public DocumentMetadata updateDocumentMetadata(
			DocumentMetadata documentmetadata) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsDocumentMetadata(documentmetadata)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(documentmetadata);
		} finally {
			mgr.close();
		}
		return documentmetadata;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeDocumentMetadata")
	public void removeDocumentMetadata(@Named("id") String id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			DocumentMetadata documentmetadata = mgr.getObjectById(
					DocumentMetadata.class, id);
			mgr.deletePersistent(documentmetadata);
		} finally {
			mgr.close();
		}
	}

	private boolean containsDocumentMetadata(DocumentMetadata documentmetadata) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(DocumentMetadata.class,
					documentmetadata.getMetadataColumnID());
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
