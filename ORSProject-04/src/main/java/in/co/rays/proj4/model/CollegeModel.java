package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * Model class for College entity. Provides methods for CRUD operations,
 * searching, and lookups by primary key and name.
 * <p>
 * This model works with the {@code st_college} table.
 *
 * @author Deepak Verma
 * @version 1.0
 */
public class CollegeModel {

    /** Logger instance for CollegeModel */
    private static Logger log = Logger.getLogger(CollegeModel.class);

    /**
     * Gets the next primary key value for the {@code st_college} table.
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
            PreparedStatement pstmt = conn.prepareStatement(
                    "select max(id) from st_college");

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
     * Adds a new College record.
     *
     * @param bean CollegeBean containing college details
     * @return generated primary key
     * @throws ApplicationException     if any application level error occurs
     * @throws DuplicateRecordException if a college with same name already exists
     */
    public long add(CollegeBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;
        int pk = 0;

        log.info("add() called for College : " + bean.getName());

        CollegeBean existBean = findByName(bean.getName());
        if (existBean != null) {
            log.warn("Duplicate College : " + bean.getName());
            throw new DuplicateRecordException("College Allready exists");
        }

        try {
            pk = nextPk();
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into st_college values(?,?,?,?,?,?,?,?,?,?)");

            pstmt.setInt(1, pk);
            pstmt.setString(2, bean.getName());
            pstmt.setString(3, bean.getAddress());
            pstmt.setString(4, bean.getState());
            pstmt.setString(5, bean.getCity());
            pstmt.setString(6, bean.getPhoneNo());
            pstmt.setString(7, bean.getCreatedBy());
            pstmt.setString(8, bean.getModifiedBy());
            pstmt.setTimestamp(9, bean.getCreatedDatetime());
            pstmt.setTimestamp(10, bean.getModifiedDatetime());

            pstmt.executeUpdate();
            conn.commit();

            log.info("College added successfully with PK : " + pk);

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in add()", e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                log.error("Rollback exception", e1);
                throw new ApplicationException(
                        "Exception : Delete rollBack exception");
            }
            throw new ApplicationException(
                    "Exception: Exception in add collage");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    /**
     * Updates an existing College record.
     *
     * @param bean CollegeBean containing updated college details
     * @throws ApplicationException     if any application level error occurs
     * @throws DuplicateRecordException if another college with same name already exists
     */
    public void update(CollegeBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;

        log.info("update() called for College ID : " + bean.getId());

        CollegeBean existBean = findByPk(bean.getId());

        if (existBean != null && existBean.getId() != bean.getId()) {
            log.warn("Duplicate College during update : " + bean.getName());
            throw new DuplicateRecordException("College allready exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_college set name = ?, address = ?, state = ?, city = ?, phone_no = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");

            pstmt.setString(1, bean.getName());
            pstmt.setString(2, bean.getAddress());
            pstmt.setString(3, bean.getState());
            pstmt.setString(4, bean.getCity());
            pstmt.setString(5, bean.getPhoneNo());
            pstmt.setString(6, bean.getCreatedBy());
            pstmt.setString(7, bean.getModifiedBy());
            pstmt.setTimestamp(8, bean.getCreatedDatetime());
            pstmt.setTimestamp(9, bean.getModifiedDatetime());
            pstmt.setLong(10, bean.getId());

            pstmt.executeUpdate();
            conn.commit();

            log.info("College updated successfully ID : " + bean.getId());

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in update()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                log.error("Rollback exception", ex);
                throw new ApplicationException(
                        "Exception : Delete rollback exception " + ex.getMessage());
            }
            throw new ApplicationException(
                    "Exception in updating College");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Deletes a College record.
     *
     * @param bean CollegeBean containing ID of the college to delete
     * @throws ApplicationException if any application level error occurs
     */
    public void delete(CollegeBean bean) throws ApplicationException {

        Connection conn = null;

        log.info("delete() called for College ID : " + bean.getId());

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "delete from st_college where id = ?");
            pstmt.setLong(1, bean.getId());
            pstmt.executeUpdate();
            conn.commit();

            log.info("College deleted successfully ID : " + bean.getId());

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
                    "Exception : Exception in delete User");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Finds a College by primary key.
     *
     * @param pk primary key of college
     * @return CollegeBean if found, otherwise {@code null}
     * @throws ApplicationException if any application level error occurs
     */
    public CollegeBean findByPk(long pk) throws ApplicationException {

        log.debug("findByPk() called PK : " + pk);

        StringBuffer sql = new StringBuffer(
                "select * from st_college where id = ?");

        CollegeBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, pk);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new CollegeBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setAddress(rs.getString(3));
                bean.setState(rs.getString(4));
                bean.setCity(rs.getString(5));
                bean.setPhoneNo(rs.getString(6));
                bean.setCreatedBy(rs.getString(7));
                bean.setModifiedBy(rs.getString(8));
                bean.setCreatedDatetime(rs.getTimestamp(9));
                bean.setModifiedDatetime(rs.getTimestamp(10));
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByPk()", e);
            throw new ApplicationException(
                    "Exception : Exception in getting College by pk");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Finds a College by name.
     *
     * @param name college name
     * @return CollegeBean if found, otherwise {@code null}
     * @throws ApplicationException if any application level error occurs
     */
    public CollegeBean findByName(String name) throws ApplicationException {

        log.debug("findByName() called Name : " + name);

        StringBuffer sql = new StringBuffer(
                "select * from st_college where name = ?");

        CollegeBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new CollegeBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setAddress(rs.getString(3));
                bean.setState(rs.getString(4));
                bean.setCity(rs.getString(5));
                bean.setPhoneNo(rs.getString(6));
                bean.setCreatedBy(rs.getString(7));
                bean.setModifiedBy(rs.getString(8));
                bean.setCreatedDatetime(rs.getTimestamp(9));
                bean.setModifiedDatetime(rs.getTimestamp(10));
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByName()", e);
            throw new ApplicationException(
                    "Exception : Exception in getting College by Name");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Returns list of all colleges.
     *
     * @return list of CollegeBean
     * @throws ApplicationException if any application level error occurs
     */
    public List<CollegeBean> list() throws ApplicationException {
        log.debug("list() called");
        return search(null, 0, 0);
    }

    /**
     * Searches colleges based on given criteria.
     *
     * @param bean     search criteria (can be {@code null})
     * @param pageNo   page number (for pagination)
     * @param pageSize number of records per page
     * @return list of CollegeBean matching the criteria
     * @throws ApplicationException if any application level error occurs
     */
    public List<CollegeBean> search(CollegeBean bean, int pageNo, int pageSize)
            throws ApplicationException {

        log.debug("search() called");

        StringBuffer sql = new StringBuffer(
                "select * from st_college where 1 = 1");

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getName() != null && bean.getName().length() > 0) {
                sql.append(" and name like '" + bean.getName() + "%'");
            }
            if (bean.getAddress() != null && bean.getAddress().length() > 0) {
                sql.append(" and address like '" + bean.getAddress() + "%'");
            }
            if (bean.getState() != null && bean.getState().length() > 0) {
                sql.append(" and state like '" + bean.getState() + "%'");
            }
            if (bean.getCity() != null && bean.getCity().length() > 0) {
                sql.append(" and city like '" + bean.getCity() + "%'");
            }
            if (bean.getPhoneNo() != null && bean.getPhoneNo().length() > 0) {
                sql.append(" and phone_no = " + bean.getPhoneNo());
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + ", " + pageSize);
        }

        ArrayList<CollegeBean> list = new ArrayList<CollegeBean>();
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new CollegeBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setAddress(rs.getString(3));
                bean.setState(rs.getString(4));
                bean.setCity(rs.getString(5));
                bean.setPhoneNo(rs.getString(6));
                bean.setCreatedBy(rs.getString(7));
                bean.setModifiedBy(rs.getString(8));
                bean.setCreatedDatetime(rs.getTimestamp(9));
                bean.setModifiedDatetime(rs.getTimestamp(10));
                list.add(bean);
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in search()", e);
            throw new ApplicationException(
                    "Exception : Exception in search college");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return list;
    }
}
