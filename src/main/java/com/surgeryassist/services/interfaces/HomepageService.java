package com.surgeryassist.services.interfaces;

import java.util.List;

import com.surgeryassist.core.UserTypeCode;
import com.surgeryassist.core.dto.HomepageDataDTO;
import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.Bookings;

/**
 * Interface for homepage content 
 * generation.
 * 
 * @author Ankit Tyagi
 */
public interface HomepageService {

	/**
	 * Returns an Enum of {@link UserTypeCode} that
	 * tells whether the user is a Surgeon,
	 * ASC, or a delegate account.
	 * @return {@link UserTypeCode} of logged in user, otherwise null.
	 */
	public UserTypeCode isUserSurgeonOrASC();

	/**
	 * Returns a list of {@link Bookings}
	 * that are in pending status for the
	 * currently logged in user
	 * @param loggedInUser The user that is currently logged in
	 * @return {@link List} of {@link Bookings}, or empty list
	 */
	public List<Bookings> getPendingBookings(ApplicationUser loggedInUser); 
	
	/**
	 * Returns a list of {@link Bookings}
	 * that are in confirmed status for the
	 * currently logged in user
	 * @param loggedInUser The user that is currently logged in
	 * @return {@link List} of {@link Bookings}, or empty list
	 */
	public List<Bookings> getConfirmedBookings(ApplicationUser loggedInUser);
	
	/**
	 * Returns a list of {@link ApplicationUser}
	 * corresponding to the ASCs with whom the surgeon 
	 * has recently booked
	 * @param loggedInUser The user that is currently logged in
	 * @return {@link List} of {@link ApplicationUser}, or empty list
	 */
	public List<ApplicationUser> getRecentASCs(ApplicationUser loggedInUser);
	
	/**
	 * Returns a list of {@link ApplicationUser}
	 * corresponding to the ASCs that the surgeon 
	 * has favorited or declared his affinity for
	 * @param loggedInUser The user that is currently logged in
	 * @return {@link List} of {@link ApplicationUser}, or empty list
	 */
	public List<ApplicationUser> getFavoriteASCs(ApplicationUser loggedInUser);
	
	/**
	 * Populates the data object that contains all the necessary data for
	 * the homepage. Will not repopulate if the object exists.
	 * @param homepageData The {@link HomepageDataDTO} object, incase it doesn't
	 * 	need to be repopulated
	 * @return A fully populated {@link HomepageDataDTO} object.
	 */
	public HomepageDataDTO populateHomepageInfo(HomepageDataDTO homepageData);
	
	/**
	 * Force refresh of all homepage data.
	 * @param homepageData The existing {@link HomepageDataDTO}
	 * @return A fully populated {@link HomepageDataDTO}
	 */
	public HomepageDataDTO refreshSurgeonInfo(HomepageDataDTO homepageData);

}
