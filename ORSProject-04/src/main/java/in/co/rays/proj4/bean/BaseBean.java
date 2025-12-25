package in.co.rays.proj4.bean;

import java.sql.Timestamp;

// Log4j 1.2.17 imports
import org.apache.log4j.Logger;

/**
 * Base bean class that contains common audit fields for all entities.
 * <p>
 * All specific bean classes should extend this class to inherit:
 * <ul>
 *   <li>Primary key ID</li>
 *   <li>Created by / Modified by</li>
 *   <li>Created datetime / Modified datetime</li>
 * </ul>
 *
 * @author Deepak Verma
 * @version 1.0
 */
public abstract class BaseBean implements DropdownListBean {

    // Log4j 1.2.17 Logger (NO logic change)
    private static final Logger log = Logger.getLogger(BaseBean.class);

    /**
     * Primary key ID.
     */
    protected long id;

    /**
     * User who created the record.
     */
    protected String createdBy;

    /**
     * User who last modified the record.
     */
    protected String modifiedBy;

    /**
     * Timestamp when the record was created.
     */
    protected Timestamp createdDatetime;

    /**
     * Timestamp when the record was last modified.
     */
    protected Timestamp modifiedDatetime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Timestamp getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Timestamp createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public Timestamp getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(Timestamp modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }
}
