'use strict';

describe('buildMonitor', function () {
    describe('stats', function () {
        var sandbox,
            intervalInSeconds = 10,
            intervalInMillis  = millis(intervalInSeconds);

        beforeEach(function() {
            sandbox = sinon.sandbox.create();

            module('buildMonitor.stats', function(statsProvider) {
                statsProvider.configure({ flushIntervalInSeconds: intervalInSeconds });
            });
        });

        afterEach(function () { sandbox.restore(); });

        it ('does not flush the metrics if there are none recorded', inject(function($timeout, googleAnalyticsBackend) {
            googleAnalyticsBackend.flush = sandbox.spy();

            $timeout.verifyNoPendingTasks();
        }));

        it ('flushes the timers after a "flushInterval" seconds', inject(function($timeout, stats, googleAnalyticsBackend) {
            googleAnalyticsBackend.flush = sandbox.spy();

            stats.timer('category name', 'timer name', 250);

            // $timeout.flush(5 * 60 * 1000 * 1000);
            $timeout.flush();

            expect(googleAnalyticsBackend.flush).toHaveBeenCalledWith({
                'category name': {
                  'timer name': {
                      upper: 250,
                      lower: 250,
                      mean:  250
                  }
                }
            });
        }));

        it ('removes recorded metrics after the flush', inject(function($timeout, stats, googleAnalyticsBackend) {
            googleAnalyticsBackend.flush = sandbox.spy();

            stats.timer('category name', 'timer name', 250);

            $timeout.flush(intervalInMillis);
            $timeout.flush(intervalInMillis);

            expect(googleAnalyticsBackend.flush).toHaveBeenCalledOnce();
        }));

        it ('starts fresh with every interval', inject(function($timeout, stats, googleAnalyticsBackend) {
            googleAnalyticsBackend.flush = sandbox.spy();

            stats.timer('category name', 'timer name', 250);
            $timeout.flush(intervalInMillis);

            stats.timer('category name', 'timer name', 475);
            $timeout.flush(intervalInMillis);

            expect(googleAnalyticsBackend.flush).toHaveBeenCalledWith({
                'category name': {
                    'timer name': {
                        lower: 475,
                        upper: 475,
                        mean:  475
                    }
                }
            });
        }));

        it ('accumulates metrics within an interval', inject(function($timeout, stats, googleAnalyticsBackend) {
            googleAnalyticsBackend.flush = sandbox.spy();

            stats.timer('category name', 'timer name', 250);
            stats.timer('category name', 'timer name', 350);
            stats.timer('category name', 'timer name', 425);
            stats.timer('category name', 'timer name', 167);

            $timeout.flush(intervalInMillis);

            expect(googleAnalyticsBackend.flush).toHaveBeenCalledWith({
                'category name': {
                    'timer name': {
                        lower: 167,
                        upper: 425,
                        mean:  298
                    }
                }
            });
        }));

        it ('accumulates metrics within a category', inject(function($timeout, stats, googleAnalyticsBackend) {
            googleAnalyticsBackend.flush = sandbox.spy();

            stats.timer('category name',        'timer name', 250);
            stats.timer('second category name', 'timer name', 350);
            stats.timer('second category name', 'timer name', 425);
            stats.timer('category name',        'timer name', 167);

            $timeout.flush(intervalInMillis);

            expect(googleAnalyticsBackend.flush).toHaveBeenCalledWith({
                'category name': {
                    'timer name': {
                        lower: 167,
                        upper: 250,
                        mean:  209
                    }
                },
                'second category name': {
                    'timer name': {
                        lower: 350,
                        upper: 425,
                        mean:  388
                    }
                }
            });
        }));

        it ('accumulates metrics per parameter', inject(function($timeout, stats, googleAnalyticsBackend) {
            googleAnalyticsBackend.flush = sandbox.spy();

            stats.timer('category name', 'timer name', 250);
            stats.timer('category name', 'second timer name', 350);
            stats.timer('category name', 'timer name', 425);
            stats.timer('category name', 'second timer name', 167);

            $timeout.flush(intervalInMillis);

            expect(googleAnalyticsBackend.flush).toHaveBeenCalledWith({
                'category name': {
                    'timer name': {
                        lower: 250,
                        upper: 425,
                        mean:  338
                    },
                    'second timer name': {
                        lower: 167,
                        upper: 350,
                        mean:  259
                    }
                }
            });
        }));

        describe('integration with google analytics script', function () {

            it ('works for a single timer', inject(function($timeout, $window, stats) {
                $window.ga = sandbox.spy(); // let's pretend we've got google analytics script loaded

                stats.timer('my timing category', 'timer name', 315);

                $timeout.flush(intervalInMillis);

                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category', 'timer name lower', 315);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category', 'timer name upper', 315);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category', 'timer name mean',  315);
            }));

            it ('works for a timers from different categories', inject(function($timeout, $window, stats) {
                $window.ga = sandbox.spy(); // let's pretend we've got the google analytics script loaded

                stats.timer('my timing category 1', 'timer',        250);
                stats.timer('my timing category 1', 'timer',        251);
                stats.timer('my timing category 1', 'timer',        254);
                stats.timer('my timing category 1', 'timer',        252);
                stats.timer('my timing category 1', 'timer',        267);
                stats.timer('my timing category 1', 'timer',        221);
                stats.timer('my timing category 1', 'timer',        260);
                stats.timer('my timing category 1', 'timer',        243);
                stats.timer('my timing category 1', 'timer',        215);
                stats.timer('my timing category 1', 'timer',        237);
                stats.timer('my timing category 1', 'timer',        280);
                stats.timer('my timing category 2', 'second timer', 350);
                stats.timer('my timing category 3', 'third timer',  167);

                $timeout.flush(intervalInMillis);

                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 1', 'timer lower', 215);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 1', 'timer upper', 280);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 1', 'timer mean',  248);

                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 2', 'second timer lower', 350);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 2', 'second timer upper', 350);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 2', 'second timer mean',  350);

                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 3', 'third timer lower', 167);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 3', 'third timer upper', 167);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 3', 'third timer mean',  167);
            }));
        });

        function millis(number) {
            return number * 1000;
        }
    });
});