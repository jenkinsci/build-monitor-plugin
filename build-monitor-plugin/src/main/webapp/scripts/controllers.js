'use strict';

angular.
    module('buildMonitor.controllers', [ 'buildMonitor.services', 'buildMonitor.cron', 'uiSlider', 'jenkins', 'buildMonitor.stats']).

    controller('JobViews', ['$scope', '$rootScope', '$window', 'proxy', 'every', 'connectivityStrategist',
        function ($scope, $rootScope, $window, proxy, every, connectivityStrategist) {
            var tryToRecover  = connectivityStrategist.decideOnStrategy,
                fetchJobViews = proxy.buildMonitor.fetchJobViews;

            $scope.jobs         = [];
            $scope.fontSize     = fontSizeFor($scope.jobs, $rootScope.settings.numberOfColumns);

            every(5000, function () {

                return fetchJobViews().then(function (response) {

                    $scope.jobs = response.data.data;

                    $rootScope.$broadcast('jenkins:data-fetched', response.data.meta);

                    $scope.fontSize = fontSizeFor($scope.jobs, $rootScope.settings.numberOfColumns);

                }, tryToRecover());
            });

            // todo: extract the below as a configuration service, don't rely on $rootScope.settings and make the dependency explicit
            $rootScope.$watch('settings.numberOfColumns', function(newColumnCount) {
                $scope.fontSize = fontSizeFor($scope.jobs, newColumnCount);
            });

            // todo: extract into a 'widget' directive; this shouldn't be a responsibility of a controller to calculate the size of the font...
            function fontSizeFor(itemsOnScreen, numberOfColumns) {
                var baseFontSizePercentage  = 5,
                    itemsCount    = itemsOnScreen && itemsOnScreen.size() || 1,
                    actualColumns = Math.min(itemsCount, numberOfColumns),
                    actualRows    = Math.ceil(itemsCount / actualColumns);

                return (baseFontSizePercentage / Math.max(actualRows, actualColumns));
            }
        }]).

    service('connectivityStrategist', ['$rootScope', '$q', 'counter',
        function ($rootScope, $q, counter) {

            var lostConnectionsCount = counter.create();

            this.resetConnectionErrorsCounter = function () {
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

                function handleMisconfiguredProxy(error) {
                    lostConnectionsCount.increase();

                    $rootScope.$broadcast("jenkins:proxy-issue", error);

                    return $q.when(error);
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
                        case 504: return handleMisconfiguredProxy(error);
                        default:  return handleUnknown(error);
                    }
                }
            }
        }]).

    // because IE11 and Edge browsers don't support vmax and vmin ... http://caniuse.com/#search=vmax
    directive('viewportUnits', function ($window) {
        return function (scope, element) {
            var w = angular.element($window);
            scope.getWindowDimensions = function () {
                return {
                    'h': $window.innerHeight,
                    'w': $window.innerWidth
                };
            };

            scope.$watch(scope.getWindowDimensions, function (size, oldSize) {
                scope.vmax = size.h > size.w ? 'vh' : 'vw';
                scope.vmin = size.h < size.w ? 'vh' : 'vw';
            }, true);

            w.bind('resize', function () {
                scope.$apply();
            });
        };
    }).

    directive('notifier', ['$timeout', function ($timeout) {
        return {
            restrict: 'E',
            controller: function ($scope) {
                $scope.message = '';

                $scope.$on('jenkins:connection-lost', function () {
                    $scope.message = 'Communication with Jenkins mother ship is lost. Trying to reconnect...';
                });

                $scope.$on('jenkins:proxy-issue', function () {
                    $scope.message = 'Your impatient proxy timed out with a "504" before Jenkins managed to respond. Let\'s try again...';
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

    run(['$rootScope', '$window', 'notifyUser', 'connectivityStrategist', 'every', 'townCrier', 'stats',
        function ($rootScope, $window, notifyUser, connectivityStrategist, every, townCrier, stats) {
            $rootScope.$on('jenkins:data-fetched', function (event) {
                connectivityStrategist.resetConnectionErrorsCounter();
            });

            $rootScope.$on('jenkins:data-fetched', function (event, meta) {
                stats.timer('Build Monitor API', 'fetchJobs', meta.response_time_ms);
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