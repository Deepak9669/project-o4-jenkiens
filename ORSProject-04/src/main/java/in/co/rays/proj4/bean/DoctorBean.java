package in.co.rays.proj4.bean;

import java.util.Date;

// Log4j 1.2.17 import
import org.apache.log4j.Logger;

/**
 * DoctorBean represents Doctor entity.
 * <p>
 * It stores doctor related information such as name, date of birth,
 * mobile number and area of expertise.
 * <p>
 * It extends {@link BaseBean} and supports dropdown list functionality.
 *
 * @author Deepak Verma
 * @version 1.0
 */
public class DoctorBean extends BaseBean {

    /**
     * Log4j logger for DoctorBean class.
     */
    private static final Logger log = Logger.getLogger(DoctorBean.class);

    /** Doctor name */
    private String name;

    /** Doctor date of birth */
    private Date dateOfBirth;

    /** Doctor mobile number */
    private String mobile;

    /** Doctor expertise */
    private String expertise;

    /**
     * Gets doctor name.
     *
     * @return doctor name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets doctor name.
     *
     * @param name doctor name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets doctor date of birth.
     *
     * @return date of birth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets doctor date of birth.
     *
     * @param dateOfBirth doctor date of birth
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets doctor mobile number.
     *
     * @return mobile number
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets doctor mobile number.
     *
     * @param mobile mobile number
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets doctor expertise.
     *
     * @return expertise
     */
    public String getExpertise() {
        return expertise;
    }

    /**
     * Sets doctor expertise.
     *
     * @param expertise doctor expertise
     */
    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    /**
     * Returns display value for dropdown list.
     *
     * @return expertise
     */
    @Override
    public String getValue() {
        return expertise;
    }

    /**
     * Returns string representation of DoctorBean.
     *
     * @return string details of doctor
     */
    @Override
    public String toString() {
        return "DoctorBean [name=" + name + ", dateOfBirth=" + dateOfBirth
                + ", mobile=" + mobile + ", expertise=" + expertise + "]";
    }

    /**
     * Returns key value for dropdown list.
     *
     * @return expertise
     */
    @Override
    public String getKey() {
        return expertise;
    }
