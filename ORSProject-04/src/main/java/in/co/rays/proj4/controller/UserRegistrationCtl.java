package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.UserModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * UserRegistrationCtl is a Controller which handles User Registration process.
 * It validates input data and creates new user account in database.
 *
 * @author Deepak Verma
 * @version 1.0
 */
@WebServlet(name = "UserRegistrationCtl", urlPatterns = { "/UserRegistrationCtl" })
public class UserRegistrationCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(UserRegistrationCtl.class);

    public static final String OP_SIGN_UP = "Sign Up";

    /**
     * Validates all required fields for User Registration.
     *
     * @param request HTTP request
     * @return true if validation passes, otherwise false
     */
    @Override
    protected void preload(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
    	HashMap<String, String> map = new HashMap<String, String>();
	    
		map.put("Female", "Female");
		map.put("Male", "Male");
		
		request.setAttribute("map", map);
	}
    
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("UserRegistrationCtl validate started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("firstName"))) {
            request.setAttribute("firstName",
                    PropertyReader.getValue("error.require", "First Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("firstName"))) {
            request.setAttribute("firstName", "Invalid First Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("lastName"))) {
            request.setAttribute("lastName",
                    PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("lastName"))) {
            request.setAttribute("lastName", "Invalid Last Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("login"))) {
            request.setAttribute("login",
                    PropertyReader.getValue("error.require", "Login Id"));
            pass = false;
        } else if (!DataValidator.isEmail(request.getParameter("login"))) {
            request.setAttribute("login",
                    PropertyReader.getValue("error.email", "Login"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("password"))) {
            request.setAttribute("password",
                    PropertyReader.getValue("error.require", "Password"));
            pass = false;
        } else if (!DataValidator.isPasswordLength(request.getParameter("password"))) {
            request.setAttribute("password",
                    "Password should be 8 to 12 characters");
            pass = false;
        } else if (!DataValidator.isPassword(request.getParameter("password"))) {
            request.setAttribute("password",
                    "Must contain uppercase, lowercase, digit & special character");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword",
                    PropertyReader.getValue("error.require", "Confirm Password"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("gender"))) {
            request.setAttribute("gender",
                    PropertyReader.getValue("error.require", "Gender"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob",
                    PropertyReader.getValue("error.require", "Date of Birth"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("dob"))) {
            request.setAttribute("dob",
                    PropertyReader.getValue("error.date", "Date of Birth"));
            pass = false;
        }

        if (!request.getParameter("password")
                .equals(request.getParameter("confirmPassword"))
                && !"".equals(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword",
                    "Password and Confirm Password must be Same!");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo",
                    PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
        } else if (!DataValidator.isPhoneLength(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo",
                    "Mobile No must have 10 digits");
            pass = false;
        } else if (!DataValidator.isPhoneNo(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Invalid Mobile No");
            pass = false;
        }

        return pass;
    }

    /**
     * Populates UserBean object using request parameters.
     *
     * @param request HTTP request
     * @return populated UserBean as BaseBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("UserRegistrationCtl populateBean started");

        UserBean bean = new UserBean();

        bean.setFirstName(DataUtility.getString(
                request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(
                request.getParameter("lastName")));
        bean.setLogin(DataUtility.getString(
                request.getParameter("login")));
        bean.setPassword(DataUtility.getString(
                request.getParameter("password")));
        bean.setConfirmPassword(DataUtility.getString(
                request.getParameter("confirmPassword")));
        bean.setGender(DataUtility.getString(
                request.getParameter("gender")));
        bean.setDob(DataUtility.getDate(
                request.getParameter("dob")));
        bean.setMobileNo(DataUtility.getString(
                request.getParameter("mobileNo")));

        // Default Role = Student
        bean.setRoleId(RoleBean.STUDENT);

        populateDTO(bean, request);

        return bean;
    }

    /**
     * Handles GET request - loads registration page.
     *
     * @param request HTTP request
     * @param response HTTP response
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("UserRegistrationCtl doGet started");

        ServletUtility.forward(getView(), request, response);
    }

    /**
     * Handles POST request - performs signup and reset operations.
     *
     * @param request HTTP request
     * @param response HTTP response
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("UserRegistrationCtl doPost started");

        String op = DataUtility.getString(
                request.getParameter("operation"));

        UserModel model = new UserModel();

        if (OP_SIGN_UP.equalsIgnoreCase(op)) {

            UserBean bean = (UserBean) populateBean(request);

            try {
                model.add(bean);
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage(
                        "Registration successful!", request);

            } catch (DuplicateRecordException e) {

                log.error("Duplicate login during registration", e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage(
                        "Login id already exists", request);

            } catch (ApplicationException e) {

                log.error("Error during registration", e);
                ServletUtility.handleException(e, request, response);
                return;
            }

            ServletUtility.forward(getView(), request, response);

        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(
                    ORSView.USER_REGISTRATION_CTL,
                    request, response);
            return;
        }
    }

    /**
     * Returns registration view page path.
     *
     * @return User Registration view page
     */
    @Override
    protected String getView() {
        return ORSView.USER_REGISTRATION_VIEW;
    }
}
