'use strict';

angular.module('jenkins', []).

    constant('STAPLER_CONTENT_TYPE', 'application/x-stapler-method-invocation;charset=UTF-8').

    provider('proxy', function() {
        this.bindings = {};

        this.configureProxiesUsing = function(bindings) {
            this.bindings = bindings;
        };

        this.$get = ['proxyFrom', function(proxyFrom) {
            var proxies = {};

            angular.forEach(this.bindings, function(binding, name) {
                proxies[name] = proxyFrom(binding);
            });

            return proxies;
        }];
    }).

    factory('stringified', function() {
        return function (object) {
            var stringified = angular.toJson(object);

            stringified = stringified.replace(/^"/, '');
            stringified = stringified.replace(/"$/, '');
            stringified = stringified.replace(/\\"/g, '"');

            return stringified;
        };
    }).

    factory('proxyFrom', [
        '$http', 'stringified', 'STAPLER_CONTENT_TYPE', 'CSRF_CRUMB_FIELD_NAME',
        function($http, stringified, STAPLER_CONTENT_TYPE, CSRF_CRUMB_FIELD_NAME) {

        return function(binding) {
            var url = binding.url + '/',
                proxy = {};

            angular.forEach(binding.methods, function(method) {
                proxy[method] = function() {
                    var parameters = Array.prototype.slice.apply(arguments),
                        headers    = {
                            'Content-Type': STAPLER_CONTENT_TYPE,
                            'Crumb':        binding.crumb  // Crumb header is needed to get past Stapler
                        };

                    // a '.crumb' header is needed to support CSRF protection up to Jenkins 2.0 (#46),
                    // but since Jenkins 2.0 it is called 'Jenkins-Crumb' (#215)
                    headers[CSRF_CRUMB_FIELD_NAME] = binding.crumb;

                    return $http({
                        url:     url + method,
                        method:  'POST',
                        data:    stringified(parameters),
                        headers: headers
                    });
                }
            });

            return proxy;
        }
    }]).

    factory('responseCodeStandardsIntroducer', ['$q', 'STAPLER_CONTENT_TYPE', function($q, STAPLER_CONTENT_TYPE) {
        function isAStapler(response) {
            return (response.config.headers
                && response.config.headers['Content-Type'] === STAPLER_CONTENT_TYPE);
        }

        return {
            'response': function(response) {
                if (isAStapler(response) && response.data.stackTrace) {
                    // this is required to patch the incorrect behaviour of Stapler
                    // see https://issues.jenkins-ci.org/browse/JENKINS-21132
                    var augmentedResponse = angular.copy(response);
                    augmentedResponse.status = 500;

                    return $q.reject(augmentedResponse);
                }

                return response;
            }
        };
    }]).

    config(function ($httpProvider) {
        $httpProvider.interceptors.push('responseCodeStandardsIntroducer');
    });