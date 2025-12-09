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

import in.co.rays.proj4.util.ServletUtility;

/**
 * FrontController acts as a security filter for the application.
 * 
 * It checks whether the user session is active or not before allowing
 * access to secured resources under /doc and /ctl.
 * 
 * If the session is expired, it redirects the user to the login page.
 * 
 * @author Deepak Verma
 * @version 1.0
 */
@WebFilter(urlPatterns = { "/doc/*", "/ctl/*" })
public class FrontController implements Filter {

    /**
     * Initializes the filter.
     * 
     * @param conf Filter configuration object
     * @throws ServletException in case of initialization error
     */
    @Override
    public void init(FilterConfig conf) throws ServletException {
        // Optional initialization code
    }

    /**
     * Filters requests and checks for valid user session.
     * If session is invalid, redirects to login page.
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

        HttpSession session = request.getSession();
        
        String uri = request.getRequestURI();
        request.setAttribute("uri", uri);

        // Check if user session exists
        if (session.getAttribute("user") == null) {

            request.setAttribute("error", "Your session has been expired. Please Login again!");

            ServletUtility.forward(ORSView.LOGIN_VIEW, request, response);
            return;

        } else {
            // If session exists, continue request chain
            chain.doFilter(req, resp);
        }
    }

    /**
     * Destroys the filter instance.
     */
    @Override
    public void destroy() {
        // Optional cleanup code
    }
}
