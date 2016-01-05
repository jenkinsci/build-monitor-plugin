'use strict';

describe('buildMonitor', function () {
    describe('stats', function () {
        var stats,
            googleAnalyticsBackend,
            $timeout,
            $window,
            sandbox,
            interval = seconds(10);

        beforeEach(function() {
            sandbox = sinon.sandbox.create();

            module('buildMonitor.stats', function(statsProvider) {
                statsProvider.configure({ flushIntervalInSeconds: interval });
            });

            inject(function (_stats_, _googleAnalyticsBackend_, _$timeout_, _$window_) {
                stats = _stats_;
                googleAnalyticsBackend = _googleAnalyticsBackend_;

                $timeout   = _$timeout_;
                $window    = _$window_;
            });
        });

        afterEach(function () { sandbox.restore(); });

        it ('does not flush the metrics if there are none recorded', function () {
            googleAnalyticsBackend.flush = sandbox.spy();

            $timeout.flush();

            expect(googleAnalyticsBackend.flush).not.toHaveBeenCalled();
        });

        it ('flushes the timers after a "flushInterval" seconds', function () {
            googleAnalyticsBackend.flush = sandbox.spy();

            stats.timer('category name', 'timer name', 250);

            $timeout.flush(seconds(10));

            expect(googleAnalyticsBackend.flush).toHaveBeenCalledWith({
                'category name': {
                  'timer name': {
                      upper: 250,
                      lower: 250,
                      mean:  250
                  }
                }
            });
        });

        it ('removes recorded metrics after the flush', function () {
            googleAnalyticsBackend.flush = sandbox.spy();

            stats.timer('category name', 'timer name', 250);

            $timeout.flush(interval);
            $timeout.flush(interval);

            expect(googleAnalyticsBackend.flush).toHaveBeenCalledOnce();
        });

        it ('starts fresh with every interval', function () {
            googleAnalyticsBackend.flush = sandbox.spy();

            stats.timer('category name', 'timer name', 250);
            $timeout.flush(interval);

            stats.timer('category name', 'timer name', 475);
            $timeout.flush(interval);

            expect(googleAnalyticsBackend.flush).toHaveBeenCalledWith({
                'category name': {
                    'timer name': {
                        lower: 475,
                        upper: 475,
                        mean:  475
                    }
                }
            });
        });

        it ('accumulates metrics within an interval', function () {
            googleAnalyticsBackend.flush = sandbox.spy();

            stats.timer('category name', 'timer name', 250);
            stats.timer('category name', 'timer name', 350);
            stats.timer('category name', 'timer name', 425);
            stats.timer('category name', 'timer name', 167);

            $timeout.flush(interval);

            expect(googleAnalyticsBackend.flush).toHaveBeenCalledWith({
                'category name': {
                    'timer name': {
                        lower: 167,
                        upper: 425,
                        mean:  298
                    }
                }
            });
        });

        it ('accumulates metrics within a category', function () {
            googleAnalyticsBackend.flush = sandbox.spy();

            stats.timer('category name',        'timer name', 250);
            stats.timer('second category name', 'timer name', 350);
            stats.timer('second category name', 'timer name', 425);
            stats.timer('category name',        'timer name', 167);

            $timeout.flush(interval);

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
        });

        it ('accumulates metrics per parameter', function () {
            googleAnalyticsBackend.flush = sandbox.spy();

            stats.timer('category name', 'timer name', 250);
            stats.timer('category name', 'second timer name', 350);
            stats.timer('category name', 'timer name', 425);
            stats.timer('category name', 'second timer name', 167);

            $timeout.flush(interval);

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
        });

        describe('integration with google analytics script', function () {

            it ('works for a single timer', function () {
                $window.ga = sandbox.spy(); // let's pretend we've got google analytics script loaded

                stats.timer('my timing category', 'timer name', 315);

                $timeout.flush(interval);

                expect(ga).toHaveBeenCalledWith('send', 'timing', 'my timing category', 'timer name lower', 315);
                expect(ga).toHaveBeenCalledWith('send', 'timing', 'my timing category', 'timer name upper', 315);
                expect(ga).toHaveBeenCalledWith('send', 'timing', 'my timing category', 'timer name mean',  315);
            });

            it ('works for a timers from different categories', function () {
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

                $timeout.flush(interval);

                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 1', 'timer lower', 215);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 1', 'timer upper', 280);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 1', 'timer mean',  248);

                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 2', 'second timer lower', 350);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 2', 'second timer upper', 350);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 2', 'second timer mean',  350);

                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 3', 'third timer lower', 167);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 3', 'third timer upper', 167);
                expect($window.ga).toHaveBeenCalledWith('send', 'timing', 'my timing category 3', 'third timer mean',  167);
            });
        });

        function seconds(number) {
            return number * 1000;
        }
    });
});