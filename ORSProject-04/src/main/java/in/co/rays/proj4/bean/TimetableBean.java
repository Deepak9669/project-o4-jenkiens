package in.co.rays.proj4.bean;

import java.util.Date;

// Log4j 1.2.17 import
import org.apache.log4j.Logger;

/**
 * TimetableBean represents Exam Timetable details.
 * <p>
 * Stores course, subject, semester and exam timing information.
 * <p>
 * Used for data transfer and dropdown lists.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
public class TimetableBean extends BaseBean {

    /**
     * Log4j logger for TimetableBean.
     */
    private static final Logger log = Logger.getLogger(TimetableBean.class);

    /** Semester */
    private String semester;

    /** Description */
    private String description;

    /** Exam date */
    private Date examDate;

    /** Exam time */
    private String examTime;

    /** Course ID */
    private long courseId;

    /** Course name */
    private String courseName;

    /** Subject ID */
    private long subjectId;

    /** Subject name */
    private String subjectName;

    /**
     * Gets semester.
     *
     * @return semester
     */
    public String getSemester() {
        return semester;
    }

    /**
     * Sets semester.
     *
     * @param semester semester
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    /**
     * Gets description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets exam date.
     *
     * @return exam date
     */
    public Date getExamDate() {
        return examDate;
    }

    /**
     * Sets exam date.
     *
     * @param examDate exam date
     */
    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    /**
     * Gets exam time.
     *
     * @return exam time
     */
    public String getExamTime() {
        return examTime;
    }

    /**
     * Sets exam time.
     *
     * @param examTime exam time
     */
    public void setExamTime(String examTime) {
        this.examTime = examTime;
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
     * @param subjectId subject id
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
     * @param subjectName subject name
     */
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /**
     * Returns dropdown key.
     * 
     * @return timetable id
     */
    @Override
    public String getKey() {
        return String.valueOf(id);
    }

    /**
     * Returns display value for dropdown.
     * 
     * @return subject name + exam time
     */
    @Override
    public String getValue() {
        return subjectName + " - " + examTime;
    }
}
