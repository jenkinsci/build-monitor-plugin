'use strict';

angular.

module('build-monitor.slots', []).

directive('slot', ['$compile', function ($compile) {
    function link (scope, element, attributes) {
        var expansion = compile(scope.expansion)(scope);

        element.append(expansion);
    }

    function compile(expansion_name) {
        var expansion_directive = angular.element(document.createElement(expansion_name));

        expansion_directive.attr('project', 'project');

        return $compile(expansion_directive);
    }

    return {
        restrict: 'E',
        replace:  true,
        link:     link,
        scope:    {
            name:      '@',
            expansion: '@',
            project:   '='
        },
        template: '<div class="{{ expansion }} slot slot-{{ name }}"></div>'
    }
}]);