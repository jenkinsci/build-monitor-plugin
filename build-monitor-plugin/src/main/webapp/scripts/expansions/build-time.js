'use strict';

angular.

module('build-monitor.expansions.build-time', []).

directive('buildTime', [function () {
    return {
        restrict: 'E',
        replace:  true,
        scope: {
            project: '='
        },
        template: [
            '<div>',
                '<div data-ng-show="!!project.estimatedDuration && project.progress > 0" class="build-time">',
                    '<span class="elapsed" title="Elapsed time">{{project.lastBuildDuration}}</span>',
                    ' | ',
                    '<span class="estimated" title="Typical build time">{{project.estimatedDuration}}</span>',
                '</div>',

                '<div data-ng-show="!!project.timeElapsedSinceLastBuild && project.progress == 0" class="build-time">',
                    '<span class="estimated" title="Last execution">{{project.timeElapsedSinceLastBuild | estimatedTimeElapsedSince}}</span>',
                '</div>',
            '</div>',
        ].join('\n')
    }
}]);