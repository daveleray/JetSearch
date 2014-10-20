package com.dleray.cloudreviewer.structures;

import java.util.HashMap;
import java.util.HashSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.dleray.cloudreviewer.responses.ClientAdminPanel;

@PersistenceCapable
public class CloudReviewerProject {


	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String sharedDriveAccount;
	
	@Persistent(defaultFetchGroup="true")
	private HashMap<String,String> userToPermissionsMap=new HashMap();

	public String getSharedDriveAccount() {
		return sharedDriveAccount;
	}

	public void setSharedDriveAccount(String sharedDriveAccount) {
		this.sharedDriveAccount = sharedDriveAccount;
	}



	

	public HashMap<String, String> getUserToPermissionsMap() {
		return userToPermissionsMap;
	}

	public void setUserToPermissionsMap(HashMap<String, String> userToPermissionsMap) {
		this.userToPermissionsMap = userToPermissionsMap;
	}

	public ClientAdminPanel toAdminPanel()
	{
		HashMap<String,HashSet<String>> permissionsUsersMap=new HashMap();
		for(String user: userToPermissionsMap.keySet())
		{
			String thisPermission=userToPermissionsMap.get(user);
			if(permissionsUsersMap.get(thisPermission)==null)
			{
				permissionsUsersMap.put(thisPermission, new HashSet());
			}
			permissionsUsersMap.get(thisPermission).add(user);
		}
		
		ClientAdminPanel output=new ClientAdminPanel();
		output.setPermissionsUsersMap(permissionsUsersMap);
		output.setProjectAccount(sharedDriveAccount);
		return output;
	}



	
	
	
}
