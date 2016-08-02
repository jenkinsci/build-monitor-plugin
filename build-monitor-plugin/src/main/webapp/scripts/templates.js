'use strict';

angular.module("buildMonitor.templates", []).run(["$templateCache", function($templateCache) {
    $templateCache.put("template/dialog/problem-with-jenkins.htm",
        "<div class=\"modal-header\">\n" +
        "	<h1>{{ title }}</h1>\n" +
        "</div>\n" +
        "<div class=\"modal-body\">\n" +
        "	<p>{{ message }}</p>\n" +
        "</div>\n" +
        "<div class=\"modal-footer\">\n" +
        "	<button ng-repeat=\"btn in buttons\" ng-click=\"close(btn.result)\" class=btn ng-class=\"btn.cssClass\">{{ btn.namespace }}</button>\n" +
        "</div>\n" +
        "");

    $templateCache.put("template/dialog/insufficient-css3-support.html",
        "<div class=\"modal-header\">\n" +
        "	<h1>It seems like your browser is having problems with CSS3</h1>\n" +
        "</div>\n" +
        "<div class=\"modal-body\">\n" +
        "	<p>" +
        "     Your browser does not seem to understand <i>CSS3 {{ feature }}</i>.<br />" +
        "     Please <a href=\"http://caniuse.com/{{ feature }}\" target=\"_blank\">click here</a> " +
        "     to see which modern browsers support this feature and consider upgrading." +
        "   </p>\n" +
        "</div>\n" +
        "");

    $templateCache.put("template/dialog/internal-jenkins-error.html",
        "<div class=\"modal-header\">\n" +
        "	<h1>Sorry to bother you, but Jenkins is having a problem :-(</h1>\n" +
        "</div>\n" +
        "<div class=\"modal-body\">\n" +
        "   <p>Instead of the expected response, I received the following, which usually means an internal Jenkins error:</p>" +
        "	<textarea rows='5'>{{ error }}</textarea>\n" +
        "   <div ng-show='{{ stackTrace.length }}'>\n" +
        "       <p>This translates to the following stack trace:</p>\n" +
        "       <textarea rows='5'>{{ stackTrace }}</textarea>\n" +
        "   </div>\n" +
        "   <p>Before <a href='https://issues.jenkins-ci.org/secure/Dashboard.jspa'>reporting a bug in Jenkins</a> it's usually worth trying to troubleshoot it using the information from your <a href='https://wiki.jenkins-ci.org/display/JENKINS/Logging'>Jenkins Error Log</a>.</p>\n" +
        "</div>\n" +
        "");
}]);