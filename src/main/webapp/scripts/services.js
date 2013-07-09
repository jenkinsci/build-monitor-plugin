'use strict';

angular.module('buildMonitor.services', ['ui.bootstrap.dialog', 'template/dialog/message.html', 'ngCookies']).

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

    service('fetch',function ($q, $rootScope, jenkinsProxy) {
        this.current = function () {
            var deferred = $q.defer();

            jenkinsProxy.fetchJobViews(function (response) {

                if (response.status === 200) {
                    deferred.resolve(response.responseObject());
                } else {
                    // todo remove the below debug once proper error handling has been implemented
                    console.error('RESPONSE RECEIVED', response.status);
                    deferred.reject({ status: response.status });
                }

                $rootScope.$apply();
            });

            return deferred.promise;
        }
    }).

    service('storage',function ($cookies, buildMonitorName, hashCode) {
        this.persist = function (name, value) {
            $cookies[prefix(name)] = value;
        }

        this.retrieve = function (name, defaultValue) {
            var value = $cookies[prefix(name)];

            return (typeof value !== 'undefined')
                ? value
                : defaultValue;
        }

        function prefix(name) {
            return 'buildMonitor.' + hashCode.of(buildMonitorName) + '.' + name;
        }
    }).

    service('hashCode', function() {
        this.of = function(name) {
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
    })
;