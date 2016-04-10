angular.
    module('buildMonitor.cron', []).

    provider('every', function () {
        this.$get = ['$rootScope', '$window', '$q', '$timeout',
            function ($rootScope, $window, $q, $timeout) {

                function every(interval, command) {
                    var isDefined  = angular.isDefined,
                        isFunction = angular.isFunction,
                        isDeferred = function (result) {
                            return (isDefined(result) && isFunction(result.then));
                        },
                        applyRootScope = function () {
                            $rootScope.$$phase || $rootScope.$apply();
                        };

                    function step() {
                        var result = command();

                        if (isDeferred(result)) {
                            result.then(function () {
                                $timeout(step, interval);
                            })
                        } else {
                            $timeout(step, interval);
                        }

                        applyRootScope();
                    }

                    step();
                }

                return every;
            }];
    });