'use strict';

angular.
    module('buildMonitor.controllers', [ 'buildMonitor.services', 'buildMonitor.cron', 'uiSlider', 'jenkins']).

    controller('JobViews', ['$scope', '$rootScope', 'proxy', 'every', 'connectivityStrategist',
        function ($scope, $rootScope, proxy, every, connectivityStrategist) {
            var tryToRecover  = connectivityStrategist.decideOnStrategy,
                fetchJobViews = proxy.buildMonitor.fetchJobViews;

            $scope.jobs = {};

            every(5000, function () {

                return fetchJobViews().then(function (response) {

                    $scope.jobs = response.data.jobs;

                    $rootScope.$broadcast('jenkins:data-fetched', {});

                }, tryToRecover());
            });
        }]).

    service('connectivityStrategist', ['$rootScope', '$q', 'counter',
        function ($rootScope, $q, counter) {

            var lostConnectionsCount = counter;

            this.resetErrorCounter = function () {
                if (lostConnectionsCount.value() > 0) {
                    $rootScope.$broadcast("jenkins:connection-reestablished", {});
                }

                lostConnectionsCount.reset();
            }

            this.decideOnStrategy = function () {

                function handleLostConnection(error) {
                    lostConnectionsCount.increase();

                    $rootScope.$broadcast("jenkins:connection-lost", error);
                }

                function handleJenkinsRestart(error) {
                    $rootScope.$broadcast("jenkins:restarted", error);
                }

                return function (error) {
                    switch (error.status) {
                        case 404: return handleJenkinsRestart(error);
                        default:  return handleLostConnection(error);
                    }
                }
            }
        }]).

    directive('notifier', ['$timeout', function ($timeout) {
        return {
            restrict: 'E',
            controller: function ($scope) {
                $scope.message = '';

                $scope.$on('jenkins:connection-lost', function (event, error) {
                    if (error.status == 0) {
                        $scope.message = 'Communication with Jenkins mother ship is lost. Trying to reconnect...';
                    } else {
                        $scope.message = 'Jenkins returned HTTP status '+error.status+'. Retrying...';
                    }
                });

                $scope.$on('jenkins:connection-reestablished', function () {
                    $scope.message = "... and we're back online, yay! :-)";

                    $timeout(function () {
                        $scope.message = "";
                    }, 3000);
                });
            },
            replace: true,
            template: '<span class="notifier" ' +
                'ng-show="message"' +
                'ng-animate="\'fade\'">' +
                    '{{ message }}' +
                "</span>\n"
        }
    }]).

    run(['$rootScope', '$window', '$log', 'notifyUser', 'connectivityStrategist',
        function ($rootScope, $window, $log, notifyUser, connectivityStrategist) {
            $rootScope.$on('jenkins:data-fetched', function (event) {
                connectivityStrategist.resetErrorCounter();
            });
            $rootScope.$on('jenkins:restarted', function (event, error) {
                $window.location.reload();
            });
        }]);
