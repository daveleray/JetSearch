package com.dleray.cloudreviewer;

import javax.jdo.PersistenceManager;

import com.dleray.cloudreviewer.endpoints.IssueTagEndpoint;
import com.dleray.cloudreviewer.endpoints.TagListEndpoint;
import com.dleray.cloudreviewer.endpoints.TaggingPanelEndpoint;
import com.dleray.cloudreviewer.structures.IssueTag;
import com.dleray.cloudreviewer.structures.TaggingPanel;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagList;
import com.google.api.server.spi.response.CollectionResponse;

public class PanelHandler {

	public static Long initDefaultPanel()
	{
		System.out.println("initializing default panel");
		TaggingPanelEndpoint endpoint=new TaggingPanelEndpoint();
		CollectionResponse<TaggingPanel> output=endpoint.listTaggingPanel("", 1000);
		if(output.getItems().size()>0)
		{
			System.out.println("TaggingPanel already found. no need to init.  ");
			return output.getItems().iterator().next().getTaggingID();
		}
		else			
		{
			IssueTagEndpoint tagEndpoint=new IssueTagEndpoint();
			CollectionResponse<IssueTag> allIssueTags=tagEndpoint.listIssueTag("",1000);
			
			
			TaggingPanel defaultPanel=new TaggingPanel();
			PersistenceManager pm=PMF.get().getPersistenceManager();
				
					TagList taglist=new TagList();
					taglist.setControlID("taggingList-1");
					for(IssueTag issetag: allIssueTags.getItems())
					{
						taglist.getIssueTagIDs().add(issetag.getId()+"");
					}
				
					pm.makePersistent(taglist);
			
					defaultPanel.getTaggingControlIds().add(taglist.getControlID());
					
					pm.makePersistent(defaultPanel);
					System.out.println("Made default panel");
					System.out.println("Default ID:" + defaultPanel.getTaggingID());
					pm.close();
					return defaultPanel.getTaggingID();
		}
			
			
	
		
		
	}
}
