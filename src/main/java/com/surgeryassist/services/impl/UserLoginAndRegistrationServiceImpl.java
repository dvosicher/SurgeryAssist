package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.RegistrationResult;
import com.surgeryassist.core.UserTypeCode;
import com.surgeryassist.core.VerificationStatus;
import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.Authorities;
import com.surgeryassist.core.entity.ContactInfo;
import com.surgeryassist.core.entity.Location;
import com.surgeryassist.core.entity.StateCode;
import com.surgeryassist.core.entity.UserInfo;
import com.surgeryassist.services.interfaces.UserLoginAndRegistrationService;
import com.surgeryassist.util.SurgeryAssistUtil;

/**
 * Implementation of custom auth handler
 * 
 * @author Ankit Tyagi
 */
@Service("userLoginAndRegistrationService")
public class UserLoginAndRegistrationServiceImpl implements UserLoginAndRegistrationService {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	SaltSource saltSource;

	public static final Integer APPLICATION_IDENTIFIER = new Integer(1);

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDetails springSecurityUser = null;
		ApplicationUser databaseUser = ApplicationUser.findApplicationUserByEmailAddress(username);

		//TODO: use this when we have an admin interface to 'verify' surgeons
		//ApplicationUser databaseUser = ApplicationUser.findValidApplicationUserByEmail(username);

		if(databaseUser == null) {
			throw new UsernameNotFoundException("User not found");
		}

		databaseUser = (ApplicationUser) SurgeryAssistUtil.setLastModifiedInfo(databaseUser);
		databaseUser.merge();

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
	@Transactional
	public RegistrationResult registerUser(ApplicationUser applicationUser, UserInfo userInfo, ContactInfo contactInfo, Location location) {

		//get the user from the DB BEFORE the user has been registered
		ApplicationUser existingUser = 
				ApplicationUser.findApplicationUserByEmailAddress(applicationUser.getUserEmail());
		
		//go ahead and register the user
		if(existingUser != null) {
			if(existingUser.getUserID() > 0) {

				//create error messages for FacesContext
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_ERROR,"User Already Exists", "Try logging in instead"));

				return RegistrationResult.ALREADY_EXISTS;
			}
		}
		//create an insertable applicationUser
		applicationUser = this.createDefaultApplicationUser(applicationUser, userInfo, contactInfo, location);

		//salt it
		Object salt = saltSource.getSalt(
				new User(applicationUser.getUserEmail(), 
						applicationUser.getUserPass(), 
						applicationUser.getIsEnabled(),
						applicationUser.getIsEnabled(),
						applicationUser.getIsEnabled(),
						applicationUser.getIsEnabled(),
						new ArrayList<GrantedAuthority>()));

		String encodedPass = passwordEncoder.encodePassword(applicationUser.getUserPass(), salt);
		applicationUser.setUserPass(encodedPass);

		//save to the database
		applicationUser.persist();
		applicationUser.flush();

		return RegistrationResult.SUCCESS;
		
	}

	@Override
	public ApplicationUser createDefaultApplicationUser(
			ApplicationUser newApplicationUser, UserInfo userInfo,
			ContactInfo contactInfo, Location location) {

		contactInfo = (ContactInfo) SurgeryAssistUtil.setAllHistoricalInfo(contactInfo);
		location = (Location) SurgeryAssistUtil.setAllHistoricalInfo(location);
		userInfo = (UserInfo) SurgeryAssistUtil.setAllHistoricalInfo(userInfo);
		newApplicationUser = (ApplicationUser) SurgeryAssistUtil.setAllHistoricalInfo(newApplicationUser);

		//persist location and contact info to save values
		contactInfo.persist();
		contactInfo.flush();

		location.persist();
		location.flush();

		//build the userInfo
		userInfo.setLocationId(location);
		userInfo.setContactInfoId(contactInfo);
		userInfo.persist();
		userInfo.flush();

		newApplicationUser.setUserInfoId(userInfo);
		newApplicationUser.setIsEnabled(Boolean.TRUE);
		newApplicationUser.setVerificationStatus(VerificationStatus.WAITING_VERIFICATION);

		return newApplicationUser;
	}


	@Override
	public Map<String, List<SelectItem>> getDropdownMenuValues() {
		//declare map, and lists to go into map
		Map<String, List<SelectItem>> mapOfSelectItems = new HashMap<String, List<SelectItem>>();
		List<SelectItem> stateCodeList = new ArrayList<SelectItem>();
		List<SelectItem> userTypeCodeList = new ArrayList<SelectItem>();
		List<SelectItem> verificationStatusList = new ArrayList<SelectItem>();

		//get state codes, convert them into SelectItems, and put them into map
		List<StateCode> stateCodesEntityList = StateCode.findAllStateCodes();
		for(StateCode stateCode : stateCodesEntityList) {
			stateCodeList.add(new SelectItem(stateCode, stateCode.getStateName()));
		}
		mapOfSelectItems.put("stateCode", stateCodeList);

		//get user type codes
		for(UserTypeCode userTypeCode : UserTypeCode.values()) {
			userTypeCodeList.add(new SelectItem(userTypeCode, StringUtils.capitalize(userTypeCode.name().toLowerCase())));
		}
		mapOfSelectItems.put("userTypeCode", userTypeCodeList);

		//get verification status
		for(VerificationStatus verificationStatus : VerificationStatus.values()) {
			verificationStatusList.add(new SelectItem(verificationStatus, StringUtils.capitalize(verificationStatus.name())));
		}
		mapOfSelectItems.put("verificationStatus", verificationStatusList);

		return mapOfSelectItems;
	}

}
