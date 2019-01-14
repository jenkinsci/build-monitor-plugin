angular.
    module('buildMonitor.settings', [ 'buildMonitor.services', 'rzModule']).

    controller('controlPanel', ['$scope', 'params', 'cookieJar', 'townCrier',
        function ($scope, params, cookieJar, townCrier) {
            'use strict';

            var fontSize        = (params) ? params.get('fontSize')          : cookieJar.get('fontSize',        1);
            var numberOfColumns = (params) ? params.get('numberOfColumns')   : cookieJar.get('numberOfColumns', 2);
            var colourBlind     = (params) ? params.get('colourBlind')       : cookieJar.get('colourBlind',     0);
            var reduceMotion    = (params) ? params.get('reduceMotion')      : cookieJar.get('reduceMotion',    0);
            var showBadges      = (params) ? params.get('showBadges')        : cookieJar.get('showBadges',      0);

            $scope.settings.fontSize        = fontSize;
            $scope.settings.numberOfColumns = numberOfColumns;
            $scope.settings.colourBlind     = colourBlind;
            $scope.settings.reduceMotion    = reduceMotion;
            $scope.settings.showBadges      = showBadges;

            angular.forEach($scope.settings, function(value, name) {
                $scope.$watch('settings.' + name, function(currentValue) {
                    params.set(name, currentValue);
                    cookieJar.put(name, currentValue);
                });
            });

            // that's the minimum viable product .. at its tiniest
            townCrier.uponNewVersion(function() {
                $scope.newVersionAvailable = true;
            });
        }
    ]);
