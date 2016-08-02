describe('buildMonitor', function () {
    describe('buildMonitor.cron', function () {
        describe('every', function () {

            var interval = 1000;

            var noop = angular.noop;

            beforeEach(module('buildMonitor.cron'));

            it('executes the first iteration straight away', inject(function (every) {
                var counter = 0;

                every(interval, function () {
                    counter++;
                });

                expect(counter).toBe(1);
            }));

            it('runs task repeatedly', inject(function (every, $timeout) {
                var counter = 0;

                every(interval, function () {
                    counter++;
                });

                $timeout.flush(interval);

                expect(counter).toBe(2);
            }));

            it('calls $apply after each task is executed', inject(function (every, $rootScope) {
                var apply = sinon.spy($rootScope, '$apply');

                every(interval, noop);

                expect(apply).toHaveBeenCalledOnce();
            }));

            describe('asynchronous steps', function () {

                it('shows how angular $q needs to be used with $rootScope.$digest', inject(function ($q, $rootScope) {
                    // https://groups.google.com/forum/#!topic/angular/0dhQzTPexA0

                    var deferred = $q.defer(),
                        promise = deferred.promise,
                        counter = 0;

                    promise.then(function () {
                        counter++;
                    });

                    deferred.resolve();

                    expect(counter).toBe(0);

                    // that's the important bit !
                    $rootScope.$digest();

                    expect(counter).toBe(1);
                }));

                it('progresses the loop if the current step resolves the promise of the next step', inject(function (every, $rootScope, $timeout, $q) {
                    var task = sinon.spy();

                    every(interval, function () {
                        task();

                        return $q.when({});
                    });


                    expect(task.callCount).toBe(1);

                    $rootScope.$digest();
                    $timeout.flush(interval);

                    expect(task.callCount).toBe(2);
                }));

                it('stops the loop if the current step brakes the promise of a next step', inject(function (every, $rootScope, $timeout, $q) {
                    var task = sinon.spy();

                    every(interval, function () {
                        task();

                        return $q.reject({});
                    });

                    $timeout.flush(interval)

                    expect(task.callCount).toBe(1);
                }));


                it('calls $apply after the step is completed', inject(function (every, $rootScope, $q) {
                    var apply = sinon.spy($rootScope, '$apply');

                    every(interval, function () {
                        return $q.when({});
                    });

                    expect(apply.callCount).toBe(1);
                }));

                it("won't run the next step until the previous step has completed", inject(function (every, $q, $timeout, $q) {
                    var syncTask = sinon.spy(),

                        asyncTask = sinon.spy(),
                        asyncTaskLength = 2 * interval;

                    every(interval, function () {
                        syncTask();

                        var deferred = $q.defer();

                        $timeout(function () {
                            asyncTask();

                            deferred.resolve({});
                        }, asyncTaskLength);

                        return deferred.promise;
                    });

                    // straight after invoking 'every':
                    expect(syncTask.callCount).toBe(1);
                    expect(asyncTask.callCount).toBe(0);

                    // after the interval (half the time it takes to complete the asyncTask):
                    $timeout.flush(interval);
                    expect(syncTask.callCount).toBe(1);
                    expect(asyncTask.callCount).toBe(0);

                    // after another interval (it takes two intervals to complete the asyncTask):
                    $timeout.flush(interval);
                    expect(syncTask.callCount).toBe(1);
                    expect(asyncTask.callCount).toBe(1);
                }));
            });
        });
    });
});