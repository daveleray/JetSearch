package com.dleray.cloudreviewer;

import com.dleray.cloudreviewer.structures.IssueTag;
import com.dleray.cloudreviewer.structures.IssueTagEndpoint;
import com.dleray.cloudreviewer.structures.TaggingPanel;
import com.dleray.cloudreviewer.structures.TaggingPanelEndpoint;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagList;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagListEndpoint;
import com.google.api.server.spi.response.CollectionResponse;

public class PanelHandler {

	public static void initDefaultPanel()
	{
		TaggingPanelEndpoint endpoint=new TaggingPanelEndpoint();
		try {
			endpoint.getTaggingPanel("taggingPanel-1");
		} catch (Exception e) {
			IssueTagEndpoint tagEndpoint=new IssueTagEndpoint();
			CollectionResponse<IssueTag> allIssueTags=tagEndpoint.listIssueTag("",1000);
			TaggingPanel defaultPanel=new TaggingPanel();
			
					TagListEndpoint tlEndpoint=new TagListEndpoint();
					TagList taglist=new TagList();
					taglist.setControlID("taggingList-1");
					for(IssueTag issetag: allIssueTags.getItems())
					{
						taglist.getIssueTagIDs().add(issetag.getTagID());
					}
					tlEndpoint.insertTagList(taglist);
			
					defaultPanel.setTaggingID("taggingPanel-1");
					defaultPanel.getTaggingControlIds().add(taglist.getControlID());
			endpoint.insertTaggingPanel(defaultPanel);
		}
		
	}
}
