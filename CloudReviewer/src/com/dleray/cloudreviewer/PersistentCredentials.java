package com.dleray.cloudreviewer;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.api.client.auth.oauth2.Credential;

@PersistenceCapable
public class PersistentCredentials {

	 	public PersistentCredentials(String user, String accessToken,
			String refreshToken, long expMilliseconds, long expSeconds) {
		super();
		this.user = user;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.expMilliseconds = expMilliseconds;
		this.expSeconds = expSeconds;
	}
		@PrimaryKey
	    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	    private String user;
	 
	 	@Persistent
	 	private String accessToken;

	 	@Persistent
	 	private String refreshToken;
	 	@Persistent
	 	private long expMilliseconds;
	 	@Persistent
	 	private long expSeconds;
		
	 	public PersistentCredentials(Credential C,String user)
	 	{
	 		this.user=user;
	 		this.accessToken=C.getAccessToken();
	 		this.refreshToken=C.getRefreshToken();
	 		this.expMilliseconds=C.getExpirationTimeMilliseconds();
	 		this.expSeconds=C.getExpiresInSeconds();
	 		
	 	}
	 	public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
		public String getRefreshToken() {
			return refreshToken;
		}
		public void setRefreshToken(String refreshToken) {
			this.refreshToken = refreshToken;
		}
		public long getExpMilliseconds() {
			return expMilliseconds;
		}
		public void setExpMilliseconds(long expMilliseconds) {
			this.expMilliseconds = expMilliseconds;
		}
		public long getExpSeconds() {
			return expSeconds;
		}
		public void setExpSeconds(long expSeconds) {
			this.expSeconds = expSeconds;
		}
	 	

}
