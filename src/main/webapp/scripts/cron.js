angular.
    module('buildMonitor.cron', []).

    provider('every', function () {
        this.$get = ['$rootScope', '$window', '$q', '$timeout',
            function ($rootScope, $window, $q, $timeout) {

                function every(interval, command) {
                    var applyRootScope = function() {
                            $rootScope.$$phase || $rootScope.$apply();
                        };

                    function synchronous(command) {
                        command();
                        $timeout(step, interval);
                    }

                    function asynchronous(command) {
                        var deferred = $q.defer(),
                            promise  = deferred.promise;

                        promise.then(function() {
                            $timeout(step, interval);
                        });

                        command(deferred);
                    }

                    function step() {
                        (command.length == 0)
                            ? synchronous(command)
                            : asynchronous(command);

                        applyRootScope();
                    }

                    step();
                }

                return every;
            }];
    });