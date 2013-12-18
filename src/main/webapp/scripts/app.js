'use strict';

angular.
    module('buildMonitor', [ 'buildMonitor.controllers', 'buildMonitor.settings' ]).
    run(['$rootScope', 'notifyUser', function($rootScope, notifyUser) {
        $rootScope.settings = { };

        if (! Modernizr.flexbox) {
            notifyUser.aboutInsufficientSupportOfCSS3('flexbox');
        }
    }]);