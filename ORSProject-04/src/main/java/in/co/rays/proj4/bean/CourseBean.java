package in.co.rays.proj4.bean;

// Log4j 1.2.17 import
import org.apache.log4j.Logger;

/**
 * CourseBean represents the Course entity.
 * <p>
 * It contains basic details of a course like name, duration and description.
 * It also supports dropdown list functionality using key-value mapping.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
public class CourseBean extends BaseBean {

    // Log4j 1.2.17 Logger (NO logic change)
    private static final Logger log = Logger.getLogger(CourseBean.class);

    private String name;
    private String duration;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getKey() {
        return String.valueOf(id);
    }

    @Override
    public String getValue() {
        return name;
    }
}
