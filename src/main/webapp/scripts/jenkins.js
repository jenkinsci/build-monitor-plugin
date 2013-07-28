'use strict';

angular.module('jenkins', []).

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

    factory('proxyFrom', ['$http', 'stringified', function($http, stringified) {
        return function(binding) {
            var url = binding.url + '/',
                proxy = {};

            angular.forEach(binding.methods, function(method) {
                proxy[method] = function() {
                    var parameters = Array.prototype.slice.apply(arguments);

                    return $http({
                        url:     url + method,
                        method:  'POST',
                        data:    stringified(parameters),
                        headers: {
                            'Content-Type': 'application/x-stapler-method-invocation;charset=UTF-8',
                            'Crumb': binding.crumb
                        }
                    });
                }
            });

            return proxy;
        }
    }]);