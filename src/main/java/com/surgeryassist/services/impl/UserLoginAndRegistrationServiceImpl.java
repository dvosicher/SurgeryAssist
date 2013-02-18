package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.Authorities;
import com.surgeryassist.services.interfaces.UserLoginAndRegistrationService;

/**
 * Implementation of custom auth handler
 * 
 * @author Ankit Tyagi
 *
 */
@Service("userLoginAndRegistrationService")
public class UserLoginAndRegistrationServiceImpl implements UserLoginAndRegistrationService {

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	SaltSource saltSource;
	
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
	public UserDetails convertApplicationUserToSpringUser(ApplicationUser applicationUser) {
		
		//create collection of authorities
		Collection<GrantedAuthority> springAuthorities = new ArrayList<GrantedAuthority>();
		
		//add to spring based collection
		for(Authorities auth : applicationUser.getAuthorities()) {
			springAuthorities.add(auth);
		}
		
		//create the user to return
		User springUser = new User(applicationUser.getUserEmail(), 
				applicationUser.getUserPass(), 
				applicationUser.getIsEnabled(),
				applicationUser.getIsEnabled(),
				applicationUser.getIsEnabled(),
				applicationUser.getIsEnabled(),
				springAuthorities);

		return springUser;
	}

	@Override
	public void registerUser(ApplicationUser applicationUser) {
//		String encodedPass = passwordEncoder.encodePassword(applicationUser.getUserPass(), 
//				saltSource.getSalt(this.convertApplicationUserToSpringUser(applicationUser)));
//		applicationUser.setUserPass(encodedPass);
		
		//save to the database
		applicationUser.persist();
		applicationUser.flush();
		
	}
	
	
	

}
