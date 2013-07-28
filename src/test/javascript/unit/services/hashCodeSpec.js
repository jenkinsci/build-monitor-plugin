'use strict';

describe('buildMonitor', function () {
    describe('buildMonitor.services', function () {
        describe('hashCodeOf', function () {

            beforeEach(module('buildMonitor.services'));

            it('produces a hash code uniquely identifying a string of text', inject(function (hashCodeOf) {
                expect(hashCodeOf('')).toEqual(0);
                expect(hashCodeOf('name')).toEqual(3373707)
                expect(hashCodeOf('Name')).toEqual(2420395);
            }));

            it('should treat "undefined" the same way as empty string', inject(function (hashCodeOf) {
                expect(hashCodeOf()).toEqual(hashCodeOf(''));
            }));

            it('should treat "null" the same way as empty string', inject(function (hashCodeOf) {
                expect(hashCodeOf(null)).toEqual(hashCodeOf(''));
            }));
        });
    });
});