'use strict';

describe('buildMonitor', function () {
    describe('buildMonitor.services', function () {
        describe('counter', function () {

            var counter;

            beforeEach(module('buildMonitor.services'));

            beforeEach(inject(function(_counter_) {
                counter = _counter_;
            }));

            it('starts from zero', inject(function (_counter_) {
                expect(counter.value()).toBe(0);
            }));

            it('allows to increment the count', inject(function (_counter_) {
                counter.increase();

                expect(counter.value()).toBe(1);
            }));

            it('allows to increment the count', inject(function (_counter_) {
                counter.increase();

                expect(counter.value()).toBe(1);
            }));

            it('allows to reset the count', inject(function (_counter_) {
                counter.increase();
                counter.reset();

                expect(counter.value()).toBe(0);
            }));
        });
    });
});