package com.dleray.cloudreviewer;

import com.dleray.cloudreviewer.structures.Document;
import com.dleray.cloudreviewer.structures.DocumentBatch;
import com.dleray.cloudreviewer.structures.DocumentBatchEndpoint;
import com.dleray.cloudreviewer.structures.DocumentEndpoint;
import com.google.api.server.spi.response.CollectionResponse;

public class BatchHandler {

	public static DocumentBatch getDefaultBatch()
	{
		System.out.println("getting default batch");
		/*String defaultID="-1";
		
		DocumentBatchEndpoint endpoint=new DocumentBatchEndpoint();
		try {
			if(endpoint.getDocumentBatch(defaultID)!=null)
			{
				return endpoint.getDocumentBatch(defaultID);
			}
		} catch (Exception e) {
			
			
		}*/
		
		DocumentBatchEndpoint endpoint=new DocumentBatchEndpoint();
		
		try {
			DocumentBatch edefault=endpoint.getDocumentBatch("-1");
			System.out.println("got default batch");
			return edefault;
			
		} catch (Exception e1) {
			DocumentEndpoint docEndpoint=new DocumentEndpoint();
			System.out.println("endpoints setup");
			CollectionResponse<Document> allDocs=docEndpoint.listDocument(null,null);
			System.out.println("beginning doc loop");
			DocumentBatch batch=new DocumentBatch();
			batch.setDocbatchID("-1");
			for(Document d: allDocs.getItems())
			{
				batch.addDocIDToBatch(d.getDocumentIdentifier());
			}
			System.out.println("done loop");
			try {
				endpoint.insertDocumentBatch(batch);
				System.out.println("inserted batch");
			} catch (Exception e) {
				e.printStackTrace(System.out);
				endpoint.updateDocumentBatch(batch);
			}
			return batch;
		}
		

	}
}
