'use strict';

angular.
    module('buildMonitor', [ 'buildMonitor.controllers', 'uiSlider', 'jenkins' ]).
    run(function($rootScope, notifyUser) {
        $rootScope.$on('communication-error', function(event, error) {
            notifyUser.about(error.status);
        });
    });