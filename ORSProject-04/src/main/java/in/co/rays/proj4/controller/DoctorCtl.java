<<<<<<< HEAD

=======
>>>>>>> 3688eb8f39ce8a09dde7a441b05719e5b2706dc5
package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.DoctorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.DocterModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * DoctorCtl handles Doctor related operations such as
 * add, update, view and validation.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
@WebServlet(name = "DoctorCtl", urlPatterns = { "/ctl/DoctorCtl" })
public class DoctorCtl extends BaseCtl {

    /** Logger instance */
    private static Logger log = Logger.getLogger(DoctorCtl.class);

    /**
     * Preloads expertise list for dropdown.
     *
     * @param request HttpServletRequest
     */
    @Override
    protected void preload(HttpServletRequest request) {

        log.debug("DoctorCtl preload started");

        HashMap<String, String> expertiseMap = new HashMap<>();
        expertiseMap.put("Ear", "Ear");
        expertiseMap.put("Nose", "Nose");
        expertiseMap.put("Throat", "Throat");

        request.setAttribute("expertiseMap", expertiseMap);

        log.debug("DoctorCtl preload completed");
    }

    /**
     * Validates Doctor form data.
     *
     * @param request HttpServletRequest
     * @return true if valid, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("DoctorCtl validate started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name",
                    PropertyReader.getValue("error.require", "Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", "Invalid Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("dateOfBirth"))) {
            request.setAttribute("dateOfBirth",
                    PropertyReader.getValue("error.require", "Date of Birth"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("dateOfBirth"))) {
            request.setAttribute("dateOfBirth",
                    PropertyReader.getValue("error.date", "Date of Birth"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("mobile"))) {
            request.setAttribute("mobile",
                    PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
        } else if (!DataValidator.isPhoneLength(request.getParameter("mobile"))) {
            request.setAttribute("mobile",
                    "Mobile No must have 10 digits");
            pass = false;
        } else if (!DataValidator.isPhoneNo(request.getParameter("mobile"))) {
            request.setAttribute("mobile", "Invalid Mobile No");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("expertise"))) {
            request.setAttribute("expertise",
                    PropertyReader.getValue("error.require", "Expertise"));
            pass = false;
        }

        log.debug("DoctorCtl validate completed with status : " + pass);
        return pass;
    }

    /**
     * Populates DoctorBean from request parameters.
     *
     * @param request HttpServletRequest
     * @return populated DoctorBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("DoctorCtl populateBean started");

        DoctorBean bean = new DoctorBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDateOfBirth(
                DataUtility.getDate(request.getParameter("dateOfBirth")));
        bean.setMobile(DataUtility.getString(request.getParameter("mobile")));
        bean.setExpertise(
                DataUtility.getString(request.getParameter("expertise")));

        populateDTO(bean, request);

        log.debug("DoctorCtl populateBean completed : " + bean);
        return bean;
    }

    /**
     * Handles GET request to load Doctor data.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("DoctorCtl doGet started");

        long id = DataUtility.getLong(req.getParameter("id"));
        DocterModel model = new DocterModel();

        if (id > 0) {
            try {
                DoctorBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, req);
            } catch (ApplicationException e) {
                log.error("Error in DoctorCtl doGet", e);
                ServletUtility.handleException(e, req, resp);
                return;
            }
        }

        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Handles POST request for save, update, reset and cancel operations.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String op = DataUtility.getString(req.getParameter("operation"));
        long id = DataUtility.getLong(req.getParameter("id"));

        log.info("DoctorCtl doPost operation : " + op);

        DocterModel model = new DocterModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {

            DoctorBean bean = (DoctorBean) populateBean(req);

            try {
                model.add(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage(
                        "Doctor added successfully", req);
                log.info("Doctor added successfully");
            } catch (DuplicateRecordException e) {
                log.warn("Duplicate doctor record", e);
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage(
                        "Doctor already exists", req);
            } catch (ApplicationException e) {
                log.error("Error while adding doctor", e);
                ServletUtility.handleException(e, req, resp);
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

            DoctorBean bean = (DoctorBean) populateBean(req);

            try {
                if (id > 0) {
                    model.update(bean);
                }
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage(
                        "Doctor updated successfully", req);
                log.info("Doctor updated successfully");
            } catch (DuplicateRecordException e) {
                log.warn("Duplicate doctor record", e);
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage(
                        "Doctor already exists", req);
            } catch (ApplicationException e) {
                log.error("Error while updating doctor", e);
                ServletUtility.handleException(e, req, resp);
                return;
            }

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.DOCTOR_CTL, req, resp);
            return;

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(
                    ORSView.DOCTOR_LIST_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Returns Doctor view page.
     *
     * @return view path
     */
    @Override
    protected String getView() {
        return ORSView.DOCTOR_VIEW;
    }
}
