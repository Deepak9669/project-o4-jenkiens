package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.exception.RecordNotFoundException;
import in.co.rays.proj4.util.EmailBuilder;
import in.co.rays.proj4.util.EmailMessage;
import in.co.rays.proj4.util.EmailUtility;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * Model class for User entity. Provides methods for CRUD operations,
 * authentication, password management and registration.
 *
 * @author Deepak Verma
 * @version 1.0
 */
public class UserModel {

    private static Logger log = Logger.getLogger(UserModel.class);

    /**
     * Gets the next primary key value for st_user table.
     */
    public Integer nextPk() throws DatabaseException {

        Connection conn = null;
        int pk = 0;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement("select max(id) from st_user");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                pk = rs.getInt(1);
            }
            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in nextPk()", e);
            throw new DatabaseException("Exception in getting PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk + 1;
    }

    /**
     * Adds a new User record.
     */
    public long add(UserBean bean)
            throws ApplicationException, DuplicateRecordException {

        log.info("add() called");

        UserBean existBean = findByLogin(bean.getLogin());
        if (existBean != null) {
            throw new DuplicateRecordException("User already exists");
        }

        Connection conn = null;
        int pk = 0;

        try {
            pk = nextPk();
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt =
                    conn.prepareStatement("insert into st_user values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pstmt.setInt(1, pk);
            pstmt.setString(2, bean.getFirstName());
            pstmt.setString(3, bean.getLastName());
            pstmt.setString(4, bean.getLogin());
            pstmt.setString(5, bean.getPassword());
            pstmt.setDate(6, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(7, bean.getMobileNo());
            pstmt.setLong(8, bean.getRoleId());
            pstmt.setString(9, bean.getGender());
            pstmt.setString(10, bean.getCreatedBy());
            pstmt.setString(11, bean.getModifiedBy());
            pstmt.setTimestamp(12, bean.getCreatedDatetime());
            pstmt.setTimestamp(13, bean.getModifiedDatetime());

            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();

            log.info("User added successfully PK : " + pk);

        } catch (Exception e) {
            log.error("Exception in add()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException(
                        "Add rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in add User");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    /**
     * Deletes a User record.
     */
    public void delete(UserBean bean) throws ApplicationException {

        log.info("delete() called ID : " + bean.getId());
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt =
                    conn.prepareStatement("delete from st_user where id=?");
            pstmt.setLong(1, bean.getId());
            pstmt.executeUpdate();

            conn.commit();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in delete()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException(
                        "Delete rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in delete User");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Updates a User record.
     */
    public void update(UserBean bean)
            throws DuplicateRecordException, ApplicationException {

        log.info("update() called ID : " + bean.getId());

        UserBean existBean = findByLogin(bean.getLogin());
        if (existBean != null && existBean.getId() != bean.getId()) {
            throw new DuplicateRecordException("User already exists");
        }

        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                "update st_user set first_name=?, last_name=?, login=?, password=?, dob=?, "
              + "mobile_no=?, role_id=?, gender=?, created_by=?, modified_by=?, "
              + "created_datetime=?, modified_datetime=? where id=?");

            pstmt.setString(1, bean.getFirstName());
            pstmt.setString(2, bean.getLastName());
            pstmt.setString(3, bean.getLogin());
            pstmt.setString(4, bean.getPassword());
            pstmt.setDate(5, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(6, bean.getMobileNo());
            pstmt.setLong(7, bean.getRoleId());
            pstmt.setString(8, bean.getGender());
            pstmt.setString(9, bean.getCreatedBy());
            pstmt.setString(10, bean.getModifiedBy());
            pstmt.setTimestamp(11, bean.getCreatedDatetime());
            pstmt.setTimestamp(12, bean.getModifiedDatetime());
            pstmt.setLong(13, bean.getId());

            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in update()", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException(
                        "Update rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in updating User");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Finds User by primary key.
     */
    public UserBean findByPk(long pk) throws ApplicationException {

        UserBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement("select * from st_user where id=?");
            pstmt.setLong(1, pk);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new UserBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setLogin(rs.getString(4));
                bean.setPassword(rs.getString(5));
                bean.setDob(rs.getDate(6));
                bean.setMobileNo(rs.getString(7));
                bean.setRoleId(rs.getLong(8));
                bean.setGender(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }
            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByPk()", e);
            throw new ApplicationException("Exception in get User by PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Finds User by login.
     */
    public UserBean findByLogin(String login) throws ApplicationException {

        UserBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement("select * from st_user where login=?");
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new UserBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setLogin(rs.getString(4));
                bean.setPassword(rs.getString(5));
                bean.setDob(rs.getDate(6));
                bean.setMobileNo(rs.getString(7));
                bean.setRoleId(rs.getLong(8));
                bean.setGender(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }
            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in findByLogin()", e);
            throw new ApplicationException("Exception in get User by Login");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Authenticates User.
     */
    public UserBean authenticate(String login, String password)
            throws ApplicationException {

        UserBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "select * from st_user where login=? and password=?");
            pstmt.setString(1, login);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = findByPk(rs.getLong(1));
            }
            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in authenticate()", e);
            throw new ApplicationException("Invalid Login or Password");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * List all users.
     */
    public List<UserBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    /**
     * Search Users.
     */
    public List<UserBean> search(UserBean bean, int pageNo, int pageSize)
            throws ApplicationException {

        StringBuffer sql = new StringBuffer("select * from st_user where 1=1");

        if (bean != null) {
            if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
                sql.append(" and first_name like '" + bean.getFirstName() + "%'");
            }
            if (bean.getLogin() != null && bean.getLogin().length() > 0) {
                sql.append(" and login like '" + bean.getLogin() + "%'");
            }
            if (bean.getRoleId() > 0) {
                sql.append(" and role_id = " + bean.getRoleId());
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + "," + pageSize);
        }

        List<UserBean> list = new ArrayList<>();
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(findByPk(rs.getLong(1)));
            }
            rs.close();
            pstmt.close();

        } catch (Exception e) {
            log.error("Exception in search()", e);
            throw new ApplicationException("Exception in search User");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return list;
    }

    /**
     * Change password.
     */
    public boolean changePassword(Long id, String oldPassword, String newPassword)
            throws RecordNotFoundException, ApplicationException {

        UserBean bean = findByPk(id);

        if (bean != null && bean.getPassword().equals(oldPassword)) {
            bean.setPassword(newPassword);
            try {
                update(bean);
            } catch (DuplicateRecordException e) {
                throw new ApplicationException("Login already exists");
            }
        } else {
            throw new RecordNotFoundException("Old Password is Invalid");
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("login", bean.getLogin());
        map.put("password", bean.getPassword());

        String message = EmailBuilder.getChangePasswordMessage(map);

        EmailMessage msg = new EmailMessage();
        msg.setTo(bean.getLogin());
        msg.setSubject("Password Changed Successfully");
        msg.setMessage(message);
        msg.setMessageType(EmailMessage.HTML_MSG);

        EmailUtility.sendMail(msg);

        return true;
    }

    /**
     * Forget password.
     */
    public boolean forgetPassword(String login)
            throws RecordNotFoundException, ApplicationException {

        UserBean bean = findByLogin(login);

        if (bean == null) {
            throw new RecordNotFoundException("Email does not exist");
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("login", bean.getLogin());
        map.put("password", bean.getPassword());

        String message = EmailBuilder.getForgetPasswordMessage(map);

        EmailMessage msg = new EmailMessage();
        msg.setTo(login);
        msg.setSubject("Password Recovery");
        msg.setMessage(message);
        msg.setMessageType(EmailMessage.HTML_MSG);

        EmailUtility.sendMail(msg);
        return true;
    }

    /**
     * Register user.
     */
    public long registerUser(UserBean bean)
            throws DuplicateRecordException, ApplicationException {

        long pk = add(bean);

        HashMap<String, String> map = new HashMap<>();
        map.put("login", bean.getLogin());
        map.put("password", bean.getPassword());

        String message = EmailBuilder.getUserRegistrationMessage(map);

        EmailMessage msg = new EmailMessage();
        msg.setTo(bean.getLogin());
        msg.setSubject("Registration Successful");
        msg.setMessage(message);
        msg.setMessageType(EmailMessage.HTML_MSG);

        EmailUtility.sendMail(msg);
        return pk;
    }
}
