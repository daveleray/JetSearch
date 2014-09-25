package com.dleray.cloudreviewer;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		//c PersistentCredentials(String user, String accessToken,
	//	String refreshToken, long expMilliseconds, long expSeconds)
		PersistentCredentials credent=new PersistentCredentials("imsquinn@gmail.com",
				"ya29.gQARv69adpc66rA-07dJr7fRPaVoq5ozRX5-i3s93U-Jp34jtIh8FhGb",
				"1/eCE6IqqQj_7hYPiC5F5idFCU56HLXfDDub7mBsS_q5Q",
				Long.MAX_VALUE,3599);
		
        try {
            pm.makePersistent(credent);
        } finally {
            pm.close();
        }
        
	}

}
