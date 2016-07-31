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
            '<a',
                'data-ng-show="!!project.lastBuild.name"',
                'class="build-name"',
                'title="Details of {{project.name}}, build {{project.lastBuild.name}}"',
                'href="{{project.lastBuild.url}}"',
            '>',
                '<span>',
                    '{{project.lastBuild.name}}',
                '</span>',
            '</a>\n'
        ].join('\n')
    }
}]);