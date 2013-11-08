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
}]);