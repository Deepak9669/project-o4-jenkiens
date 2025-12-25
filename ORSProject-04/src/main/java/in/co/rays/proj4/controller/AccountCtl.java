package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.AccountBean;
import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.AccountModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "AccountCtl", urlPatterns = { "/ctl/AccountCtl" })
public class AccountCtl extends BaseCtl {
	

	@Override
	protected void preload(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("SAVING", "SAVING");
		map.put("CURRENT", "CURRENT");
		map.put("BUSINESS", "BUSINESS");
		request.setAttribute("map", map);

	}


    @Override
    protected boolean validate(HttpServletRequest request) {

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("accountNo"))) {
            request.setAttribute("accountNo",
                    PropertyReader.getValue("error.require", "Account No"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("accountType"))) {
            request.setAttribute("accountType",
                    PropertyReader.getValue("error.require", "Account Type"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("bankName"))) {
            request.setAttribute("bankName",
                    PropertyReader.getValue("error.require", "Bank Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("balance"))) {
            request.setAttribute("balance",
                    PropertyReader.getValue("error.require", "Balance"));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        AccountBean bean = new AccountBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setAccountNo(DataUtility.getString(request.getParameter("accountNo")));
        bean.setAccountType(DataUtility.getString(request.getParameter("accountType")));
        bean.setBankName(DataUtility.getString(request.getParameter("bankName")));
        bean.setBalance(DataUtility.getString(request.getParameter("balance")));

        populateDTO(bean, request);

        return bean;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long id = DataUtility.getLong(request.getParameter("id"));
        AccountModel model = new AccountModel();

        if (id > 0) {
            try {
                AccountBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        ServletUtility.forward(getView(), request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String op = DataUtility.getString(request.getParameter("operation"));
        AccountModel model = new AccountModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op)) {

            AccountBean bean = (AccountBean) populateBean(request);

            try {
                model.add(bean);
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Data is successfully saved", request);
            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Account No already exists", request);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

            AccountBean bean = (AccountBean) populateBean(request);

            try {
                if (id > 0) {
                    model.update(bean);
                }
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Data is successfully updated", request);
            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Account No already exists", request);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.ACCOUNT_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
    }

    @Override
    protected String getView() {
        return ORSView.ACCOUNT_VIEW;
    }
}