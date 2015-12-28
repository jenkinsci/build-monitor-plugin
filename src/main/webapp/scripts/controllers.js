'use strict';

angular.
    module('buildMonitor.controllers', [ 'buildMonitor.services', 'buildMonitor.cron', 'uiSlider', 'jenkins']).

    controller('JobViews', ['$scope', '$rootScope', 'proxy', 'every', 'connectivityStrategist',
        function ($scope, $rootScope, proxy, every, connectivityStrategist) {
            var tryToRecover  = connectivityStrategist.decideOnStrategy,
                fetchJobViews = proxy.buildMonitor.fetchJobViews;

            $scope.jobs         = [];
            $scope.fontSize     = fontSizeFor($scope.jobs);

            every(5000, function () {

                return fetchJobViews().then(function (response) {

                    $scope.jobs = response.data.data;

                    $rootScope.$broadcast('jenkins:data-fetched', response.data.meta);

                    $scope.fontSize = fontSizeFor($scope.jobs);

                }, tryToRecover());
            });

            // todo: extract the below as a configuration service, don't rely on $rootScope.settings and make the dependency explicit
            $rootScope.$watch('settings.numberOfColumns', function() {
                $scope.fontSize = fontSizeFor($scope.jobs);
            });

            function fontSizeFor(itemsOnScreen) {
                var initialFontSize = 24,
                    minFontSize     = 3,
                    numberOfRows    = Math.ceil((itemsOnScreen && itemsOnScreen.size() || 1) / ($rootScope.settings.numberOfColumns || 1));

                return Math.max(initialFontSize / numberOfRows, minFontSize);
            }
        }]).

    service('connectivityStrategist', ['$rootScope', '$q', 'counter',
        function ($rootScope, $q, counter) {

            var lostConnectionsCount = counter;

            this.resetErrorCounter = function () {
                if (lostConnectionsCount.value() > 0) {
                    $rootScope.$broadcast("jenkins:connection-reestablished", {});
                }

                lostConnectionsCount.reset();
            };

            this.decideOnStrategy = function () {

                function handleLostConnection(error) {
                    lostConnectionsCount.increase();

                    $rootScope.$broadcast("jenkins:connection-lost", error);

                    return $q.when(error);
                }

                function handleJenkinsRestart(error) {
                    $rootScope.$broadcast("jenkins:restarted", error);
                    return $q.reject(error);
                }

                function handleInternalJenkins(error) {
                    $rootScope.$broadcast("jenkins:internal-error", error);
                    return $q.reject(error);
                }

                function handleUnknown(error) {
                    $rootScope.$broadcast("jenkins:unknown-communication-error", error);
                    return $q.reject(error);
                }

                return function (error) {
                    switch (error.status) {
                        case 0:   return handleLostConnection(error);
                        case 404: return handleJenkinsRestart(error);
                        case 500: return handleInternalJenkins(error);
                        case 502: return handleLostConnection(error);
                        case 503: return handleLostConnection(error);
                        default:  return handleUnknown(error);
                    }
                }
            }
        }]).

    directive('notifier', ['$timeout', function ($timeout) {
        return {
            restrict: 'E',
            controller: function ($scope) {
                $scope.message = '';

                $scope.$on('jenkins:connection-lost', function () {
                    $scope.message = 'Communication with Jenkins mother ship is lost. Trying to reconnect...';
                });

                $scope.$on('jenkins:connection-reestablished', function () {
                    $scope.message = "... and we're back online, yay! :-)";

                    $timeout(function () {
                        $scope.message = "";
                    }, 3000);
                });
            },
            replace: true,
            template: ['<div class="notifier"',
                'ng-show="message"',
                'ng-animate="\'fade\'">',
                    '{{ message }}',
                '</div>\n'
            ].join('\n')
        }
    }]).

    run(['$rootScope', '$window', '$log', 'notifyUser', 'connectivityStrategist', 'every', 'townCrier',
        function ($rootScope, $window, $log, notifyUser, connectivityStrategist, every, townCrier) {
            $rootScope.$on('jenkins:data-fetched', function (event) {
                connectivityStrategist.resetErrorCounter();
            });

            $rootScope.$on('jenkins:data-fetched', function (event, meta) {
                $window.ga('send', 'timing', 'Build Monitor API', 'fetchJobs',   meta.response_time_ms);
            });

            $rootScope.$on('jenkins:internal-error', function (event, error) {
                notifyUser.aboutInternalJenkins(error);
            });

            $rootScope.$on('jenkins:restarted', function (event, error) {
                $window.location.reload();
            });

            $rootScope.$on('jenkins:unknown-communication-error', function (event, error) {
                notifyUser.about(error.status);
            });

            every(25 * 60000, function () {
                $window.ga('send', 'event',  'Build Monitor UI',  'heartbeat');
            });

            townCrier.start();
        }]);