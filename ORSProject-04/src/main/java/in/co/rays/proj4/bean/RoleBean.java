package in.co.rays.proj4.bean;

// Log4j 1.2.17 import
import org.apache.log4j.Logger;

/**
 * RoleBean represents role details in the system.
 * <p>
 * Used in access control and authorization modules.
 * Also implements DropdownListBean for UI dropdowns.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
public class RoleBean extends BaseBean {

    /**
     * Log4j logger for RoleBean.
     */
    private static final Logger log = Logger.getLogger(RoleBean.class);

    public static final int ADMIN = 1;
    public static final int STUDENT = 2;
    public static final int FACULTY = 3;
    public static final int KIOSK = 4;

    private String name;
    private String description;

    /**
     * Gets role name.
     * 
     * @return role name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets role name.
     * 
     * @param name role name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets role description.
     * 
     * @return role description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets role description.
     * 
     * @param description role description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns key for dropdown list.
     * 
     * @return role id as string
     */
    @Override
    public String getKey() {
        return String.valueOf(id);
    }

    /**
     * Returns value for dropdown list.
     * 
     * @return role name
     */
    @Override
    public String getValue() {
        return name;
    }
}
