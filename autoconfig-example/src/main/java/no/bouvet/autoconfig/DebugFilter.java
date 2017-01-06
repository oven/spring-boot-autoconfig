package no.bouvet.autoconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

public class DebugFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(Filter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        log.info("starting request: ");
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("finished request: " + (System.currentTimeMillis() - start) + " ms.");
    }

    @Override
    public void destroy() {

    }
}
