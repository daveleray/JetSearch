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

@Api(name = "batchfolderendpoint", namespace = @ApiNamespace(ownerDomain = "dleray.com", ownerName = "dleray.com", packagePath = "cloudreviewer.structures"))
public class BatchFolderEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listBatchFolder")
	public CollectionResponse<BatchFolder> listBatchFolder(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<BatchFolder> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(BatchFolder.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<BatchFolder>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (BatchFolder obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<BatchFolder> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getBatchFolder")
	public BatchFolder getBatchFolder(@Named("id") String id) {
		PersistenceManager mgr = getPersistenceManager();
		BatchFolder batchfolder = null;
		try {
			batchfolder = mgr.getObjectById(BatchFolder.class, id);
		} finally {
			mgr.close();
		}
		return batchfolder;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param batchfolder the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertBatchFolder")
	public BatchFolder insertBatchFolder(BatchFolder batchfolder) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsBatchFolder(batchfolder)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(batchfolder);
		} finally {
			mgr.close();
		}
		return batchfolder;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param batchfolder the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateBatchFolder")
	public BatchFolder updateBatchFolder(BatchFolder batchfolder) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsBatchFolder(batchfolder)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(batchfolder);
		} finally {
			mgr.close();
		}
		return batchfolder;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeBatchFolder")
	public void removeBatchFolder(@Named("id") String id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			BatchFolder batchfolder = mgr.getObjectById(BatchFolder.class, id);
			mgr.deletePersistent(batchfolder);
		} finally {
			mgr.close();
		}
	}

	private boolean containsBatchFolder(BatchFolder batchfolder) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(BatchFolder.class, batchfolder.getBatchFolderID());
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
