package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.bean.SubjectBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * Model class for Subject entity. Provides methods for CRUD operations,
 * search, and lookup by primary key and name.
 *
 * @author Deepak Verma
 * @version 1.0
 */
public class SubjectModel {

    /** Logger instance for SubjectModel */
    private static Logger log = Logger.getLogger(SubjectModel.class);

    /**
     * Gets the next primary key for the st_subject table.
     *
     * @return next primary key value
     * @throws DatabaseException if any database error occurs
     */
    public Integer nextPk() throws DatabaseException {
        Connection conn = null;
        int pk = 0;

        log.debug("nextPk() started");

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement("select max(id) from st_subject");
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
     * Adds a new Subject record.
     *
     * @param bean SubjectBean containing subject details
     * @return generated primary key
     * @throws ApplicationException     if any application level error occurs
     * @throws DuplicateRecordException if subject with same name already exists
     */
    public long add(SubjectBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;
        int pk = 0;

        log.info("add() called for Subject : " + bean.getName());

        CourseModel courseModel = new CourseModel();
        CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
        bean.setCourseName(courseBean.getName());

        SubjectBean existBean = findByName(bean.getName());
        if (existBean != null) {
            log.warn("Duplicate Subject Name found : " + bean.getName());
            throw new DuplicateRecordException("Subject Name already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            pk = nextPk();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into st_subject values(?, ?, ?, ?, ?, ?, ?, ?, ?)");

            pstmt.setInt(1, pk);
            pstmt.setString(2, bean.getName());
            pstmt.setLong(3, bean.getCourseId());
            pstmt.setString(4, bean.getCourseName());
            pstmt.setString(5, bean.getDescription());
            pstmt.setString(6, bean.getCreatedBy());
            pstmt.setString(7, bean.getModifiedBy());
            pstmt.setTimestamp(8, bean.getCreatedDatetime());
            pstmt.setTimestamp(9, bean.getModifiedDatetime());

            pstmt.executeUpdate();
            conn.commit();

            log.info("Subject added successfully PK : " + pk);

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in add()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException(
                        "Exception : add rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception : Exception in add Subject");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    /**
     * Updates an existing Subject record.
     *
     * @param bean SubjectBean containing updated subject details
     * @throws ApplicationException     if any application level error occurs
     * @throws DuplicateRecordException if another subject with same name already exists
     */
    public void update(SubjectBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;

        log.info("update() called for Subject ID : " + bean.getId());

        CourseModel courseModel = new CourseModel();
        CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
        bean.setCourseName(courseBean.getName());

        SubjectBean existBean = findByName(bean.getName());
        if (existBean != null && existBean.getId() != bean.getId()) {
            log.warn("Duplicate Subject Name during update : " + bean.getName());
            throw new DuplicateRecordException("Subject Name already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                "update st_subject set name = ?, course_id = ?, course_name = ?, "
              + "description = ?, created_by = ?, modified_by = ?, "
              + "created_datetime = ?, modified_datetime = ? where id = ?");

            pstmt.setString(1, bean.getName());
            pstmt.setLong(2, bean.getCourseId());
            pstmt.setString(3, bean.getCourseName());
            pstmt.setString(4, bean.getDescription());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDatetime());
            pstmt.setTimestamp(8, bean.getModifiedDatetime());
            pstmt.setLong(9, bean.getId());

            pstmt.executeUpdate();
            conn.commit();

            log.info("Subject updated successfully ID : " + bean.getId());

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in update()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException(
                        "Exception : Delete rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in updating Subject ");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Deletes a Subject record.
     *
     * @param bean SubjectBean containing ID of subject to delete
     * @throws ApplicationException if any application level error occurs
     */
    public void delete(SubjectBean bean) throws ApplicationException {

        Connection conn = null;
        log.info("delete() called for Subject ID : " + bean.getId());

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt =
                    conn.prepareStatement("delete from st_subject where id = ?");
            pstmt.setLong(1, bean.getId());
            pstmt.executeUpdate();
            conn.commit();

            log.info("Subject deleted successfully ID : " + bean.getId());

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in delete()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException(
                        "Exception : Delete rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception : Exception in delete Subject");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Finds a Subject by primary key.
     *
     * @param pk primary key of subject
     * @return SubjectBean if found, otherwise null
     * @throws ApplicationException if any application level error occurs
     */
    public SubjectBean findByPk(long pk) throws ApplicationException {

        log.debug("findByPk() called PK : " + pk);

        StringBuffer sql =
                new StringBuffer("select * from st_subject where id = ?");
        SubjectBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql.toString());
            pstmt.setLong(1, pk);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new SubjectBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setCourseId(rs.getLong(3));
                bean.setCourseName(rs.getString(4));
                bean.setDescription(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDatetime(rs.getTimestamp(8));
                bean.setModifiedDatetime(rs.getTimestamp(9));
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByPk()", e);
            throw new ApplicationException("Exception : Exception in getting Subject by pk");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Finds a Subject by name.
     *
     * @param name name of subject
     * @return SubjectBean if found, otherwise null
     * @throws ApplicationException if any application level error occurs
     */
    public SubjectBean findByName(String name) throws ApplicationException {

        log.debug("findByName() called Name : " + name);

        StringBuffer sql =
                new StringBuffer("select * from st_subject where name = ?");
        SubjectBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql.toString());
            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new SubjectBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setCourseId(rs.getLong(3));
                bean.setCourseName(rs.getString(4));
                bean.setDescription(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDatetime(rs.getTimestamp(8));
                bean.setModifiedDatetime(rs.getTimestamp(9));
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByName()", e);
            throw new ApplicationException(
                    "Exception : Exception in getting Subject by Subject Name");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Returns list of all subjects.
     *
     * @return list of SubjectBean
     * @throws ApplicationException if any application level error occurs
     */
    public List<SubjectBean> list() throws ApplicationException {
        log.debug("list() called");
        return search(null, 0, 0);
    }

    /**
     * Searches Subject records based on given criteria.
     *
     * @param bean     search criteria bean (can be null)
     * @param pageNo   page number (for pagination)
     * @param pageSize number of records per page
     * @return list of SubjectBean matching criteria
     * @throws ApplicationException if any application level error occurs
     */
    public List<SubjectBean> search(SubjectBean bean, int pageNo, int pageSize)
            throws ApplicationException {

        log.debug("search() called");

        StringBuffer sql =
                new StringBuffer("select * from st_subject where 1=1");

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getName() != null && bean.getName().length() > 0) {
                sql.append(" and name like '" + bean.getName() + "%'");
            }
            if (bean.getCourseId() > 0) {
                sql.append(" and course_id = " + bean.getCourseId());
            }
            if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
                sql.append(" and course_name like '" + bean.getCourseName() + "%'");
            }
            if (bean.getDescription() != null && bean.getDescription().length() > 0) {
                sql.append(" and description like '" + bean.getDescription() + "%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + ", " + pageSize);
        }

        ArrayList<SubjectBean> list = new ArrayList<>();
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new SubjectBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setCourseId(rs.getLong(3));
                bean.setCourseName(rs.getString(4));
                bean.setDescription(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDatetime(rs.getTimestamp(8));
                bean.setModifiedDatetime(rs.getTimestamp(9));
                list.add(bean);
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in search()", e);
            throw new ApplicationException("Exception : Exception in search Subject");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return list;
    }
}
