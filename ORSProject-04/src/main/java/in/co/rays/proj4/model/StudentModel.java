package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.bean.StudentBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * Model class for Student entity. Provides methods for CRUD operations,
 * searching, and lookup by primary key and email.
 *
 * This class interacts with the <code>st_student</code> table.
 *
 * author Deepak Verma
 * @version 1.0
 */
public class StudentModel {

    /** Logger instance for StudentModel */
    private static Logger log = Logger.getLogger(StudentModel.class);

    /**
     * Gets the next primary key value for the <code>st_student</code> table.
     *
     * @return next primary key
     * @throws DatabaseException if any database error occurs
     */
    public Integer nextPk() throws DatabaseException {

        Connection conn = null;
        int pk = 0;

        log.debug("nextPk() started");

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement("select max(id) from st_student");
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
     * Adds a new Student record.
     *
     * @param bean StudentBean containing student details
     * @return generated primary key
     * @throws ApplicationException     if any application level error occurs
     * @throws DuplicateRecordException if a student with same email already exists
     */
    public long add(StudentBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;
        int pk = 0;

        log.info("add() called for Student Email : " + bean.getEmail());

        CollegeModel collegeModel = new CollegeModel();
        CollegeBean collegeBean = collegeModel.findByPk(bean.getCollegeId());
        bean.setCollegeName(collegeBean.getName());

        StudentBean existBean = findByEmailId(bean.getEmail());
        if (existBean != null) {
            log.warn("Duplicate Email found : " + bean.getEmail());
            throw new DuplicateRecordException("Email already exists");
        }

        try {
            pk = nextPk();
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                "insert into st_student values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            pstmt.setInt(1, pk);
            pstmt.setString(2, bean.getFirstName());
            pstmt.setString(3, bean.getLastName());
            pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(5, bean.getGender());
            pstmt.setString(6, bean.getMobileNo());
            pstmt.setString(7, bean.getEmail());
            pstmt.setLong(8, bean.getCollegeId());
            pstmt.setString(9, bean.getCollegeName());
            pstmt.setString(10, bean.getCreatedBy());
            pstmt.setString(11, bean.getModifiedBy());
            pstmt.setTimestamp(12, bean.getCreatedDatetime());
            pstmt.setTimestamp(13, bean.getModifiedDatetime());

            pstmt.executeUpdate();
            conn.commit();

            log.info("Student added successfully PK : " + pk);

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in add()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException(
                        "Exception : add rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception : Exception in add Student");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    /**
     * Updates an existing Student record.
     *
     * @param bean StudentBean containing updated details
     * @throws ApplicationException     if any application level error occurs
     * @throws DuplicateRecordException if another student with same email already exists
     */
    public void update(StudentBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;

        log.info("update() called for Student ID : " + bean.getId());

        StudentBean existBean = findByEmailId(bean.getEmail());
        if (existBean != null && existBean.getId() != bean.getId()) {
            log.warn("Duplicate Email during update : " + bean.getEmail());
            throw new DuplicateRecordException("Email Id is already exist");
        }

        CollegeModel collegeModel = new CollegeModel();
        CollegeBean collegeBean = collegeModel.findByPk(bean.getCollegeId());
        bean.setCollegeName(collegeBean.getName());

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                "update st_student set first_name = ?, last_name = ?, dob = ?, gender = ?, "
              + "mobile_no = ?, email = ?, college_id = ?, college_name = ?, "
              + "created_by = ?, modified_by = ?, created_datetime = ?, "
              + "modified_datetime = ? where id = ?");

            pstmt.setString(1, bean.getFirstName());
            pstmt.setString(2, bean.getLastName());
            pstmt.setDate(3, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(4, bean.getGender());
            pstmt.setString(5, bean.getMobileNo());
            pstmt.setString(6, bean.getEmail());
            pstmt.setLong(7, bean.getCollegeId());
            pstmt.setString(8, bean.getCollegeName());
            pstmt.setString(9, bean.getCreatedBy());
            pstmt.setString(10, bean.getModifiedBy());
            pstmt.setTimestamp(11, bean.getCreatedDatetime());
            pstmt.setTimestamp(12, bean.getModifiedDatetime());
            pstmt.setLong(13, bean.getId());

            pstmt.executeUpdate();
            conn.commit();

            log.info("Student updated successfully ID : " + bean.getId());

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in update()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException(
                        "Exception : Delete rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in updating Student ");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Deletes a Student record.
     *
     * @param bean StudentBean containing ID of student to delete
     * @throws ApplicationException if any application level error occurs
     */
    public void delete(StudentBean bean) throws ApplicationException {

        Connection conn = null;
        log.info("delete() called for Student ID : " + bean.getId());

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt =
                    conn.prepareStatement("delete from st_student where id = ?");
            pstmt.setLong(1, bean.getId());
            pstmt.executeUpdate();
            conn.commit();

            log.info("Student deleted successfully ID : " + bean.getId());

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in delete()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException(
                        "Exception : Delete rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception : Exception in delete Student");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Finds a Student by primary key.
     *
     * @param pk primary key of student
     * @return StudentBean if found, otherwise null
     * @throws ApplicationException if any application level error occurs
     */
    public StudentBean findByPk(long pk) throws ApplicationException {

        log.debug("findByPk() called PK : " + pk);

        StringBuffer sql =
                new StringBuffer("select * from st_student where id = ?");
        StudentBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql.toString());
            pstmt.setLong(1, pk);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new StudentBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setDob(rs.getDate(4));
                bean.setGender(rs.getString(5));
                bean.setMobileNo(rs.getString(6));
                bean.setEmail(rs.getString(7));
                bean.setCollegeId(rs.getLong(8));
                bean.setCollegeName(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByPk()", e);
            throw new ApplicationException("Exception : Exception in getting User by pk");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Finds a Student by email ID.
     *
     * @param Email email ID of student
     * @return StudentBean if found, otherwise null
     * @throws ApplicationException if any application level error occurs
     */
    public StudentBean findByEmailId(String Email)
            throws ApplicationException {

        log.debug("findByEmailId() called Email : " + Email);

        StringBuffer sql =
                new StringBuffer("select * from st_student where email = ?");
        StudentBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql.toString());
            pstmt.setString(1, Email);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new StudentBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setDob(rs.getDate(4));
                bean.setGender(rs.getString(5));
                bean.setMobileNo(rs.getString(6));
                bean.setEmail(rs.getString(7));
                bean.setCollegeId(rs.getLong(8));
                bean.setCollegeName(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByEmailId()", e);
            throw new ApplicationException("Exception : Exception in getting User by Email");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Returns list of all students.
     *
     * @return list of StudentBean
     * @throws ApplicationException if any application level error occurs
     */
    public List<StudentBean> list() throws ApplicationException {
        log.debug("list() called");
        return search(null, 0, 0);
    }

    /**
     * Searches students based on given criteria.
     *
     * @param bean     search criteria (can be null)
     * @param pageNo   page number (for pagination)
     * @param pageSize number of records per page
     * @return list of StudentBean matching the criteria
     * @throws ApplicationException if any application level error occurs
     */
    public List<StudentBean> search(StudentBean bean, int pageNo, int pageSize)
            throws ApplicationException {

        log.debug("search() called");

        StringBuffer sql =
                new StringBuffer("select * from st_student where 1 = 1");

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
                sql.append(" and first_name like '" + bean.getFirstName() + "%'");
            }
            if (bean.getLastName() != null && bean.getLastName().length() > 0) {
                sql.append(" and last_name like '" + bean.getLastName() + "%'");
            }
            if (bean.getDob() != null && bean.getDob().getDate() > 0) {
                sql.append(" and dob = " + bean.getDob());
            }
            if (bean.getGender() != null && bean.getGender().length() > 0) {
                sql.append(" and gender like '" + bean.getGender() + "%'");
            }
            if (bean.getMobileNo() != null && bean.getMobileNo().length() > 0) {
                sql.append(" and mobile_no like '" + bean.getMobileNo() + "%'");
            }
            if (bean.getEmail() != null && bean.getEmail().length() > 0) {
                sql.append(" and email like '" + bean.getEmail() + "%'");
            }
            if (bean.getCollegeName() != null && bean.getCollegeName().length() > 0) {
                sql.append(" and college_name = " + bean.getCollegeName());
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + ", " + pageSize);
        }

        ArrayList<StudentBean> list = new ArrayList<>();
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new StudentBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setDob(rs.getDate(4));
                bean.setGender(rs.getString(5));
                bean.setMobileNo(rs.getString(6));
                bean.setEmail(rs.getString(7));
                bean.setCollegeId(rs.getLong(8));
                bean.setCollegeName(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
                list.add(bean);
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in search()", e);
            throw new ApplicationException("Exception : Exception in search Student");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return list;
    }
}
