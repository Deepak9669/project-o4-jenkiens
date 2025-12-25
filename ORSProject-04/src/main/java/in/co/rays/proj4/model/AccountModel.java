package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.AccountBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class AccountModel {

    public Integer nextPk() throws DatabaseException {
        Connection conn = null;
        int pk = 0;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_account");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pk = rs.getInt(1);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            throw new DatabaseException("Exception : Exception in getting PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk + 1;
    }

    public long add(AccountBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        int pk = 0;

        AccountBean duplicate = findByAccountNo(bean.getAccountNo());

        if (duplicate != null) {
            throw new DuplicateRecordException("Account No already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            pk = nextPk();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into st_account values(?, ?, ?, ?, ?, ?, ?, ?, ?)");

            pstmt.setInt(1, pk);
            pstmt.setString(2, bean.getAccountNo());
            pstmt.setString(3, bean.getAccountType());
            pstmt.setString(4, bean.getBankName());
            pstmt.setString(5, bean.getBalance());
            pstmt.setString(6, bean.getCreatedBy());
            pstmt.setString(7, bean.getModifiedBy());
            pstmt.setTimestamp(8, bean.getCreatedDatetime());
            pstmt.setTimestamp(9, bean.getModifiedDatetime());

            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception : Exception in add Account");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    public void update(AccountBean bean) throws ApplicationException, DuplicateRecordException {

        Connection conn = null;

        AccountBean existBean = findByAccountNo(bean.getAccountNo());

        if (existBean != null && existBean.getId() != bean.getId()) {
            throw new DuplicateRecordException("Account already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_account set account_no=?, account_type=?, bank_name=?, balance=?, created_by=?, modified_by=?, created_datetime=?, modified_datetime=? where id=?");

            pstmt.setString(1, bean.getAccountNo());
            pstmt.setString(2, bean.getAccountType());
            pstmt.setString(3, bean.getBankName());
            pstmt.setString(4, bean.getBalance());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDatetime());
            pstmt.setTimestamp(8, bean.getModifiedDatetime());
            pstmt.setLong(9, bean.getId());

            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception : Exception in updating Account");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public void delete(AccountBean bean) throws ApplicationException {
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("delete from st_account where id=?");
            pstmt.setLong(1, bean.getId());
            pstmt.executeUpdate();

            conn.commit();
            pstmt.close();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception : Exception in delete Account");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public AccountBean findByPk(long pk) throws ApplicationException {

        StringBuffer sql = new StringBuffer("select * from st_account where id=?");
        AccountBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, pk);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new AccountBean();
                bean.setId(rs.getLong(1));
                bean.setAccountNo(rs.getString(2));
                bean.setAccountType(rs.getString(3));
                bean.setBankName(rs.getString(4));
                bean.setBalance(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDatetime(rs.getTimestamp(8));
                bean.setModifiedDatetime(rs.getTimestamp(9));
            }
            rs.close();
            pstmt.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception : Exception in get Account by pk");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public AccountBean findByAccountNo(String accountNo) throws ApplicationException {

        StringBuffer sql = new StringBuffer("select * from st_account where account_no=?");
        AccountBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, accountNo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new AccountBean();
                bean.setId(rs.getLong(1));
                bean.setAccountNo(rs.getString(2));
                bean.setAccountType(rs.getString(3));
                bean.setBankName(rs.getString(4));
                bean.setBalance(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDatetime(rs.getTimestamp(8));
                bean.setModifiedDatetime(rs.getTimestamp(9));
            }
            rs.close();
            pstmt.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception : Exception in get Account by account no");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }
    

	public List<AccountBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}
	
	public List<AccountBean> search(AccountBean bean, int pageNo, int pageSize)
	        throws ApplicationException {

	    Connection conn = null;
	    ArrayList<AccountBean> list = new ArrayList<AccountBean>();

	    StringBuffer sql = new StringBuffer("select * from st_account where 1=1");

	    if (bean != null) {

	        if (bean.getId() > 0) {
	            sql.append(" and id = " + bean.getId());
	        }

	        if (bean.getAccountNo() != null && bean.getAccountNo().length() > 0) {
	            sql.append(" and account_no like '" + bean.getAccountNo() + "%'");
	        }

	        if (bean.getAccountType() != null && bean.getAccountType().length() > 0) {
	            sql.append(" and account_type like '" + bean.getAccountType() + "%'");
	        }

	        if (bean.getBankName() != null && bean.getBankName().length() > 0) {
	            sql.append(" and bank_name like '" + bean.getBankName() + "%'");
	        }

	        if (bean.getBalance() != null && bean.getBalance().length() > 0) {
	            sql.append(" and balance like '" + bean.getBalance() + "%'");
	        }
	    }

	    /* ========== Pagination ========== */
	    if (pageSize > 0) {
	        pageNo = (pageNo - 1) * pageSize;
	        sql.append(" limit " + pageNo + ", " + pageSize);
	    }

	    try {
	        conn = JDBCDataSource.getConnection();
	        PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {

	            bean = new AccountBean();

	            bean.setId(rs.getLong(1));              // id
	            bean.setAccountNo(rs.getString(2));     // account_no
	            bean.setAccountType(rs.getString(3));   // account_type
	            bean.setBankName(rs.getString(4));      // bank_name
	            bean.setBalance(rs.getString(5));       // balance

	            bean.setCreatedBy(rs.getString(6));
	            bean.setModifiedBy(rs.getString(7));
	            bean.setCreatedDatetime(rs.getTimestamp(8));
	            bean.setModifiedDatetime(rs.getTimestamp(9));

	            list.add(bean);
	        }

	        rs.close();
	        pstmt.close();

	    } catch (Exception e) {
	        throw new ApplicationException("Exception : Exception in search Account");
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }

	    return list;
	}

	
}
	
	
	
	
	
	