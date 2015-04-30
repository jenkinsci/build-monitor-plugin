'use strict';

angular.
    module('buildMonitor.services', ['ui.bootstrap.dialog', 'buildMonitor.templates', 'template/dialog/message.html']).

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
        }
    }).

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

        var value = 0;

        return {
            reset:    function() { value = 0;    },
            increase: function() { ++value;      },
            value:    function() { return value; }
        }
    }]);
