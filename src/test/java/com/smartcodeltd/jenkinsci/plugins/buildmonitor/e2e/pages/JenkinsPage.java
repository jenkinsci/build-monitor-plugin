package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class JenkinsPage<T extends JenkinsPage<T>> {
    private static final Logger log = LoggerFactory.getLogger(JenkinsPage.class);
    protected final String pageUrl;
    protected final Page page;

    protected JenkinsPage(Page page, String pageUrl) {
        this.page = page;
        this.pageUrl = pageUrl;
    }

    @SuppressWarnings("unchecked")
    T waitForLoaded() {
        isAtUrl(pageUrl);
        return (T) this;
    }

    public T goTo() {
        log.info("Navigating to {}", pageUrl);
        page.navigate(pageUrl);
        return this.waitForLoaded();
    }

    void isAtUrl(String url) {
        log.info("Waiting for url to be {}", url);
        try {
            page.waitForURL(url);
        } catch (TimeoutError e) {
            throw new TimeoutError("Timeout exceeding waiting for the url to be " + url, e);
        }
    }
}
