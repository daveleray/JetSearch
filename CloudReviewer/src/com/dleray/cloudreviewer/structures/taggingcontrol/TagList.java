package com.dleray.cloudreviewer.structures.taggingcontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.dleray.cloudreviewer.PMF;
import com.dleray.cloudreviewer.responses.ClientIssueTagCategory;
import com.dleray.cloudreviewer.responses.ClientIssueTagSubCategory;
import com.dleray.cloudreviewer.responses.ClientTagList;
import com.dleray.cloudreviewer.structures.IssueTag;

@PersistenceCapable
public class TagList {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String controlID;
	
	@Persistent
	private String displayName;
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

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

	public ClientTagList toClient() {
		
		ClientTagList output=new ClientTagList();
		PersistenceManager pm=PMF.get().getPersistenceManager();
		HashMap<String,HashMap<String,HashSet<IssueTag>>> allIssues=new HashMap();
		for(String s: issueTagIDs)
		{
			IssueTag tag=pm.getObjectById(IssueTag.class,Long.parseLong(s));
			HashMap<String,HashSet<IssueTag>> subCategoryMap=allIssues.get(tag.getCategoryDisplay());
			if(subCategoryMap==null)
			{
				subCategoryMap=new HashMap();
			}
			HashSet<IssueTag> issueSet=subCategoryMap.get(tag.getSubCategoryDisplay());
			if(issueSet==null)
			{
				issueSet=new HashSet();
			}
			issueSet.add(tag);
			subCategoryMap.put(tag.getSubCategoryDisplay(), issueSet);
			allIssues.put(tag.getCategoryDisplay(), subCategoryMap);
		}
		output.setId(this.controlID);
		
		if(displayName!=null)
		{
			output.setDisplayName(displayName);
		}
		else
		{
			output.setDisplayName(this.controlID);
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
