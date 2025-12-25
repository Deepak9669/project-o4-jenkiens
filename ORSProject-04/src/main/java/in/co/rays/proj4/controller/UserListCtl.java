package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.model.UserModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * UserListCtl Controller handles listing, searching, pagination
 * and deletion of User records.
 *
 * It also preloads Role list for filtering users by role.
 *
 * @author Deepak Verma
 * @version 1.0
 */
@WebServlet(name = "UserListCtl", urlPatterns = { "/ctl/UserListCtl" })
public class UserListCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(UserListCtl.class);

    /**
     * Preloads Role list to be used as filter on User List page.
     *
     * @param request HTTP request
     */
    @Override
    protected void preload(HttpServletRequest request) {

        log.debug("UserListCtl preload started");

        RoleModel roleModel = new RoleModel();
        try {
            List roleList = roleModel.list();
            request.setAttribute("roleList", roleList);
        } catch (ApplicationException e) {
            log.error("Error in preload()", e);
        }
    }

    /**
     * Populates UserBean from request parameters to use as
     * search criteria on User List page.
     *
     * @param request HTTP request
     * @return populated UserBean as BaseBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("UserListCtl populateBean started");

        UserBean bean = new UserBean();

        bean.setFirstName(DataUtility.getString(
                request.getParameter("firstName")));
        bean.setLogin(DataUtility.getString(
                request.getParameter("login")));
        bean.setRoleId(DataUtility.getLong(
                request.getParameter("roleId")));

        return bean;
    }

    /**
     * Handles GET request to load User List page initially.
     *
     * @param request  HTTP request
     * @param response HTTP response
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("UserListCtl doGet started");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(
                PropertyReader.getValue("page.size"));

        UserBean bean = (UserBean) populateBean(request);
        UserModel model = new UserModel();

        try {
            List<UserBean> list = model.search(bean, pageNo, pageSize);
            List<UserBean> next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage("No record found", request);
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.setBean(bean, request);

            request.setAttribute("nextListSize", next.size());

            ServletUtility.forward(getView(), request, response);

        } catch (ApplicationException e) {
            log.error("Error in doGet()", e);
            ServletUtility.handleException(e, request, response);
        }
    }

    /**
     * Handles POST request for Search, Next, Previous, New,
     * Delete, Reset and Back operations on User List page.
     *
     * @param request  HTTP request
     * @param response HTTP response
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("UserListCtl doPost started");

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

        UserBean bean = (UserBean) populateBean(request);
        UserModel model = new UserModel();

        String[] ids = request.getParameterValues("ids");
        String op = DataUtility.getString(
                request.getParameter("operation"));

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
                        ORSView.USER_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {

                pageNo = 1;

                if (ids != null && ids.length > 0) {

                    UserBean deleteBean = new UserBean();

                    for (String id : ids) {
                        deleteBean.setId(
                                DataUtility.getInt(id));
                        model.delete(deleteBean);
                    }

                    ServletUtility.setSuccessMessage(
                            "User deleted successfully", request);

                } else {
                    ServletUtility.setErrorMessage(
                            "Select at least one record", request);
                }

            } else if (OP_RESET.equalsIgnoreCase(op)
                    || OP_BACK.equalsIgnoreCase(op)) {

                ServletUtility.redirect(
                        ORSView.USER_LIST_CTL, request, response);
                return;
            }

            list = model.search(bean, pageNo, pageSize);
            next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage("No record found", request);
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.setBean(bean, request);

            request.setAttribute("nextListSize", next.size());

            ServletUtility.forward(getView(), request, response);

        } catch (ApplicationException e) {
            log.error("Error in doPost()", e);
            ServletUtility.handleException(e, request, response);
        }
    }

    /**
     * Returns User List view JSP path.
     *
     * @return User List view page constant
     */
    @Override
    protected String getView() {
        return ORSView.USER_LIST_VIEW;
    }
}
