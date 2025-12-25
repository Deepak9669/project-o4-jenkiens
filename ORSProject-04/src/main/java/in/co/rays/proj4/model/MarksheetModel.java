package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.MarksheetBean;
import in.co.rays.proj4.bean.StudentBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * Model class for Marksheet entity. Provides methods for CRUD operations,
 * search, find by primary key / roll number, and retrieving merit list.
 *
 * This model works with the {@code st_marksheet} table.
 *
 * @author Deepak Verma
 * @version 1.0
 */
public class MarksheetModel {

    /** Logger instance for MarksheetModel */
    private static Logger log = Logger.getLogger(MarksheetModel.class);

    /**
     * Gets the next primary key value for the {@code st_marksheet} table.
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
                    conn.prepareStatement("select max(id) from st_marksheet");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                pk = rs.getInt(1);
            }

            rs.close();
            pstmt.close();

            log.debug("Next PK generated : " + (pk + 1));

        } catch (Exception e) {
            log.error("Exception in nextPk()", e);
            throw new DatabaseException("Exception in Marksheet getting PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk + 1;
    }

    /**
     * Adds a new Marksheet record.
     *
     * @param bean MarksheetBean containing marksheet details
     * @return generated primary key
     * @throws ApplicationException     if any application level error occurs
     * @throws DuplicateRecordException if a marksheet with same roll number already exists
     */
    public long add(MarksheetBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;
        int pk = 0;

        log.info("add() called for Roll No : " + bean.getRollNo());

        // Set student name from Student table
        StudentModel studentModel = new StudentModel();
        StudentBean studentbean = studentModel.findByPk(bean.getStudentId());
        bean.setName(studentbean.getFirstName() + " " + studentbean.getLastName());

        // Check duplicate roll number
        MarksheetBean existBean = findByRollNo(bean.getRollNo());
        if (existBean != null) {
            log.warn("Duplicate Roll No found : " + bean.getRollNo());
            throw new DuplicateRecordException("Roll Number already exists");
        }

        try {
            pk = nextPk();
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                "insert into st_marksheet values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            pstmt.setInt(1, pk);
            pstmt.setString(2, bean.getRollNo());
            pstmt.setLong(3, bean.getStudentId());
            pstmt.setString(4, bean.getName());
            pstmt.setInt(5, bean.getPhysics());
            pstmt.setInt(6, bean.getChemistry());
            pstmt.setInt(7, bean.getMaths());
            pstmt.setString(8, bean.getCreatedBy());
            pstmt.setString(9, bean.getModifiedBy());
            pstmt.setTimestamp(10, bean.getCreatedDatetime());
            pstmt.setTimestamp(11, bean.getModifiedDatetime());

            pstmt.executeUpdate();
            conn.commit();

            log.info("Marksheet added successfully PK : " + pk);

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in add()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("add rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in add marksheet");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    /**
     * Updates an existing Marksheet record.
     *
     * @param bean MarksheetBean containing updated marksheet details
     * @throws ApplicationException     if any application level error occurs
     * @throws DuplicateRecordException if another marksheet with same roll number exists
     */
    public void update(MarksheetBean bean)
            throws ApplicationException, DuplicateRecordException {

        log.info("update() called for Marksheet ID : " + bean.getId());

        Connection conn = null;

        MarksheetBean existBean = findByRollNo(bean.getRollNo());
        if (existBean != null && existBean.getId() != bean.getId()) {
            log.warn("Duplicate Roll No during update : " + bean.getRollNo());
            throw new DuplicateRecordException("Roll No is already exist");
        }

        // Set student name from Student table
        StudentModel studentModel = new StudentModel();
        StudentBean studentbean = studentModel.findByPk(bean.getStudentId());
        bean.setName(studentbean.getFirstName() + " " + studentbean.getLastName());

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                "update st_marksheet set roll_no = ?, student_id = ?, name = ?, physics = ?, chemistry = ?, maths = ?, "
              + "created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");

            pstmt.setString(1, bean.getRollNo());
            pstmt.setLong(2, bean.getStudentId());
            pstmt.setString(3, bean.getName());
            pstmt.setInt(4, bean.getPhysics());
            pstmt.setInt(5, bean.getChemistry());
            pstmt.setInt(6, bean.getMaths());
            pstmt.setString(7, bean.getCreatedBy());
            pstmt.setString(8, bean.getModifiedBy());
            pstmt.setTimestamp(9, bean.getCreatedDatetime());
            pstmt.setTimestamp(10, bean.getModifiedDatetime());
            pstmt.setLong(11, bean.getId());

            pstmt.executeUpdate();
            conn.commit();

            log.info("Marksheet updated successfully ID : " + bean.getId());

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in update()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Update rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in updating Marksheet ");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Deletes a Marksheet record.
     *
     * @param bean MarksheetBean containing ID of the marksheet to delete
     * @throws ApplicationException if any application level error occurs
     */
    public void delete(MarksheetBean bean) throws ApplicationException {

        Connection conn = null;
        log.info("delete() called for Marksheet ID : " + bean.getId());

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt =
                    conn.prepareStatement("delete from st_marksheet where id = ?");
            pstmt.setLong(1, bean.getId());
            pstmt.executeUpdate();
            conn.commit();

            log.info("Marksheet deleted successfully ID : " + bean.getId());

            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in delete()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Delete rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in delete marksheet");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Finds a Marksheet by primary key.
     *
     * @param pk primary key of marksheet
     * @return MarksheetBean if found, otherwise {@code null}
     * @throws ApplicationException if any application level error occurs
     */
    public MarksheetBean findByPk(long pk) throws ApplicationException {

        log.debug("findByPk() called PK : " + pk);

        StringBuffer sql =
                new StringBuffer("select * from st_marksheet where id = ?");
        MarksheetBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, pk);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new MarksheetBean();
                bean.setId(rs.getLong(1));
                bean.setRollNo(rs.getString(2));
                bean.setStudentId(rs.getLong(3));
                bean.setName(rs.getString(4));
                bean.setPhysics(rs.getInt(5));
                bean.setChemistry(rs.getInt(6));
                bean.setMaths(rs.getInt(7));
                bean.setCreatedBy(rs.getString(8));
                bean.setModifiedBy(rs.getString(9));
                bean.setCreatedDatetime(rs.getTimestamp(10));
                bean.setModifiedDatetime(rs.getTimestamp(11));
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByPk()", e);
            throw new ApplicationException("Exception in getting marksheet by pk");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Finds a Marksheet by roll number.
     *
     * @param rollNo roll number of the student
     * @return MarksheetBean if found, otherwise {@code null}
     * @throws ApplicationException if any application level error occurs
     */
    public MarksheetBean findByRollNo(String rollNo) throws ApplicationException {

        log.debug("findByRollNo() called Roll No : " + rollNo);

        StringBuffer sql =
                new StringBuffer("select * from st_marksheet where roll_no = ?");
        MarksheetBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, rollNo);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new MarksheetBean();
                bean.setId(rs.getLong(1));
                bean.setRollNo(rs.getString(2));
                bean.setStudentId(rs.getLong(3));
                bean.setName(rs.getString(4));
                bean.setPhysics(rs.getInt(5));
                bean.setChemistry(rs.getInt(6));
                bean.setMaths(rs.getInt(7));
                bean.setCreatedBy(rs.getString(8));
                bean.setModifiedBy(rs.getString(9));
                bean.setCreatedDatetime(rs.getTimestamp(10));
                bean.setModifiedDatetime(rs.getTimestamp(11));
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByRollNo()", e);
            throw new ApplicationException("Exception in getting marksheet by roll no");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Returns list of all marksheets.
     *
     * @return list of MarksheetBean
     * @throws ApplicationException if any application level error occurs
     */
    public List<MarksheetBean> list() throws ApplicationException {
        log.debug("list() called");
        return search(null, 0, 0);
    }

    /**
     * Searches marksheets based on given criteria.
     *
     * @param bean     search criteria (can be {@code null})
     * @param pageNo   page number (for pagination)
     * @param pageSize number of records per page
     * @return list of MarksheetBean matching the criteria
     * @throws ApplicationException if any application level error occurs
     */
    public List<MarksheetBean> search(MarksheetBean bean, int pageNo, int pageSize)
            throws ApplicationException {

        log.debug("search() called");

        StringBuffer sql =
                new StringBuffer("select * from st_marksheet where 1=1");

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getRollNo() != null && bean.getRollNo().length() > 0) {
                sql.append(" and roll_no like '" + bean.getRollNo() + "%'");
            }
            if (bean.getName() != null && bean.getName().length() > 0) {
                sql.append(" and name like '" + bean.getName() + "%'");
            }
            if (bean.getPhysics() != null && bean.getPhysics() > 0) {
                sql.append(" and physics = " + bean.getPhysics());
            }
            if (bean.getChemistry() != null && bean.getChemistry() > 0) {
                sql.append(" and chemistry = " + bean.getChemistry());
            }
            if (bean.getMaths() != null && bean.getMaths() > 0) {
                sql.append(" and maths = " + bean.getMaths());
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + ", " + pageSize);
        }

        ArrayList<MarksheetBean> list = new ArrayList<>();
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new MarksheetBean();
                bean.setId(rs.getLong(1));
                bean.setRollNo(rs.getString(2));
                bean.setStudentId(rs.getLong(3));
                bean.setName(rs.getString(4));
                bean.setPhysics(rs.getInt(5));
                bean.setChemistry(rs.getInt(6));
                bean.setMaths(rs.getInt(7));
                bean.setCreatedBy(rs.getString(8));
                bean.setModifiedBy(rs.getString(9));
                bean.setCreatedDatetime(rs.getTimestamp(10));
                bean.setModifiedDatetime(rs.getTimestamp(11));
                list.add(bean);
            }

            rs.close();

        } catch (Exception e) {
            log.error("Exception in search()", e);
            throw new ApplicationException("Update rollback exception " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return list;
    }

    /**
     * Returns merit list of students (only those who passed all subjects), ordered
     * by total marks in descending order.
     *
     * @param pageNo   page number (for pagination)
     * @param pageSize number of records per page
     * @return list of MarksheetBean representing merit list
     * @throws ApplicationException if any application level error occurs
     */
    public List<MarksheetBean> getMeritList(int pageNo, int pageSize)
            throws ApplicationException {

        log.debug("getMeritList() called");

        ArrayList<MarksheetBean> list = new ArrayList<>();

        StringBuffer sql = new StringBuffer(
            "select id, roll_no, name, physics, chemistry, maths, "
          + "(physics + chemistry + maths) as total "
          + "from st_marksheet where physics > 33 and chemistry > 33 "
          + "and maths > 33 order by total desc");

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + ", " + pageSize);
        }

        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                MarksheetBean bean = new MarksheetBean();
                bean.setId(rs.getLong(1));
                bean.setRollNo(rs.getString(2));
                bean.setName(rs.getString(3));
                bean.setPhysics(rs.getInt(4));
                bean.setChemistry(rs.getInt(5));
                bean.setMaths(rs.getInt(6));
                list.add(bean);
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in getMeritList()", e);
            throw new ApplicationException("Exception in getting merit list of Marksheet");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return list;
    }
}
