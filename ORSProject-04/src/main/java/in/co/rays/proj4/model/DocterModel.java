package in.co.rays.proj4.model;

/**
 * @Author: Deepak Verma
 * @Description: DoctorModel handles CRUD operations and search functionality 
 * for Patient entities. It interacts with the database using JDBC.
 * 
 * @Version: 1.0
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.DoctorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * Model class for managing Doctor entity. Provides methods for add, update,
 * delete, find by PK, find by Name, search, and list operations.
 */
public class DocterModel {

	/** Logger instance for DoctorModel */
	private static Logger log = Logger.getLogger(DocterModel.class);

	/**
	 * Returns next primary key for Doctor table.
	 * 
	 * @return next primary key
	 * @throws DatabaseException
	 */
	public static Integer nextPk() throws DatabaseException {

		Connection conn = null;
		int pk = 0;

		log.debug("nextPk() started");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(id) FROM st_doctor");
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				pk = rs.getInt(1);
			}

			rs.close();
			pstmt.close();

			log.debug("Next PK generated : " + (pk + 1));

		} catch (Exception e) {
			log.error("Exception in nextPk()", e);
			throw new DatabaseException("Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return pk + 1;
	}

	/**
	 * Adds a new Doctor record to the database.
	 * 
	 * @param bean PDoctoBean object
	 * @throws ApplicationException
	 * @throws DuplicateRecordException if Doctor with same name exists
	 */
	public void add(DoctorBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		log.info("add() called for Patient : " + bean.getName());

		// Check for duplicate patient name
		DoctorBean existing = findByName(bean.getName());
		if (existing != null && existing.getId() != bean.getId()) {
			log.warn("Duplicate Doctor Name : " + bean.getName());
			throw new DuplicateRecordException("Doctor Name already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			int pk = nextPk();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO st_doctor VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setDate(3, new java.sql.Date(bean.getDateOfBirth().getTime()));
			pstmt.setString(4, bean.getMobile());
			pstmt.setString(5, bean.getExpertise());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());

			pstmt.executeUpdate();
			conn.commit();

			log.info("Doctor added successfully with PK : " + pk);

			pstmt.close();

		} catch (Exception e) {
			log.error("Exception in add()", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Add rollback exception: " + ex.getMessage());
			}
			throw new ApplicationException("Exception in adding Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Updates existing Doctor record in the database.
	 * 
	 * @param bean DoctorBean object
	 * @throws ApplicationException
	 * @throws DuplicateRecordException if patient with same name exists
	 */
	public void update(DoctorBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		log.info("update() called for Doctor ID : " + bean.getId());

		DoctorBean existing = findByName(bean.getName());
		if (existing != null && existing.getId() != bean.getId()) {
			log.warn("Duplicate Doctor Name during update : " + bean.getName());
			throw new DuplicateRecordException("Doctor Name already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn
					.prepareStatement("UPDATE st_doctor SET name=?, date_of_birth=?, mobile=?, expertise=?, "
							+ "created_by=?, modified_by=?, created_datetime=?, modified_datetime=? WHERE id=?");

			pstmt.setString(1, bean.getName());
			pstmt.setDate(2, new java.sql.Date(bean.getDateOfBirth().getTime()));
			pstmt.setString(3, bean.getMobile());
			pstmt.setString(4, bean.getExpertise());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDatetime());
			pstmt.setTimestamp(8, bean.getModifiedDatetime());
			pstmt.setLong(9, bean.getId());

			pstmt.executeUpdate();
			conn.commit();

			log.info("Doctor updated successfully ID : " + bean.getId());

			pstmt.close();

		} catch (Exception e) {
			log.error("Exception in update()", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Update rollback exception: " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Deletes a Doctor record by ID.
	 * 
	 * @param id Doctor ID
	 * @throws ApplicationException
	 */
	public void delete(long id) throws ApplicationException {

		Connection conn = null;
		log.info("delete() called for Patient ID : " + id);

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_doctor WHERE id=?");
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
			conn.commit();

			log.info("Doctor deleted successfully ID : " + id);

			pstmt.close();

		} catch (Exception e) {
			log.error("Exception in delete()", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Delete rollback exception: " + ex.getMessage());
			}
			throw new ApplicationException("Exception in deleting Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Finds a Doctor by primary key.
	 * 
	 * @param id Doctor ID
	 * @return DoctorBean
	 * @throws ApplicationException
	 */
	public DoctorBean findByPk(long id) throws ApplicationException {

		log.debug("findByPk() called ID : " + id);

		DoctorBean bean = null;
		Connection conn = null;
		String sql = "SELECT * FROM st_doctor WHERE id=?";

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bean = mapResultSetToBean(rs);
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			log.error("Exception in findByPk()", e);
			throw new ApplicationException("Exception in getting Doctor by PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	/**
	 * Finds a Doctor by name.
	 * 
	 * @param name Doctor name
	 * @return DoctorBean
	 * @throws ApplicationException
	 */
	public DoctorBean findByName(String name) throws ApplicationException {

		log.debug("findByName() called Name : " + name);

		DoctorBean bean = null;
		Connection conn = null;
		String sql = "SELECT * FROM st_doctor WHERE name=?";

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bean = mapResultSetToBean(rs);
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			log.error("Exception in findByName()", e);
			throw new ApplicationException("Exception in getting Doctor by Name");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	/**
	 * Returns a list of all Doctor records.
	 * 
	 * @return List of DoctorBean
	 * @throws ApplicationException
	 */
	public List<DoctorBean> list() throws ApplicationException {
		log.debug("list() called");
		return search(null, 0, 0);
	}

	/**
	 * Searches Doctor records with optional pagination.
	 * 
	 * @param bean     DoctorBean for search criteria
	 * @param pageNo   Page number
	 * @param pageSize Number of records per page
	 * @return List of DoctorBean
	 * @throws ApplicationException
	 */
	public List<DoctorBean> search(DoctorBean bean, int pageNo, int pageSize) throws ApplicationException {

		log.debug("search() called");

		Connection conn = null;
		StringBuilder sql = new StringBuilder("SELECT * FROM st_doctor WHERE 1=1 ");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id=").append(bean.getId());
			}
			if (bean.getName() != null && !bean.getName().isEmpty()) {
				sql.append(" AND name LIKE '").append(bean.getName()).append("%'");
			}
			if (bean.getDateOfBirth() != null) {
				sql.append(" AND date_of_birth LIKE '").append(new java.sql.Date(bean.getDateOfBirth().getTime()))
						.append("%'");
			}
			if (bean.getExpertise() != null && !bean.getExpertise().isEmpty()) {
				sql.append(" AND expertise LIKE '").append(bean.getExpertise()).append("%'");
				
				 
				 
			}
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" LIMIT ").append(pageNo).append(",").append(pageSize);
		}

		List<DoctorBean> list = new ArrayList<>();

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(mapResultSetToBean(rs));
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			log.error("Exception in search()", e);
			throw new ApplicationException("Exception in searching Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
		
	}

	/**
	 * Maps ResultSet row to DoctorBean.
	 * 
	 * @param rs ResultSet
	 * @return PatientBean
	 * @throws Exception
	 */
	private DoctorBean mapResultSetToBean(ResultSet rs) throws Exception {

	    DoctorBean bean = new DoctorBean();

	    bean.setId(rs.getLong("id"));
	    bean.setName(rs.getString("name"));
	    bean.setDateOfBirth(rs.getDate("date_of_birth"));
	    bean.setMobile(rs.getString("mobile"));
	    bean.setExpertise(rs.getString("expertise"));
	    bean.setCreatedBy(rs.getString("created_by"));
	    bean.setModifiedBy(rs.getString("modified_by"));
	    bean.setCreatedDatetime(rs.getTimestamp("created_datetime"));
	    bean.setModifiedDatetime(rs.getTimestamp("modified_datetime"));

	    return bean;
	}
}
