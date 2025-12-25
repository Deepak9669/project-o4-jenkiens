package in.co.rays.proj4.bean;

// Log4j 1.2.17 import
import org.apache.log4j.Logger;

/**
 * SubjectBean represents Subject entity in the system.
 * It stores subject details and its mapping with Course.
 * 
 * Used for data transfer and dropdown usage.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
public class SubjectBean extends BaseBean {

    /**
     * Log4j logger for SubjectBean.
     */
    private static final Logger log = Logger.getLogger(SubjectBean.class);

    private String name;
    private long courseId;
    private String courseName;
    private String description;

    /**
     * Gets subject name.
     * 
     * @return subject name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets subject name.
     * 
     * @param name subject name
     */
    public void setName(String name) {
        this.name = name;
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
     * @param courseId course id
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
     * @param courseName course name
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Gets subject description.
     * 
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets subject description.
     * 
     * @param description subject description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns dropdown key.
     * 
     * @return subject id
     */
    @Override
    public String getKey() {
        return String.valueOf(id);
    }

    /**
     * Returns dropdown display value.
     * 
     * @return subject name
     */
    @Override
    public String getValue() {
        return name;
    }
}
