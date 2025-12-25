package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.bean.FacultyBean;
import in.co.rays.proj4.bean.SubjectBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * Faculty Model class for handling database operations of Faculty.
 * Supports CRUD, search, and find operations.
 * 
 * @author Deepak
 */
public class FacultyModel {

    /** Logger instance for FacultyModel */
    private static Logger log = Logger.getLogger(FacultyModel.class);

    /**
     * Get next primary key from database.
     */
    public Integer nextPk() throws DatabaseException {

        Connection conn = null;
        int pk = 0;

        log.debug("nextPk() started");

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement("select max(id) from st_faculty");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                pk = rs.getInt(1);
            }

            rs.close();
            pstmt.close();

            log.debug("Next PK generated : " + (pk + 1));

        } catch (Exception e) {
            log.error("Exception in nextPk()", e);
            throw new DatabaseException("Exception : Exception in getting PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk + 1;
    }

    /**
     * Add new Faculty record.
     */
    public long add(FacultyBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;
        int pk = 0;

        log.info("add() called for Faculty Email : " + bean.getEmail());

        // Set College Name
        CollegeModel collegeModel = new CollegeModel();
        CollegeBean collegeBean = collegeModel.findByPk(bean.getCollegeId());
        bean.setCollegeName(collegeBean.getName());

        // Set Course Name
        CourseModel courseModel = new CourseModel();
        CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
        bean.setCourseName(courseBean.getName());

        // Set Subject Name
        SubjectModel subjectModel = new SubjectModel();
        SubjectBean subjectBean = subjectModel.findByPk(bean.getSubjectId());
        bean.setSubjectName(subjectBean.getName());

        // Check for duplicate email
        FacultyBean existBean = findByEmail(bean.getEmail());
        if (existBean != null) {
            log.warn("Duplicate Email found : " + bean.getEmail());
            throw new DuplicateRecordException("Email Id already exists");
        }

        try {
            pk = nextPk();
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                "insert into st_faculty values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            pstmt.setInt(1, pk);
            pstmt.setString(2, bean.getFirstName());
            pstmt.setString(3, bean.getLastName());
            pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(5, bean.getGender());
            pstmt.setString(6, bean.getMobileNo());
            pstmt.setString(7, bean.getEmail());
            pstmt.setLong(8, bean.getCollegeId());
            pstmt.setString(9, bean.getCollegeName());
            pstmt.setLong(10, bean.getCourseId());
            pstmt.setString(11, bean.getCourseName());
            pstmt.setLong(12, bean.getSubjectId());
            pstmt.setString(13, bean.getSubjectName());
            pstmt.setString(14, bean.getCreatedBy());
            pstmt.setString(15, bean.getModifiedBy());
            pstmt.setTimestamp(16, bean.getCreatedDatetime());
            pstmt.setTimestamp(17, bean.getModifiedDatetime());

            pstmt.executeUpdate();
            conn.commit();

            log.info("Faculty added successfully with PK : " + pk);

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in add()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Rollback error : " + ex.getMessage());
            }
            throw new ApplicationException("Exception in adding Faculty");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return pk;
    }

    /**
     * Update Faculty record.
     */
    public void update(FacultyBean bean)
            throws ApplicationException, DuplicateRecordException {

        log.info("update() called for Faculty ID : " + bean.getId());

        // Set related names again
        CollegeModel collegeModel = new CollegeModel();
        bean.setCollegeName(collegeModel.findByPk(bean.getCollegeId()).getName());

        CourseModel courseModel = new CourseModel();
        bean.setCourseName(courseModel.findByPk(bean.getCourseId()).getName());

        SubjectModel subjectModel = new SubjectModel();
        bean.setSubjectName(subjectModel.findByPk(bean.getSubjectId()).getName());

        FacultyBean existBean = findByEmail(bean.getEmail());
        if (existBean != null && existBean.getId() != bean.getId()) {
            log.warn("Duplicate Email during update : " + bean.getEmail());
            throw new DuplicateRecordException("Email Id already exists");
        }

        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                "update st_faculty set first_name=?, last_name=?, dob=?, gender=?, mobile_no=?, email=?, "
              + "college_id=?, college_name=?, course_id=?, course_name=?, "
              + "subject_id=?, subject_name=?, created_by=?, modified_by=?, "
              + "created_datetime=?, modified_datetime=? where id=?");

            pstmt.setString(1, bean.getFirstName());
            pstmt.setString(2, bean.getLastName());
            pstmt.setDate(3, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(4, bean.getGender());
            pstmt.setString(5, bean.getMobileNo());
            pstmt.setString(6, bean.getEmail());
            pstmt.setLong(7, bean.getCollegeId());
            pstmt.setString(8, bean.getCollegeName());
            pstmt.setLong(9, bean.getCourseId());
            pstmt.setString(10, bean.getCourseName());
            pstmt.setLong(11, bean.getSubjectId());
            pstmt.setString(12, bean.getSubjectName());
            pstmt.setString(13, bean.getCreatedBy());
            pstmt.setString(14, bean.getModifiedBy());
            pstmt.setTimestamp(15, bean.getCreatedDatetime());
            pstmt.setTimestamp(16, bean.getModifiedDatetime());
            pstmt.setLong(17, bean.getId());

            pstmt.executeUpdate();
            conn.commit();

            log.info("Faculty updated successfully ID : " + bean.getId());

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in update()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Rollback error : " + ex.getMessage());
            }
            throw new ApplicationException("Exception in updating Faculty");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Delete Faculty.
     */
    public void delete(FacultyBean bean) throws ApplicationException {

        Connection conn = null;
        log.info("delete() called for Faculty ID : " + bean.getId());

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt =
                    conn.prepareStatement("delete from st_faculty where id = ?");
            pstmt.setLong(1, bean.getId());

            pstmt.executeUpdate();
            conn.commit();

            log.info("Faculty deleted successfully ID : " + bean.getId());

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in delete()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Rollback error : " + ex.getMessage());
            }
            throw new ApplicationException("Exception in deleting Faculty");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Find Faculty by Primary Key.
     */
    public FacultyBean findByPk(long pk) throws ApplicationException {

        log.debug("findByPk() called PK : " + pk);

        FacultyBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement("select * from st_faculty where id=?");
            pstmt.setLong(1, pk);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                bean = new FacultyBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setDob(rs.getDate(4));
                bean.setGender(rs.getString(5));
                bean.setMobileNo(rs.getString(6));
                bean.setEmail(rs.getString(7));
                bean.setCollegeId(rs.getLong(8));
                bean.setCollegeName(rs.getString(9));
                bean.setCourseId(rs.getLong(10));
                bean.setCourseName(rs.getString(11));
                bean.setSubjectId(rs.getLong(12));
                bean.setSubjectName(rs.getString(13));
                bean.setCreatedBy(rs.getString(14));
                bean.setModifiedBy(rs.getString(15));
                bean.setCreatedDatetime(rs.getTimestamp(16));
                bean.setModifiedDatetime(rs.getTimestamp(17));
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByPk()", e);
            throw new ApplicationException("Exception in finding Faculty by PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    /**
     * Find Faculty by Email.
     */
    public FacultyBean findByEmail(String email) throws ApplicationException {

        log.debug("findByEmail() called Email : " + email);

        FacultyBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement("select * from st_faculty where email=?");
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                bean = findByPk(rs.getLong(1));
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByEmail()", e);
            throw new ApplicationException("Exception in finding Faculty by Email");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    /**
     * Get Faculty List.
     */
    public List<FacultyBean> list() throws ApplicationException {
        log.debug("list() called");
        return search(null, 0, 0);
    }

    /**
     * Search Faculty with filters.
     */
    public List<FacultyBean> search(FacultyBean bean, int pageNo, int pageSize)
            throws ApplicationException {

        log.debug("search() called");

        StringBuffer sql = new StringBuffer(
                "select * from st_faculty where 1=1");

        if (bean != null) {

            if (bean.getId() > 0)
                sql.append(" and id = " + bean.getId());
            if (bean.getCollegeId() > 0)
                sql.append(" and college_id = " + bean.getCollegeId());
            if (bean.getCourseId() > 0)
                sql.append(" and course_id = " + bean.getCourseId());
            if (bean.getSubjectId() > 0)
                sql.append(" and subject_id = " + bean.getSubjectId());

            if (bean.getFirstName() != null)
                sql.append(" and first_name like '" + bean.getFirstName() + "%'");

            if (bean.getEmail() != null)
                sql.append(" and email like '" + bean.getEmail() + "%'");
        }

        if (pageSize > 0) {
            int offset = (pageNo - 1) * pageSize;
            sql.append(" limit " + offset + "," + pageSize);
        }

        List<FacultyBean> list = new ArrayList<>();
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = findByPk(rs.getLong(1));
                list.add(bean);
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in search()", e);
            throw new ApplicationException("Exception in searching Faculty");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return list;
    }
}
