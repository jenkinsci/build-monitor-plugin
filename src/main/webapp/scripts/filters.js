'use strict';

angular.
    module('buildMonitor.filters', []).

    filter('estimatedTimeElapsedSince', function() {
        var seconds = 1000,
            minutes = 60 * seconds,
            hours   = 60 * minutes,
            days    = 24 * hours,
            months  = 30 * days,
            years   = 12 * months,

            unitsOfTime = [
                { divisor: seconds,           singular: "%d second ago",   plural: "%d seconds ago" },
                { divisor: minutes / seconds, singular: "%d minute ago",   plural: "%d minutes ago" },
                { divisor: hours   / minutes, singular: "%d hour ago",     plural: "%d hours ago" },
                { divisor: days    / hours,   singular: "%d day ago",      plural: "%d days ago" },
                { divisor: months  / days,    singular: "%d month ago",    plural: "%d months ago" },
                { divisor: years   / months,  singular: "over a year ago", plural: "hasn't run in ages!" }
            ];

        function humanFriendly(remainder, unitOfTime) {
            var rounded = Math.round(remainder);

            return (rounded === 1 ? unitOfTime.singular : unitOfTime.plural).replace("%d", rounded);
        }

        function approximate(remainder, unitOfTime, tail) {
            return (tail.length === 0 || remainder < tail[0].divisor) ?
                humanFriendly(remainder, unitOfTime) :
                approximate(remainder / tail[0].divisor, tail[0], tail.slice(1));
        }

        return function(ago) {
            switch(true) {
                case (ago <= 30 * seconds): return "just now :-)";
                case (ago <= 5  * minutes): return "a moment ago";
                default: return approximate(ago / unitsOfTime[0].divisor, unitsOfTime[0], unitsOfTime.slice(1));
            }
        }
    });