'use strict';

angular.
    module('buildMonitor.services', ['ui.bootstrap.dialog', 'buildMonitor.templates', 'buildMonitor.cron', 'template/dialog/message.html']).

    value('YahooCookie', YAHOO.util.Cookie).

    service('notifyUser',function ($dialog, $window) {
        this.aboutInsufficientSupportOfCSS3 = function(feature) {
            $dialog.dialog({
                templateUrl: 'template/dialog/insufficient-css3-support.html',
                controller: function($scope, dialog, model) {
                    $scope.feature = model.feature;
                },
                resolve: { model: function() {
                    return { feature: feature };
                }},
                keyboard: false,
                backdrop: false
            }).open().then();
        };

        this.aboutInternalJenkins = function(error) {
            function stackTraceOf(error) {
                var stackTrace = "";

                if (! (error.data && angular.isArray(error.data.stackTrace))) {
                    return stackTrace;
                }

                angular.forEach(error.data.stackTrace, function(entry) {
                    stackTrace += "at " + entry.className + "(" + entry.fileName + ":" + entry.lineNumber + ")\n";
                });

                return stackTrace;
            }

            $dialog.dialog({
                templateUrl: 'template/dialog/internal-jenkins-error.html',
                controller: function($scope, dialog, model) {
                    $scope.error      = model.error;
                    $scope.stackTrace = model.stackTrace;
                },
                resolve: { model: function() {
                    return {
                        error: JSON.stringify(error.data, undefined, 2),
                        stackTrace: stackTraceOf(error)
                    };
                }},
                keyboard: false,
                backdrop: true,
                backdropClick: false
            }).open().then();
        };

        this.about = function (problemStatus) {

            var title = "Sorry to bother you, but there is a slight issue ...";
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

    service('townCrier', ['$http', 'every', 'version', 'BUILD_MONITOR_VERSION',
        function($http, every, version, BUILD_MONITOR_VERSION) {

        var hour            = 3600 * 1000,
            currentVersion  = version(BUILD_MONITOR_VERSION),
            observers       = [];

        this.uponNewVersion = function (observer) {
            observers.push(observer);
        };

        this.start    = function() {
            every(hour, function() {
                return $http
                    .jsonp('https://api.github.com/repos/jan-molak/jenkins-build-monitor-plugin/releases/latest?callback=JSON_CALLBACK')
                    .then(function(response) {
                        var latestRelease = response.data.data.tag_name;

                        if (currentVersion.isOlderThan(latestRelease)) {
                            angular.forEach(observers, function(observer) {
                                observer();
                            });
                        }
                    });
            });
        };
    }]).

    factory('version', [function() {
        function build(version) {
            var build_number = (version.match(/([0-9]+)$/) || []).pop();

            // assume 0 if we couldn't determine the build number
            return parseInt(build_number || 0);
        }

        return function (version) {
            return {
                isOlderThan: function (anotherVersion) {
                    return build(version) < build(anotherVersion);
                }
            }
        };
    }]).

    provider('cookieJar',function () {
        var defaultAttributes = {
                label: '',
                shelfLife: 0
            },
            attributes = {};

        this.describe = function (cookieJarAttributes) {
            attributes = cookieJarAttributes;
        }

        this.$get = ['YahooCookie', function (YahooCookie) {
            return new CookieJar(YahooCookie, angular.extend(defaultAttributes, attributes));
        }];


        function CookieJar(YahooCookie, attributes) {

            function expiryDetailsBasedOn(days) {
                if (days <= 0) {
                    return {};
                }

                return {
                    expires: new Date(+new Date() + (days * 1000 * 3600 * 24))
                }
            }

            function prefixed(name) {
                return attributes.label
                    ? attributes.label + '.' + name
                    : name;
            }

            return {
                put: function (name, value) {
                    YahooCookie.set(prefixed(name), value, expiryDetailsBasedOn(attributes.shelfLife));
                },
                get: function (name, defaultValue) {
                    var value = YahooCookie.get(prefixed(name));

                    return (value !== null)
                        ? value
                        : defaultValue;
                }
            }
        }
    }).

    provider('hashCode', function () {
        this.hashCodeOf = hashCodeOf;

        this.$get = function() {
            return hashCodeOf
        }

        function hashCodeOf(name) {
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
    }).


    factory('counter', [function() {

        function Counter() {
            this._value = 0;
        }

        Counter.prototype.reset    = function() { this._value = 0;    }
        Counter.prototype.increase = function() { ++this._value;      }
        Counter.prototype.value    = function() { return this._value; }
    
        return {
            create: function() {
                return new Counter();
            }
        }
    }]);