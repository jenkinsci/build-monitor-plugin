'use strict';

angular.
    module('buildMonitor.controllers', [ 'buildMonitor.services', 'uiSlider']).

    controller('JobViews', function($scope, $dialog, $timeout, fetch, storage) {
        $scope.fontSize        = storage.retrieve('fontSize', 1);
        $scope.numberOfColumns = storage.retrieve('numberOfColumns', 2);

        $scope.$watch('fontSize', function(currentFontSize) {
            storage.persist('fontSize', currentFontSize);
        });
        $scope.$watch('numberOfColumns', function(currentNumberOfColumns) {
            storage.persist('numberOfColumns', currentNumberOfColumns);
        });


        $scope.jobs = {};
        var update = function() {
            var updating;
            fetch.current().then(function(current) {
                $scope.jobs = current.jobs;
                updating = $timeout(update, 5000)
            }, function(error) {
                $timeout.cancel(updating);
                $rootScope.$broadcast("communication-error", error);
            });
        }

        update();
    });