package in.co.rays.proj4.bean;

import java.util.Date;

// Log4j 1.2.17 import
import org.apache.log4j.Logger;

/**
 * FacultyBean represents Faculty information in the system.
 * <p>
 * It contains personal details and mapping with College, Course, and Subject.
 * This bean is also used for dropdown data representation.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
public class FacultyBean extends BaseBean {

    /**
     * Log4j logger for FacultyBean.
     */
    private static final Logger log = Logger.getLogger(FacultyBean.class);

    private String firstName;
    private String lastName;
    private Date dob;
    private String gender;
    private String mobileNo;
    private String email;
    private long collegeId;
    private String collegeName;
    private long courseId;
    private String courseName;
    private long subjectId;
    private String subjectName;

    /**
     * Gets first name of faculty.
     * 
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name of faculty.
     * 
     * @param firstName the faculty first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name of faculty.
     * 
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name of faculty.
     * 
     * @param lastName the faculty last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * @param dob the date of birth
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
     * @param gender the gender
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
     * @param mobileNo the mobile number
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * Gets email address.
     * 
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email address.
     * 
     * @param email the email address
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
     * @param collegeId the college id
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
     * @param collegeName the college name
     */
    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    /**
     * Gets course ID.
     * 
     * @return course id
     */
    public long getCourseId() {
        return courseId;
    }

    /**
     * Sets course ID.
     * 
     * @param courseId the course id
     */
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    /**
     * Gets course name.
     * 
     * @return course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets course name.
     * 
     * @param courseName the course name
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Gets subject ID.
     * 
     * @return subject id
     */
    public long getSubjectId() {
        return subjectId;
    }

    /**
     * Sets subject ID.
     * 
     * @param subjectId the subject id
     */
    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * Gets subject name.
     * 
     * @return subject name
     */
    public String getSubjectName() {
        return subjectName;
    }

    /**
     * Sets subject name.
     * 
     * @param subjectName the subject name
     */
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /**
     * Returns key for dropdown list.
     * 
     * @return faculty id as String
     */
    @Override
    public String getKey() {
        return String.valueOf(id);
    }

    /**
     * Returns value for dropdown list.
     * 
     * @return faculty full name
     */
    @Override
    public String getValue() {
        return firstName + " " + lastName;
    }
}
