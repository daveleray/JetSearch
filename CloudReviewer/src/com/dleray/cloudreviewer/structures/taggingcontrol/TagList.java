package com.dleray.cloudreviewer.structures.taggingcontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.dleray.cloudreviewer.responses.ClientIssueTagCategory;
import com.dleray.cloudreviewer.responses.ClientIssueTagSubCategory;
import com.dleray.cloudreviewer.responses.ClientTagList;
import com.dleray.cloudreviewer.responses.ClientTaggingControl;
import com.dleray.cloudreviewer.structures.IssueTag;
import com.dleray.cloudreviewer.structures.IssueTagEndpoint;

@PersistenceCapable
public class TagList {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String controlID;
	
	@Persistent(defaultFetchGroup="true")
	private ArrayList<String> issueTagIDs=new ArrayList();

	public ArrayList<String> getIssueTagIDs() {
		return issueTagIDs;
	}

	public void setIssueTagIDs(ArrayList<String> issueTagIDs) {
		this.issueTagIDs = issueTagIDs;
	}

	


	public String getControlID() {
		return controlID;
	}

	public void setControlID(String controlID) {
		this.controlID = controlID;
	}

	public ClientTaggingControl toClient() {
		
		ClientTagList output=new ClientTagList();
		IssueTagEndpoint endpoint=new IssueTagEndpoint();
		HashMap<String,HashMap<String,HashSet<IssueTag>>> allIssues=new HashMap();
		for(String s: issueTagIDs)
		{
			IssueTag tag=endpoint.getIssueTag(s);
			HashMap<String,HashSet<IssueTag>> subCategoryMap=allIssues.get(tag.getCategory());
			if(subCategoryMap==null)
			{
				subCategoryMap=new HashMap();
			}
			HashSet<IssueTag> issueSet=subCategoryMap.get(tag.getSubCategory());
			if(issueSet==null)
			{
				issueSet=new HashSet();
			}
			issueSet.add(tag);
			subCategoryMap.put(tag.getSubCategory(), issueSet);
			allIssues.put(tag.getCategory(), subCategoryMap);
		}
		
		ArrayList<String> allCategories=new ArrayList(allIssues.keySet());
		Collections.sort(allCategories);
		issueTagComparator comparator=new issueTagComparator();
		
		for(String category: allCategories)
		{
			ClientIssueTagCategory outputCategory=new ClientIssueTagCategory();
			outputCategory.setCategoryName(category);
			ArrayList<String> subCategories=new ArrayList(allIssues.get(category).keySet());
			Collections.sort(subCategories);
			for(String subCategory: subCategories)
			{
				ClientIssueTagSubCategory outputSubCategory=new ClientIssueTagSubCategory();
				outputSubCategory.setSubCategoryName(subCategory);
				ArrayList<IssueTag> issueTags=new ArrayList(allIssues.get(category).get(subCategory));
				Collections.sort(issueTags,comparator);
				for(IssueTag t: issueTags)
				{
					outputSubCategory.getAllTags().add(t.toClientIssueTag());
				}
				outputCategory.getSubcategories().add(outputSubCategory);			
			}
			output.getCategories().add(outputCategory);
		}
		return output;
	}
	
	private class issueTagComparator implements Comparator<IssueTag>{

		@Override
		public int compare(IssueTag o1, IssueTag o2) {
			// TODO Auto-generated method stub
			return o1.getDisplayName().compareTo(o2.getDisplayName());
		}
		
	}
		
}
