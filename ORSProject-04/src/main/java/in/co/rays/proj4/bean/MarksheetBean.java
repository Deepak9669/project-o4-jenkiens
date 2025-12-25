package in.co.rays.proj4.bean;

// Log4j 1.2.17 import
import org.apache.log4j.Logger;

/**
 * MarksheetBean represents student's marksheet details.
 * <p>
 * It stores marks of Physics, Chemistry and Maths along with student info.
 * Used for data transfer and dropdown functionality.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
public class MarksheetBean extends BaseBean {

    /**
     * Log4j logger for MarksheetBean.
     */
    private static final Logger log = Logger.getLogger(MarksheetBean.class);

    private String rollNo;
    private long studentId;
    private String name;
    private Integer physics;
    private Integer chemistry;
    private Integer maths;

    /**
     * Gets roll number of student.
     * 
     * @return roll number
     */
    public String getRollNo() {
        return rollNo;
    }

    /**
     * Sets roll number of student.
     * 
     * @param rollNo roll number
     */
    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    /**
     * Gets student ID.
     * 
     * @return student id
     */
    public long getStudentId() {
        return studentId;
    }

    /**
     * Sets student ID.
     * 
     * @param studentId student id
     */
    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    /**
     * Gets student name.
     * 
     * @return student name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets student name.
     * 
     * @param name student name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets physics marks.
     * 
     * @return physics marks
     */
    public Integer getPhysics() {
        return physics;
    }

    /**
     * Sets physics marks.
     * 
     * @param physics physics marks
     */
    public void setPhysics(Integer physics) {
        this.physics = physics;
    }

    /**
     * Gets chemistry marks.
     * 
     * @return chemistry marks
     */
    public Integer getChemistry() {
        return chemistry;
    }

    /**
     * Sets chemistry marks.
     * 
     * @param chemistry chemistry marks
     */
    public void setChemistry(Integer chemistry) {
        this.chemistry = chemistry;
    }

    /**
     * Gets maths marks.
     * 
     * @return maths marks
     */
    public Integer getMaths() {
        return maths;
    }

    /**
     * Sets maths marks.
     * 
     * @param maths maths marks
     */
    public void setMaths(Integer maths) {
        this.maths = maths;
    }

    /**
     * Returns key for dropdown list.
     * 
     * @return roll number
     */
    @Override
    public String getKey() {
        return rollNo;
    }

    /**
     * Returns display value for dropdown list.
     * 
     * @return student name with roll no
     */
    @Override
    public String getValue() {
        return rollNo + " - " + name;
    }
}
