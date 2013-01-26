package com.surgeryassist.services.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.services.interfaces.UserLoginService;

/**
 * Implementation of custom auth handler
 * 
 * @author Ankit Tyagi
 *
 */
@Service("userLoginService")
public class UserLoginServiceImpl implements UserLoginService {

	/**
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDetails springSecurityUser = null;
		ApplicationUser databaseUser = ApplicationUser.findApplicationUserByEmailAddress(username);
		if(databaseUser == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		springSecurityUser = convertApplicationUserToSpringUser(databaseUser);
		
		return springSecurityUser;
	}

	@Override
	public User convertApplicationUserToSpringUser(ApplicationUser applicationUser) {
		
		//TODO: need to figure out authorities
		
		User springUser = new User(applicationUser.getUserEmail(), 
				applicationUser.getUserPass(), 
				false, 
				false, 
				false, 
				false, 
				null);
		
		return springUser;
	}
	
	

}
