package com.dleray.cloudreviewer.responses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.dleray.cloudreviewer.Auth;

public class ClientAdminPanel {


	private ArrayList<String> accessLevels=new ArrayList();
    private String projectAccount;
	
    public ClientAdminPanel()
    {
    	for(Auth.AccessLevel level:Auth.AccessLevel.values())
    	{
    		accessLevels.add(level.toString());
    	}
    	
    }
    
    
	public ArrayList<String> getAccessLevels() {
		return accessLevels;
	}

	public void setAccessLevels(ArrayList<String> accessLevels) {
		this.accessLevels = accessLevels;
	}


	private HashMap<String, HashSet<String>> permissionsUsersMap=new HashMap();

	public String getProjectAccount() {
		return projectAccount;
	}

	public void setProjectAccount(String projectAccount) {
		this.projectAccount = projectAccount;
	}

	public HashMap<String, HashSet<String>> getPermissionsUsersMap() {
		return permissionsUsersMap;
	}

	public void setPermissionsUsersMap(
			HashMap<String, HashSet<String>> permissionsUsersMap) {
		this.permissionsUsersMap = permissionsUsersMap;
	}

	
	
}
