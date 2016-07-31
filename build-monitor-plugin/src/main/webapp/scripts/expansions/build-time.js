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
                    '<span class="elapsed" title="Elapsed time">{{project.lastBuild.duration}}</span>',
                    ' | ',
                    '<span class="estimated" title="Typical build time">{{project.estimatedDuration}}</span>',
                '</div>',

                '<div data-ng-show="!!project.lastBuild.timeElapsedSince && project.progress == 0" class="build-time">',
                    '<span class="estimated" title="Last execution">{{project.lastBuild.timeElapsedSince | estimatedTimeElapsedSince}}</span>',
                '</div>',
            '</div>',
        ].join('\n')
    }
}]);