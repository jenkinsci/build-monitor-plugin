'use strict';

angular.
    module('buildMonitor', [ 'buildMonitor.controllers', 'uiSlider', 'jenkins' ]).
    run(function($rootScope, notifyUser) {
        if (! Modernizr.flexbox) {
            notifyUser.aboutInsufficientSupportOfCSS3('flexbox');
        }

        $rootScope.$on('communication-error', function(event, error) {
            notifyUser.about(error.status);
        });
    });