package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.model.UserModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * LoginCtl controller handles User Login, Logout and Sign Up navigation.
 * 
 * It validates login credentials and creates user session on successful login.
 * It also handles logout functionality.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
@WebServlet(name = "LoginCtl", urlPatterns = { "/LoginCtl" })
public class LoginCtl extends BaseCtl {

	public static final String OP_SIGN_IN = "Sign In";
	public static final String OP_SIGN_UP = "Sign Up";

	/**
	 * Validates Login form fields.
	 * 
	 * @param request HTTP request object
	 * @return true if validation passes, otherwise false
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		String op = request.getParameter("operation");

		// Skip validation for Sign Up and Logout
		if (OP_SIGN_UP.equals(op) || OP_LOG_OUT.equals(op)) {
			return pass;
		}

		if (DataValidator.isNull(request.getParameter("login"))) {
			request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("password"))) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			pass = false;
		}

		return pass;
	}

	/**
	 * Populates UserBean from login form inputs.
	 * 
	 * @param request HTTP request object
	 * @return populated UserBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		UserBean bean = new UserBean();

		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));

		return bean;
	}

	/**
	 * Handles GET request for Login page and Logout operation.
	 * 
	 * @param request  HTTP request
	 * @param response HTTP response
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		String op = DataUtility.getString(request.getParameter("operation"));

		// Handle Logout
		if (OP_LOG_OUT.equals(op)) {
			session.invalidate();
			ServletUtility.setSuccessMessage("Logout Successful!", request);
		}

		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Handles POST request for Login and Sign Up operations.
	 * 
	 * @param request  HTTP request
	 * @param response HTTP response
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		UserModel model = new UserModel();
		RoleModel role = new RoleModel();

		String op = DataUtility.getString(request.getParameter("operation"));

		// Login Operation
		if (OP_SIGN_IN.equalsIgnoreCase(op)) {

			UserBean bean = (UserBean) populateBean(request);

			try {

				bean = model.authenticate(bean.getLogin(), bean.getPassword());

				if (bean != null) {

					// Create session for user
					session.setAttribute("user", bean);

					// Fetch Role Name
					RoleBean rolebean = role.findByPk(bean.getRoleId());

					if (rolebean != null) {
						session.setAttribute("role", rolebean.getName());
					}
					String uri = DataUtility.getString(request.getParameter("uri"));
					System.out.println(uri);
					if (uri == null || "null".equalsIgnoreCase(uri)) {
					ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
					return;
					} else {
						
						ServletUtility.redirect(uri, request, response);
						return;
					}	

				} else {

					bean = (UserBean) populateBean(request);
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Invalid LoginId And Password", request);
				}

			} catch (ApplicationException e) {
				e.printStackTrace();
				ServletUtility.handleException(e, request, response);
				return;
			}

		}
		// Sign Up Navigation
		else if (OP_SIGN_UP.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Returns Login Page view.
	 * 
	 * @return Login JSP page constant
	 */
	@Override
	protected String getView() {
		return ORSView.LOGIN_VIEW;
	}
}
