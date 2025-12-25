package in.co.rays.proj4.bean;

import java.util.Date;

// Log4j 1.2.17 import
import org.apache.log4j.Logger;

/**
 * PatientBean represents Patient entity.
 * <p>
 * It stores patient related details such as name, date of visit,
 * mobile number and disease information.
 * <p>
 * It extends {@link BaseBean} and supports dropdown list functionality.
 *
 * @author Deepak Verma
 * @version 1.0
 */
public class PatientBean extends BaseBean {

    /**
     * Log4j logger for PatientBean.
     */
    private static final Logger log = Logger.getLogger(PatientBean.class);

    /** Patient name */
    private String name;

    /** Date of patient visit */
    private Date dateOfVisit;

    /** Patient mobile number */
    private String mobile;

    /** Patient disease */
    private String disease;

    /**
     * Gets patient name.
     *
     * @return patient name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets patient name.
     *
     * @param name patient name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets date of visit.
     *
     * @return date of visit
     */
    public Date getDateOfVisit() {
        return dateOfVisit;
    }

    /**
     * Sets date of visit.
     *
     * @param dateOfVisit date of visit
     */
    public void setDateOfVisit(Date dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    /**
     * Gets patient mobile number.
     *
     * @return mobile number
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets patient mobile number.
     *
     * @param mobile mobile number
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets patient disease.
     *
     * @return disease
     */
    public String getDisease() {
        return disease;
    }

    /**
     * Sets patient disease.
     *
     * @param disease patient disease
     */
    public void setDisease(String disease) {
        this.disease = disease;
    }

    /**
     * Returns display value for dropdown list.
     *
     * @return disease
     */
    @Override
    public String getValue() {
        return disease;
    }

    /**
     * Returns string representation of PatientBean.
     *
     * @return patient details
     */
    @Override
    public String toString() {
        return "PatientBean [name=" + name + ", dateOfVisit=" + dateOfVisit
                + ", mobile=" + mobile + ", disease=" + disease + "]";
    }

    /**
     * Returns key for dropdown list.
     *
     * @return disease
     */
    @Override
    public String getKey() {
        return disease;
    }
}
