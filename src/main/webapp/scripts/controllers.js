'use strict';

angular.
    module('buildMonitor.controllers', [ 'buildMonitor.services', 'uiSlider']).

    controller('JobViews', function($scope, $rootScope, $dialog, $timeout, proxy, cookieJar) {
        $scope.fontSize        = cookieJar.get('fontSize', 1);
        $scope.numberOfColumns = cookieJar.get('numberOfColumns', 2);

        $scope.$watch('fontSize', function(currentFontSize) {
            cookieJar.put('fontSize', currentFontSize);
        });
        $scope.$watch('numberOfColumns', function(currentNumberOfColumns) {
            cookieJar.put('numberOfColumns', currentNumberOfColumns);
        });


        $scope.jobs = {};
        var update = function() {
            var updating;
            proxy.buildMonitor.fetchJobViews().then(function(response) {
                $scope.jobs = response.data.jobs;
                updating = $timeout(update, 5000);
            }, function(error) {
                $timeout.cancel(updating);
                $rootScope.$broadcast("communication-error", error);
            });
        }

        update();
    });