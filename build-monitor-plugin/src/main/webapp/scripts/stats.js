angular

.module('buildMonitor.stats', ['ngLodash', 'buildMonitor.cron'])

.service('googleAnalyticsBackend', ['lodash', '$window', function(_, $window) {

    // public

    this.flush = function(metrics) {
        _.forEach(timerExtractedFrom(metrics), sendTimer);
    };

    // private

    function analyticsScriptLoaded() {
        return !! $window.ga && !! $window.ga.create;
    }

    // this function sends the performance indicators to google analytics only when:
    // - you agreed to participate in making Build Monitor better by allowing for the anonymous stats to be collected
    //   see https://github.com/jan-molak/jenkins-build-monitor-plugin/wiki/Privacy-Policy
    // - your machine is connected to the Internet
    function sendTimer (timer) {
        if (analyticsScriptLoaded()) {
            $window.ga('send', 'timing', timer.category, timer.variable, timer.value);
        }
    }

    function timerExtractedFrom (metrics) {
        return _.flattenDeep(_.reduce(metrics, toTimers, []))
    }

    function toTimers (acc, metrics, category) {

        acc.push(_.reduce(metrics, toTimersPer(category), []) );

        return acc;
    }

    function toTimersPer(category) {

        return function (acc_timers, values, timer_name) {

            acc_timers.push(_.map(values, function (value, value_name) {
                return { category: category, variable: timer_name + ' ' + value_name, value: value };
            }));

            return acc_timers;
        };
    }
}])

.provider('stats', ['lodash', function (_) {

    var defaults = { flushInterval: 10 },
        config   = defaults;

    // public

    this.configure = function(_config_) {
        config = angular.extend(defaults, _config_);
    };

    this.$get = ['every', '$window', 'googleAnalyticsBackend', function (every, $window, googleAnalyticsBackend) {
        return new Stats(config, every, $window, googleAnalyticsBackend);
    }];

    // --

    function Stats(configured, every, $window, backend) {
        var categorised_timers = {};

        // public

        function recordTimer (category_name, parameter_name, value_in_ms) {
            if (! categorised_timers[category_name]) {
                categorised_timers[category_name] = {};
            }

            if (! categorised_timers[category_name][parameter_name]) {
                categorised_timers[category_name][parameter_name] = [];
            }

            categorised_timers[category_name][parameter_name].push(Number(value_in_ms));
        }

        // --

        function whenUserLeavesTheApplication (handler) {
            var existingHandler    = $window.onbeforeunload;

            $window.onbeforeunload = function (event) {
                if (existingHandler) existingHandler(event);

                handler(event);

                return undefined;
            };
        }


        function flush () {

            if (! angular.equals(categorised_timers, {})) {
                backend.flush(processed(categorised_timers));

                // reset the timers
                categorised_timers = {};
            }
        }

        function processed (categorised_timers) {

            function processEachTimer (acc, values, timer_name) {
                var sorted = values.sort(function (a,b) { return a - b; }),
                    count  = sorted.length;

                acc[timer_name] = {
                    lower: sorted[0],
                    upper: sorted[count - 1],
                    mean:  Math.round(_.reduce(sorted, function(total, current) { return total + current }) / count)
                };

                return acc;
            }

            function processEachCategory (acc, timers, category_name) {
                acc[category_name] = _.reduce(timers, processEachTimer, {});

                return acc;
            }

            return _.reduce(categorised_timers, processEachCategory, {});
        }

        every(configured.flushIntervalInSeconds * 1000, flush);

        whenUserLeavesTheApplication(flush);

        this.timer = recordTimer;
    }
}]);