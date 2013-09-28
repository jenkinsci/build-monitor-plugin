'use strict';

angular.
    module('buildMonitor.services', ['ui.bootstrap.dialog', 'template/dialog/message.html', 'ngCookies']).

    service('notifyUser',function ($dialog, $window) {
        this.about = function (problemStatus) {

            var title = "Sorry to bother you, but there is a slight issue ..."
            var message = "Jenkins returned a \"" + problemStatus + "\" response " +
                "causing the last request to fail. " +
                "This usually means a network connection problem " +
                "or that Jenkins is being restarted. " +
                "Don't worry, reloading the page should help :-)";

            $dialog.messageBox(title, message, [
                    { result: "reload", label: "Reload the page"}
                ]).open().then(function (result) {
                    $window.location.reload();
                });
        }
    }).

    service('storage',function ($cookies, buildMonitorName, hashCodeOf) {
        this.persist = function (name, value) {
            var nextYear = new Date();
            nextYear.setYear(now.getFullYear()+1);
            YAHOO.util.Cookie.set(prefix(name), value, {
                expires: nextYear
            });
        }

        this.retrieve = function (name, defaultValue) {
            var value = $cookies[prefix(name)];

            return (typeof value !== 'undefined')
                ? value
                : defaultValue;
        }

        function prefix(name) {
            return 'buildMonitor.' + hashCodeOf(buildMonitorName) + '.' + name;
        }
    }).

    factory('hashCodeOf', function() {
        return function(name) {
            var name = name || '',
                hash = 0,
                char;

            if (name.length == 0) {
                return hash;
            }

            for (var i = 0; i < name.length; i++) {
                char = name.charCodeAt(i);
                hash = ((hash << 5) - hash) + char;
                hash = hash & hash; // Convert to 32bit integer
            }

            return hash;
        }
    });
