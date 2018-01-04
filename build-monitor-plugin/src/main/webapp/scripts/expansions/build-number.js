'use strict';

angular.

module('build-monitor.expansions.build-number', []).

// todo fixme: lastBuildUrl: "job/demo%20app//20/", double slash?
directive('buildNumber', [function () {
    return {
        restrict: 'E',
        replace:  true,
        scope: {
            project: '='
        },
        template: [
            '<div>',
                '<div data-ng-show="!!project.estimatedDuration && project.progress > 0" data-ng-repeat="build in project.currentBuilds track by $index">',
                    '<a',
                        'data-ng-show="!!build.name"',
                        'class="build-name"',
                        'title="Details of {{project.name}}, build {{build.name}}"',
                        'href="{{build.url}}"',
                    '>',
                        '<span>',
                            '{{build.name}}',
                        '</span>',
                    '</a>',
                    '<span data-ng-if="build.pipelineStages" class="build-stages">',
                        '{{build.pipelineStages}}',
                    '</span>',
                '</div>',
                '<div data-ng-show="!!project.lastCompletedBuild.timeElapsedSince && project.progress == 0">',
                    '<a',
                        'data-ng-show="!!project.lastCompletedBuild.name"',
                        'class="build-name"',
                        'title="Details of {{project.name}}, build {{project.lastCompletedBuild.name}}"',
                        'href="{{project.lastCompletedBuild.url}}"',
                    '>',
                        '<span>',
                            '{{project.lastCompletedBuild.name}}',
                        '</span>',
                    '</a>',
                '</div>',
            '</div>\n'
        ].join('\n')
    }
}]);