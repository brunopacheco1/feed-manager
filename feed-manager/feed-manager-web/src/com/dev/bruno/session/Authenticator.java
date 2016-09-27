package com.dev.bruno.session;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.locator.ServiceLocator;
import com.dev.bruno.model.AppUser;
import com.dev.bruno.service.AppUserService;

@Name("authenticator")
public class Authenticator {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
    
    @In
    private Identity identity;
    
    @In
    private Credentials credentials;
    
    private AppUserService appUserService = ServiceLocator.getInstance().lookup(AppUserService.class);

	public boolean authenticate() {
		AppUser app = new AppUser();
		app.setLogin(credentials.getUsername());
		app.setPassword(credentials.getPassword());

		try {
			appUserService.login(app);
			
			return true;
		} catch (GenericException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

}
