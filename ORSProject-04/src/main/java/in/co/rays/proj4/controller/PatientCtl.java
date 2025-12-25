package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.PatientBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.PatientModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * PatientCtl handles Patient create, update, view and validation operations.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
@WebServlet(name = "PatientCtl", urlPatterns = { "/ctl/PatientCtl" })
public class PatientCtl extends BaseCtl {

    /** Logger instance for PatientCtl */
    private static Logger log = Logger.getLogger(PatientCtl.class);

    /**
     * Preloads disease list for dropdown.
     *
     * @param request HttpServletRequest
     */
    @Override
    protected void preload(HttpServletRequest request) {

        log.debug("PatientCtl preload started");

        HashMap<String, String> diseaseMap = new HashMap<>();
        diseaseMap.put("Diabetes", "Diabetes");
        diseaseMap.put("Hypertension", "Hypertension");
        diseaseMap.put("Asthma", "Asthma");
        diseaseMap.put("Tuberculosis", "Tuberculosis");
        diseaseMap.put("Malaria", "Malaria");
        diseaseMap.put("Alzheimer's", "Alzheimer's");
        diseaseMap.put("Parkinson's", "Parkinson's");
        diseaseMap.put("Hepatitis", "Hepatitis");
        diseaseMap.put("Cholera", "Cholera");
        diseaseMap.put("Ebola", "Ebola");

        request.setAttribute("diseaseMap", diseaseMap);

        log.debug("PatientCtl preload completed");
    }

    /**
     * Validates patient form data.
     *
     * @param request HttpServletRequest
     * @return true if valid, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("PatientCtl validate started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", "Invalid Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("dateOfVisit"))) {
            request.setAttribute("dateOfVisit", PropertyReader.getValue("error.require", "Date of Visit"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("dateOfVisit"))) {
            request.setAttribute("dateOfVisit", PropertyReader.getValue("error.date", "Date of Visit"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("mobile"))) {
            request.setAttribute("mobile", PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
        } else if (!DataValidator.isPhoneLength(request.getParameter("mobile"))) {
            request.setAttribute("mobile", "Mobile No must have 10 digits");
            pass = false;
        } else if (!DataValidator.isPhoneNo(request.getParameter("mobile"))) {
            request.setAttribute("mobile", "Invalid Mobile No");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("disease"))) {
            request.setAttribute("disease", PropertyReader.getValue("error.require", "Disease"));
            pass = false;
        }

        log.debug("PatientCtl validate completed with status : " + pass);
        return pass;
    }

    /**
     * Populates PatientBean from request parameters.
     *
     * @param request HttpServletRequest
     * @return populated PatientBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("PatientCtl populateBean started");

        PatientBean bean = new PatientBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDateOfVisit(DataUtility.getDate(request.getParameter("dateOfVisit")));
        bean.setMobile(DataUtility.getString(request.getParameter("mobile")));
        bean.setDisease(DataUtility.getString(request.getParameter("disease")));

        populateDTO(bean, request);

        log.debug("PatientCtl populateBean completed : " + bean);
        return bean;
    }

    /**
     * Handles GET request (load patient data for edit/view).
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("PatientCtl doGet started");

        long id = DataUtility.getLong(req.getParameter("id"));
        PatientModel model = new PatientModel();

        if (id > 0) {
            try {
                PatientBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, req);
            } catch (ApplicationException e) {
                log.error("Error in doGet", e);
                ServletUtility.handleException(e, req, resp);
                return;
            }
        }
        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Handles POST request (save, update, reset, cancel).
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String op = DataUtility.getString(req.getParameter("operation"));
        long id = DataUtility.getLong(req.getParameter("id"));

        log.info("PatientCtl doPost operation : " + op);

        PatientModel model = new PatientModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {

            PatientBean bean = (PatientBean) populateBean(req);

            try {
                model.add(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Patient added successfully", req);
                log.info("Patient added successfully");
            } catch (DuplicateRecordException e) {
                log.warn("Duplicate patient record", e);
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Patient already exists", req);
            } catch (ApplicationException e) {
                log.error("Error while adding patient", e);
                ServletUtility.handleException(e, req, resp);
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

            PatientBean bean = (PatientBean) populateBean(req);

            try {
                if (id > 0) {
                    model.update(bean);
                }
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Patient updated successfully", req);
                log.info("Patient updated successfully");
            } catch (DuplicateRecordException e) {
                log.warn("Duplicate patient record", e);
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Patient already exists", req);
            } catch (ApplicationException e) {
                log.error("Error while updating patient", e);
                ServletUtility.handleException(e, req, resp);
                return;
            }

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.PATIENT_CTL, req, resp);
            return;

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.PATIENT_LIST_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Returns Patient view page.
     *
     * @return view path
     */
    @Override
    protected String getView() {
        return ORSView.PATIENT_VIEW;
    }
}
