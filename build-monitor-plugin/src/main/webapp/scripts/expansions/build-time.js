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
                '<div data-ng-show="!!project.estimatedDuration && project.progress > 0" data-ng-repeat="build in project.currentBuilds track by $index" class="build-time">',
                    '<span class="elapsed" title="Elapsed time">{{build.duration}}</span>',
                    ' | ',
                    '<span class="estimated" title="Typical build time">{{project.estimatedDuration}}</span>',
                '</div>',

                '<div data-ng-show="!!project.lastCompletedBuild.timeElapsedSince && project.progress == 0" class="build-time">',
                    '<span class="estimated" title="Last execution">{{project.lastCompletedBuild.timeElapsedSince | estimatedTimeElapsedSince}}</span>',
                '</div>',
            '</div>',
        ].join('\n')
    }
}]);