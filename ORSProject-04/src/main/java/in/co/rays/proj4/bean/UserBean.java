package in.co.rays.proj4.bean;

import java.util.Date;

// Log4j 1.2.17 import
import org.apache.log4j.Logger;

/**
 * UserBean represents user information in the system.
 * <p>
 * It is used for authentication, authorization and user profile data.
 * <p>
 * Implements DropdownListBean through BaseBean.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
public class UserBean extends BaseBean {

    /**
     * Log4j logger for UserBean.
     */
    private static final Logger log = Logger.getLogger(UserBean.class);

    /** First name */
    private String firstName;

    /** Last name */
    private String lastName;

    /** Login (email / username) */
    private String login;

    /** User password */
    private String password;

    /** Confirm password */
    private String confirmPassword;

    /** Date of birth */
    private Date dob;

    /** Mobile number */
    private String mobileNo;

    /** Role ID */
    private long roleId;

    /** Gender */
    private String gender;

    /**
     * Gets first name.
     * 
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     * 
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     * 
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     * 
     * @param lastName last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets login (email / username).
     * 
     * @return login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets login (email / username).
     * 
     * @param login login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Gets user password.
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets user password.
     * 
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets confirm password.
     * 
     * @return confirm password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets confirm password.
     * 
     * @param confirmPassword confirm password
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Gets date of birth.
     * 
     * @return date of birth
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Sets date of birth.
     * 
     * @param dob date of birth
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * Gets mobile number.
     * 
     * @return mobile number
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets mobile number.
     * 
     * @param mobileNo mobile number
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * Gets role ID.
     * 
     * @return role id
     */
    public long getRoleId() {
        return roleId;
    }

    /**
     * Sets role ID.
     * 
     * @param roleId role id
     */
    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    /**
     * Gets gender.
     * 
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets gender.
     * 
     * @param gender gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Dropdown key (id).
     * 
     * @return user id as string
     */
    @Override
    public String getKey() {
        return String.valueOf(id);
    }

    /**
     * Dropdown display value (full name).
     * 
     * @return full name
     */
    @Override
    public String getValue() {
        return firstName + " " + lastName;
    }
}
