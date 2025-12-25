package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * Model class for Course entity. Provides methods for CRUD operations,
 * search, and lookup by primary key and name.
 * <p>
 * This model works with the {@code st_course} table.
 *
 * @author Deepak Verma
 * @version 1.0
 */
public class CourseModel {

    /** Logger instance for CourseModel */
    private static Logger log = Logger.getLogger(CourseModel.class);

    /**
     * Gets the next primary key value for the {@code st_course} table.
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
                    conn.prepareStatement("select max(id)from st_course");

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
     * Adds a new Course record.
     *
     * @param bean CourseBean containing course details
     * @return generated primary key
     * @throws ApplicationException     if any application level error occurs
     * @throws DuplicateRecordException if a course with same name already exists
     */
    public long add(CourseBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;
        int pk = 0;

        log.info("add() called for Course : " + bean.getName());

        CourseBean existBean = findByName(bean.getName());

        if (existBean != null) {
            log.warn("Duplicate Course : " + bean.getName());
            throw new DuplicateRecordException("Course Name already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            pk = nextPk();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into st_course values(?, ?, ?, ?, ?, ?, ?, ?)");

            pstmt.setInt(1, pk);
            pstmt.setString(2, bean.getName());
            pstmt.setString(3, bean.getDuration());
            pstmt.setString(4, bean.getDescription());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDatetime());
            pstmt.setTimestamp(8, bean.getModifiedDatetime());

            pstmt.executeUpdate();
            conn.commit();

            log.info("Course added successfully with PK : " + pk);

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in add()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException(
                        "Exception : add rollback exception " + ex.getMessage());
            }
            throw new ApplicationException(
                    "Exception : Exception in add Course");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    /**
     * Updates an existing Course record.
     *
     * @param bean CourseBean containing updated course details
     * @throws ApplicationException     if any application level error occurs
     * @throws DuplicateRecordException if another course with same name already exists
     */
    public void update(CourseBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;

        log.info("update() called for Course ID : " + bean.getId());

        CourseBean existBean = findByName(bean.getName());
        if (existBean != null && existBean.getId() != bean.getId()) {
            log.warn("Duplicate Course during update : " + bean.getName());
            throw new DuplicateRecordException("Course already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_course set name = ?, duration = ?, description = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");

            pstmt.setString(1, bean.getName());
            pstmt.setString(2, bean.getDuration());
            pstmt.setString(3, bean.getDescription());
            pstmt.setString(4, bean.getCreatedBy());
            pstmt.setString(5, bean.getModifiedBy());
            pstmt.setTimestamp(6, bean.getCreatedDatetime());
            pstmt.setTimestamp(7, bean.getModifiedDatetime());
            pstmt.setLong(8, bean.getId());

            pstmt.executeUpdate();
            conn.commit();

            log.info("Course updated successfully ID : " + bean.getId());

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in update()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException(
                        "Exception : Delete rollback exception " + ex.getMessage());
            }
            throw new ApplicationException(
                    "Exception in updating Course ");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Deletes a Course record.
     *
     * @param bean CourseBean containing ID of the course to delete
     * @throws ApplicationException if any application level error occurs
     */
    public void delete(CourseBean bean) throws ApplicationException {

        Connection conn = null;

        log.info("delete() called for Course ID : " + bean.getId());

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt =
                    conn.prepareStatement("delete from st_course where id = ?");
            pstmt.setLong(1, bean.getId());
            pstmt.executeUpdate();
            conn.commit();

            log.info("Course deleted successfully ID : " + bean.getId());

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in delete()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException(
                        "Exception : Delete rollback exception " + ex.getMessage());
            }
            throw new ApplicationException(
                    "Exception : Exception in delete Course");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Finds a Course by primary key.
     *
     * @param pk primary key of course
     * @return CourseBean if found, otherwise {@code null}
     * @throws ApplicationException if any application level error occurs
     */
    public CourseBean findByPk(long pk) throws ApplicationException {

        log.debug("findByPk() called PK : " + pk);

        StringBuffer sql =
                new StringBuffer("select * from st_course where id = ?");

        CourseBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, pk);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new CourseBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setDuration(rs.getString(3));
                bean.setDescription(rs.getString(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDatetime(rs.getTimestamp(7));
                bean.setModifiedDatetime(rs.getTimestamp(8));
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByPk()", e);
            throw new ApplicationException(
                    "Exception : Exception in getting Course by pk");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Finds a Course by name.
     *
     * @param name course name
     * @return CourseBean if found, otherwise {@code null}
     * @throws ApplicationException if any application level error occurs
     */
    public CourseBean findByName(String name) throws ApplicationException {

        log.debug("findByName() called Name : " + name);

        StringBuffer sql =
                new StringBuffer("select * from st_course where name = ?");

        CourseBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new CourseBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setDuration(rs.getString(3));
                bean.setDescription(rs.getString(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDatetime(rs.getTimestamp(7));
                bean.setModifiedDatetime(rs.getTimestamp(8));
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByName()", e);
            throw new ApplicationException(
                    "Exception : Exception in getting Course by Course Name");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Returns list of all courses.
     *
     * @return list of CourseBean
     * @throws ApplicationException if any application level error occurs
     */
    public List<CourseBean> list() throws ApplicationException {
        log.debug("list() called");
        return search(null, 0, 0);
    }

    /**
     * Searches courses based on given criteria.
     *
     * @param bean     search criteria (can be {@code null})
     * @param pageNo   page number (for pagination)
     * @param pageSize number of records per page
     * @return list of CourseBean matching the criteria
     * @throws ApplicationException if any application level error occurs
     */
    public List<CourseBean> search(CourseBean bean, int pageNo, int pageSize)
            throws ApplicationException {

        log.debug("search() called");

        StringBuffer sql =
                new StringBuffer("select * from st_course where 1=1");

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getName() != null && bean.getName().length() > 0) {
                sql.append(" and name like '" + bean.getName() + "%'");
            }
            if (bean.getDuration() != null && bean.getDuration().length() > 0) {
                sql.append(" and duration like '" + bean.getDuration() + "%'");
            }
            if (bean.getDescription() != null && bean.getDescription().length() > 0) {
                sql.append(" and description like '" + bean.getDescription() + "%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + ", " + pageSize);
        }

        ArrayList<CourseBean> list = new ArrayList<CourseBean>();
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new CourseBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setDuration(rs.getString(3));
                bean.setDescription(rs.getString(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDatetime(rs.getTimestamp(7));
                bean.setModifiedDatetime(rs.getTimestamp(8));
                list.add(bean);
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in search()", e);
            throw new ApplicationException(
                    "Exception : Exception in search Course");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return list;
    }
}
