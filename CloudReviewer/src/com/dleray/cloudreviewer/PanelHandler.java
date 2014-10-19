package com.dleray.cloudreviewer;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.dleray.cloudreviewer.structures.BatchFolder;
import com.dleray.cloudreviewer.structures.IssueTag;
import com.dleray.cloudreviewer.structures.TaggingPanel;
import com.dleray.cloudreviewer.structures.taggingcontrol.TagList;
import com.google.api.server.spi.response.CollectionResponse;

public class PanelHandler {

	public static Long initDefaultPanel()
	{
		System.out.println("initializing default panel");
	
		PersistenceManager pm=PMF.get().getPersistenceManager();
		Query q = pm.newQuery(TaggingPanel.class);
		
		List<TaggingPanel> output=(List<TaggingPanel>) q.execute();
		
		if(output.size()>0)
		{
			System.out.println("TaggingPanel already found. no need to init.  ");
			pm.close();
			return output.iterator().next().getTaggingID();
		}
		else			
		{
		
			Query q2 = pm.newQuery(IssueTag.class);
			
			List<IssueTag> allIssueTags=(List<IssueTag>) q2.execute();
			
			TaggingPanel defaultPanel=new TaggingPanel();

					TagList taglist=new TagList();
					taglist.setControlID("taggingList-1");
					for(IssueTag issetag: allIssueTags)
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
