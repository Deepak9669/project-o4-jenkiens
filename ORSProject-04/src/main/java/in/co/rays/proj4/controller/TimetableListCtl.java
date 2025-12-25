package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.TimetableBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.CourseModel;
import in.co.rays.proj4.model.SubjectModel;
import in.co.rays.proj4.model.TimetableModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * TimetableListCtl Controller handles listing, searching, pagination
 * and deletion of Timetable records.
 *
 * It also preloads Course and Subject data for filters on list page.
 *
 * @author Deepak Verma
 * @version 1.0
 */
@WebServlet(name = "TimetableListCtl", urlPatterns = { "/ctl/TimetableListCtl" })
public class TimetableListCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(TimetableListCtl.class);

    /**
     * Preloads Subject and Course lists for filters in Timetable List page.
     *
     * @param request HTTP request object
     */
    @Override
    protected void preload(HttpServletRequest request) {

        log.debug("TimetableListCtl preload started");

        SubjectModel subjectModel = new SubjectModel();
        CourseModel courseModel = new CourseModel();

        try {
            List subjectList = subjectModel.list();
            request.setAttribute("subjectList", subjectList);

            List courseList = courseModel.list();
            request.setAttribute("courseList", courseList);

        } catch (ApplicationException e) {
            log.error("Error in preload()", e);
        }
    }

    /**
     * Populates TimetableBean using request parameters for searching.
     *
     * @param request HTTP request object
     * @return populated TimetableBean as BaseBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("TimetableListCtl populateBean started");

        TimetableBean bean = new TimetableBean();

        bean.setCourseId(DataUtility.getLong(
                request.getParameter("courseId")));
        bean.setSubjectId(DataUtility.getLong(
                request.getParameter("subjectId")));
        bean.setExamDate(DataUtility.getDate(
                request.getParameter("examDate")));

        return bean;
    }

    /**
     * Handles GET request to display Timetable List page with initial data.
     *
     * @param request  HTTP request
     * @param response HTTP response
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("TimetableListCtl doGet started");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(
                PropertyReader.getValue("page.size"));

        TimetableBean bean = (TimetableBean) populateBean(request);
        TimetableModel model = new TimetableModel();

        try {
            List<TimetableBean> list =
                    model.search(bean, pageNo, pageSize);
            List<TimetableBean> next =
                    model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage(
                        "No record found", request);
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.setBean(bean, request);

            request.setAttribute(
                    "nextListSize", next.size());

            ServletUtility.forward(
                    getView(), request, response);

        } catch (ApplicationException e) {
            log.error("Error in doGet()", e);
            ServletUtility.handleException(
                    e, request, response);
        }
    }

    /**
     * Handles POST request for Search, Next, Previous, New, Delete,
     * Reset and Back operations on Timetable List page.
     *
     * @param request  HTTP request
     * @param response HTTP response
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("TimetableListCtl doPost started");

        List list = null;
        List next = null;

        int pageNo = DataUtility.getInt(
                request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(
                request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0)
                ? DataUtility.getInt(
                        PropertyReader.getValue("page.size"))
                : pageSize;

        TimetableBean bean =
                (TimetableBean) populateBean(request);
        TimetableModel model = new TimetableModel();

        String op = DataUtility.getString(
                request.getParameter("operation"));
        String[] ids =
                request.getParameterValues("ids");

        try {

            if (OP_SEARCH.equalsIgnoreCase(op)
                    || OP_NEXT.equalsIgnoreCase(op)
                    || OP_PREVIOUS.equalsIgnoreCase(op)) {

                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;
                } else if (OP_NEXT.equalsIgnoreCase(op)) {
                    pageNo++;
                } else if (OP_PREVIOUS.equalsIgnoreCase(op)
                        && pageNo > 1) {
                    pageNo--;
                }

            } else if (OP_NEW.equalsIgnoreCase(op)) {

                ServletUtility.redirect(
                        ORSView.TIMETABLE_CTL,
                        request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {

                pageNo = 1;

                if (ids != null && ids.length > 0) {

                    TimetableBean deleteBean =
                            new TimetableBean();

                    for (String id : ids) {
                        deleteBean.setId(
                                DataUtility.getInt(id));
                        model.delete(deleteBean);
                    }

                    ServletUtility.setSuccessMessage(
                            "Data is deleted successfully",
                            request);

                } else {
                    ServletUtility.setErrorMessage(
                            "Select at least one record",
                            request);
                }

            } else if (OP_RESET.equalsIgnoreCase(op)
                    || OP_BACK.equalsIgnoreCase(op)) {

                ServletUtility.redirect(
                        ORSView.TIMETABLE_LIST_CTL,
                        request, response);
                return;
            }

            list = model.search(bean, pageNo, pageSize);
            next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage(
                        "No record found", request);
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.setBean(bean, request);

            request.setAttribute(
                    "nextListSize", next.size());

            ServletUtility.forward(
                    getView(), request, response);

        } catch (ApplicationException e) {
            log.error("Error in doPost()", e);
            ServletUtility.handleException(
                    e, request, response);
        }
    }

    /**
     * Returns Timetable List view JSP path.
     *
     * @return Timetable List view constant
     */
    @Override
    protected String getView() {
        return ORSView.TIMETABLE_LIST_VIEW;
    }
}
