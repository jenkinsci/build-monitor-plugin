package com.smartcodeltd.jenkinsci.plugin.assetbundler.filters;

import com.github.sommeri.less4j.Less4jException;
import com.github.sommeri.less4j.LessCompiler;
import com.github.sommeri.less4j.core.DefaultLessCompiler;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;

import javax.servlet.Filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static java.lang.String.format;

public class LessCSS implements Filter {
    private final File   lessFile;
    private final String pathToCSS;
    private final StaticJenkinsAPIs jenkins;

    private String compiledCSS;

    public LessCSS(String pathToCSS, File pathToLess, StaticJenkinsAPIs jenkins) throws URISyntaxException {
        this.pathToCSS  = pathToCSS;
        this.lessFile   = pathToLess;
        this.jenkins    = jenkins;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest) request).getPathInfo();
        if (path == null) {
            path = ((HttpServletRequest) request).getServletPath();
        }

        if (path != null && path.matches(pathToCSS)) {
            sendCSS(response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void sendCSS(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String css = compiledAndCachedIfNeeded(lessFile);

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.setContentType("text/css;charset=UTF-8");
        httpResponse.setContentLength(css.length());
        httpResponse.getWriter().write(css);
    }

    // todo: extract the caching functionality into a separate class
    private String compiledAndCachedIfNeeded(File less) {
        if (jenkins.isDevelopmentMode() || compiledCSS == null) {
            compiledCSS = cssFrom(less);
        }

        return compiledCSS;
    }

    private String cssFrom(File less) {
        LessCompiler compiler             = new DefaultLessCompiler();
        LessCompiler.Configuration config = new LessCompiler.Configuration();

        config.setCompressing(false);
        config.getSourceMapConfiguration().setLinkSourceMap(false);

        try {
            return compiler.compile(less, config).getCss();
        } catch (Less4jException e) {
            return format("/* Less compilation failed with: %s */", e.getMessage());
        }
    }

    @Override
    public void destroy() {}
}