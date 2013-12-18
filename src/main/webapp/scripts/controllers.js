'use strict';

angular.
    module('buildMonitor.controllers', [ 'buildMonitor.services', 'buildMonitor.cron', 'uiSlider']).

    controller('JobViews', ['$scope', '$rootScope', 'proxy', 'cookieJar', 'every', 'connectionErrorHandler',
        function ($scope, $rootScope, proxy, cookieJar, every, connectionErrorHandler) {

            // todo: consider extracting a Configuration Controller
            $scope.fontSize = cookieJar.get('fontSize', 1);
            $scope.numberOfColumns = cookieJar.get('numberOfColumns', 2);

            $scope.$watch('fontSize', function (currentFontSize) {
                cookieJar.put('fontSize', currentFontSize);
            });
            $scope.$watch('numberOfColumns', function (currentNumberOfColumns) {
                cookieJar.put('numberOfColumns', currentNumberOfColumns);
            });

            //

            var handleErrorAndDecideOnNext = connectionErrorHandler.handleErrorAndNotify,
                fetchJobViews              = proxy.buildMonitor.fetchJobViews;

            $scope.jobs = {};

            every(5000, function (step) {

                fetchJobViews().then(function (response) {

                    $scope.jobs = response.data.jobs
                    step.resolve();

                }, handleErrorAndDecideOnNext(step));
            });
        }]).

    service('connectionErrorHandler', ['$rootScope',
        function ($rootScope) {
            this.handleErrorAndNotify = function (deferred) {

                function handleLostConnection(error) {
                    deferred.resolve();
                    $rootScope.$broadcast("jenkins:connection-lost", error);
                }

                function handleJenkinsRestart(error) {
                    deferred.reject();
                    $rootScope.$broadcast("jenkins:restarted", error);
                }

                function handleUnknown(error) {
                    deferred.reject();
                    $rootScope.$broadcast("jenkins:unknown-communication-error", error);
                }

                return function (error) {
                    switch (error.status) {
                        case 0:   handleLostConnection(error); break;
                        case 404: handleJenkinsRestart(error); break;
                        default:  handleUnknown(error);        break;
                    }
                }
            }
        }]).

    run(['$rootScope', '$window', '$log', 'notifyUser',
        function ($rootScope, $window, $log, notifyUser) {
            $rootScope.$on('jenkins:connection-lost', function (event, error) {
                // todo: notify the user about the problem and what we're doing in order to resolve it
                $log.info('Connection with Jenkins mother ship is lost. I\'ll try to reconnect in a couple of seconds and see if we have more luck...');
            });

            $rootScope.$on('jenkins:restarted', function (event, error) {
                $window.location.reload();
            });

            $rootScope.$on('jenkins:unknown-communication-error', function (event, error) {
                notifyUser.about(error.status);
            });
        }]);