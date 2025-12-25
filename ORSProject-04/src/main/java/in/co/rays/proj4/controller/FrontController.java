package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.rays.proj4.util.ServletUtility;

/**
 * FrontController acts as a main filter for the application.
 * <p>
 * It performs:
 * <ul>
 *   <li>Session validation</li>
 *   <li>Request logging</li>
 *   <li>Prevents unauthorized access</li>
 * </ul>
 * </p>
 * 
 * Only authenticated users are allowed to access controllers and documents.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
@WebFilter(urlPatterns = { "/doc/*", "/ctl/*" })
public class FrontController implements Filter {

    /** Logger instance */
    private static Logger log = Logger.getLogger(FrontController.class);

    /**
     * Initializes the filter.
     *
     * @param conf FilterConfig object
     * @throws ServletException if initialization fails
     */
    @Override
    public void init(FilterConfig conf) throws ServletException {
        log.info("FrontController initialized");
    }

    /**
     * Performs session validation before allowing request to proceed.
     * <p>
     * If session is invalid, user is redirected to login page.
     * </p>
     *
     * @param req   ServletRequest
     * @param resp  ServletResponse
     * @param chain FilterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();

        request.setAttribute("uri", uri);
        log.debug("Requested URI : " + uri);

        // Check user session
        if (session == null || session.getAttribute("user") == null) {

            log.warn("Unauthorized access attempt for URI : " + uri);

            request.setAttribute("error",
                    "Your session has expired. Please login again!");

            ServletUtility.forward(ORSView.LOGIN_VIEW, request, response);
            return;
        }

        log.debug("Valid session found, proceeding with request");
        chain.doFilter(req, resp);
    }

    /**
     * Destroys the filter instance.
     */
    @Override
    public void destroy() {
        log.info("FrontController destroyed");
    }
}
