package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.DoctorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.DocterModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * DoctorListCtl is responsible for handling operations related to
 * listing, searching, deleting, and paginating Doctor records.
 * 
 * It extends BaseCtl to reuse common controller functionalities.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
@WebServlet(name = "DoctorListCtl", urlPatterns = { "/ctl/DoctorListCtl" })
public class DoctorListCtl extends BaseCtl {

    /** Logger instance */
    private static Logger log = Logger.getLogger(DoctorListCtl.class);

    /**
     * Preloads unique expertise list for search dropdown.
     *
     * @param request HttpServletRequest
     */
    @Override
    protected void preload(HttpServletRequest request) {

        log.debug("DoctorListCtl preload started");

        DocterModel model = new DocterModel();
        HashMap<String, String> expertiseMap = new HashMap<>();

        try {
            List<DoctorBean> list = model.list();
            for (DoctorBean bean : list) {
                expertiseMap.put(bean.getExpertise(), bean.getExpertise());
            }
            request.setAttribute("expertiseMap", expertiseMap);

        } catch (ApplicationException e) {
            log.error("Error while preloading expertise list", e);
        }

        log.debug("DoctorListCtl preload completed");
    }

    /**
     * Populates DoctorBean for search criteria.
     *
     * @param request HttpServletRequest
     * @return populated DoctorBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("DoctorListCtl populateBean started");

        DoctorBean bean = new DoctorBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDateOfBirth(
                DataUtility.getDate(request.getParameter("dateOfBirth")));
        bean.setMobile(DataUtility.getString(request.getParameter("mobile")));
        bean.setExpertise(
                DataUtility.getString(request.getParameter("expertise")));

        log.debug("DoctorListCtl populateBean completed");
        return bean;
    }

    /**
     * Handles HTTP GET request and displays doctor list.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("DoctorListCtl doGet started");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(
                PropertyReader.getValue("page.size"));

        DoctorBean bean = (DoctorBean) populateBean(req);
        DocterModel model = new DocterModel();

        try {
            List<DoctorBean> list = model.search(bean, pageNo, pageSize);
            List<DoctorBean> next =
                    model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage("No record found", req);
            }

            ServletUtility.setList(list, req);
            ServletUtility.setPageNo(pageNo, req);
            ServletUtility.setPageSize(pageSize, req);
            ServletUtility.setBean(bean, req);
            req.setAttribute("nextListSize", next.size());

            ServletUtility.forward(getView(), req, resp);

        } catch (ApplicationException e) {
            log.error("Error in DoctorListCtl doGet", e);
            ServletUtility.handleException(e, req, resp);
        }
    }

    /**
     * Handles HTTP POST request for search, pagination, delete, reset and back.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("DoctorListCtl doPost started");

        int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(
                PropertyReader.getValue("page.size"));

        pageNo = (pageNo == 0) ? 1 : pageNo;

        DoctorBean bean = (DoctorBean) populateBean(req);
        DocterModel model = new DocterModel();

        String op = DataUtility.getString(req.getParameter("operation"));
        String[] ids = req.getParameterValues("ids");

        try {

            if (OP_SEARCH.equalsIgnoreCase(op)) {
                pageNo = 1;

            } else if (OP_NEXT.equalsIgnoreCase(op)) {
                pageNo++;

            } else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
                pageNo--;

            } else if (OP_NEW.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.DOCTOR_CTL, req, resp);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {

                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    for (String id : ids) {
                        model.delete(DataUtility.getLong(id));
                    }
                    ServletUtility.setSuccessMessage(
                            "Doctor deleted successfully", req);
                } else {
                    ServletUtility.setErrorMessage(
                            "Select at least one record", req);
                }

            } else if (OP_RESET.equalsIgnoreCase(op)
                    || OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(
                        ORSView.DOCTOR_LIST_CTL, req, resp);
                return;
            }

            List<DoctorBean> list =
                    model.search(bean, pageNo, pageSize);
            List<DoctorBean> next =
                    model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage("No record found", req);
            }

            ServletUtility.setBean(bean, req);
            ServletUtility.setList(list, req);
            ServletUtility.setPageNo(pageNo, req);
            ServletUtility.setPageSize(pageSize, req);
            req.setAttribute("nextListSize", next.size());

        } catch (ApplicationException e) {
            log.error("Error in DoctorListCtl doPost", e);
            ServletUtility.handleException(e, req, resp);
            return;
        }

        log.info("DoctorListCtl doPost completed");
        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Returns Doctor list view page.
     *
     * @return view path
     */
    @Override
    protected String getView() {
        return ORSView.DOCTOR_LIST_VIEW;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 3688eb8f39ce8a09dde7a441b05719e5b2706dc5
