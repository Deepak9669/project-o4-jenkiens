package in.co.rays.proj4.bean;

import java.util.Date;

// Log4j 1.2.17 import
import org.apache.log4j.Logger;

/**
 * StudentBean represents Student details in the system.
 * <p>
 * Stores personal information and college mapping.
 * Used for data transfer and dropdown representation.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
public class StudentBean extends BaseBean {

    /**
     * Log4j logger for StudentBean.
     */
    private static final Logger log = Logger.getLogger(StudentBean.class);

    private String firstName;
    private String lastName;
    private Date dob;
    private String gender;
    private String mobileNo;
    private String email;
    private long collegeId;
    private String collegeName;

    /**
     * Gets first name of student.
     * 
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name of student.
     * 
     * @param firstName student first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name of student.
     * 
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name of student.
     * 
     * @param lastName student last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets date of birth.
     * 
     * @return DOB
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
     * Gets email address.
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email address.
     * 
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets college ID.
     * 
     * @return college id
     */
    public long getCollegeId() {
        return collegeId;
    }

    /**
     * Sets college ID.
     * 
     * @param collegeId college id
     */
    public void setCollegeId(long collegeId) {
        this.collegeId = collegeId;
    }

    /**
     * Gets college name.
     * 
     * @return college name
     */
    public String getCollegeName() {
        return collegeName;
    }

    /**
     * Sets college name.
     * 
     * @param collegeName college name
     */
    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    /**
     * Returns key for dropdown list.
     * 
     * @return student id as string
     */
    @Override
    public String getKey() {
        return String.valueOf(id);
    }

    /**
     * Returns display value for dropdown list.
     * 
     * @return full name of student
     */
    @Override
    public String getValue() {
        return firstName + " " + lastName;
    }
}
