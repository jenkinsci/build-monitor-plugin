'use strict';

angular.
    module('buildMonitor', [
        'buildMonitor.controllers',
        'buildMonitor.filters',
        'buildMonitor.settings',
        'slugifier',
        'build-monitor.slots',
        'build-monitor.expansions.build-number', //todo: this should be discovered rather than defined explicitly
        'build-monitor.expansions.build-time'    //todo: this should be discovered rather than defined explicitly
    ]).
    run(['$rootScope', 'notifyUser', function($rootScope, notifyUser) {
        $rootScope.settings = { };

        if (! Modernizr.flexbox) {
            notifyUser.aboutInsufficientSupportOfCSS3('flexbox');
        }
    }]);