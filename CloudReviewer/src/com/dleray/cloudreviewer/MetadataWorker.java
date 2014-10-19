package com.dleray.cloudreviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dleray.cloudreviewer.structures.Document;
import com.dleray.cloudreviewer.structures.DocumentMetadata;
import com.dleray.cloudreviewer.structures.MetadataHolder;
import com.google.appengine.api.datastore.Text;

public class MetadataWorker extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
	
	        
		Long holderID = Long.parseLong(req.getParameter("holderid"));
		Integer arrayStart = Integer.parseInt(req.getParameter("start"));
		Integer arrayEnd = Integer.parseInt(req.getParameter("end"));
		PersistenceManager mgr=PMF.get().getPersistenceManager();
		try{
		
			MetadataHolder holder =  mgr.getObjectById(MetadataHolder.class, holderID);
			
			
			String headerline=holder.getLinesOfMetadata().get(0).getValue();
			

			String delimiter=holder.getDelimiter();
			String[] allHeaders=headerline.split(holder.getDelimiter());
			ArrayList<String> allTextInputs=new ArrayList();
			for(int j=arrayStart;j<=arrayEnd;j++)
			{
				allTextInputs.add(holder.getLinesOfMetadata().get(j).getValue());
			}
			System.out.println("starting worker from:" + arrayStart + " to:" + arrayEnd);
			Integer successCount=0;
			Integer notfound=0;
			Integer dirty=0;
			for(int i=1;i<allTextInputs.size();i++)
			{
				String docInfo=allTextInputs.get(i);
				String[] data=docInfo.split(delimiter);
				String docID=data[0];
				
				if(docID==null || docID.contentEquals(""))
				{
					dirty+=1;
					System.out.println("skipping dirty input line");
					continue;
				}
				try {
					
					Document cloudDocument=DocHelper.getDocumentByID(docID, mgr);
					if(cloudDocument==null)
					{
						System.out.println("Could not find:" + docID);
						notfound+=1;
					}
					else
					{
						for(int j=1;j<data.length;j++)
						{
							String value=data[j];
							String header=allHeaders[j];
							DocumentMetadata metadata;
							try {
								metadata = DocHelper.getDocumentMetadataByID(header, mgr);
							} catch (Exception e) {
								metadata=new DocumentMetadata();
								metadata.setMetadataColumnID(header);
								metadata.setDisplayName(header);
								mgr.makePersistent(metadata);
							}
							
							if(cloudDocument.getMetadataAndValuesSearchable()==null)
							{
								cloudDocument.setMetadataAndValuesSearchable(new HashMap<String,String>());
							}
							if(cloudDocument.getMetadataAndValuesNonSearchable()==null)
							{
								cloudDocument.setMetadataAndValuesNonSearchable(new HashMap<String,Text>());
							}
							if(metadata==null)
							{
								Double a=5.0;
							}
							if(value.length()<500)
							{
								cloudDocument.getMetadataAndValuesSearchable().put(metadata.getMetadataColumnID(), value);
							}
							else
							{
								cloudDocument.getMetadataAndValuesNonSearchable().put(metadata.getMetadataColumnID(), new Text(value));
							}
							mgr.makePersistent(cloudDocument);
			
						
						}
						successCount+=1;
					}
					
				
					
				} catch (JDOObjectNotFoundException e) {
					notfound+=1;
				}
				
			}
			System.out.println("Total Attempt:" + (allTextInputs.size()-1));
			System.out.println("Success:" + successCount + " // Not Found:" + notfound + " // Dirty:" + dirty);
		}
			finally {
				mgr.close();
			}
			
		}
	}


