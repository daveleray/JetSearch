package com.dleray.cloudreviewer.structures;

import java.util.HashMap;
import java.util.HashSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.dleray.cloudreviewer.Auth;

public class CloudReviewerProject {


	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String sharedDriveAccount;
	
	@Persistent(defaultFetchGroup="true")
	private HashMap<Auth.AccessLevel,HashSet<String>> permissionsUsersMap=new HashMap();

	public String getSharedDriveAccount() {
		return sharedDriveAccount;
	}

	public void setSharedDriveAccount(String sharedDriveAccount) {
		this.sharedDriveAccount = sharedDriveAccount;
	}

	public HashMap<Auth.AccessLevel, HashSet<String>> getPermissionsUsersMap() {
		return permissionsUsersMap;
	}

	public void setPermissionsUsersMap(
			HashMap<Auth.AccessLevel, HashSet<String>> permissionsUsersMap) {
		this.permissionsUsersMap = permissionsUsersMap;
	}

	



	
	
	
}
