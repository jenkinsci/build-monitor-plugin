'use strict';

describe('buildMonitor', function () {
    describe('buildMonitor.services', function () {
        describe('hashCode', function () {

            beforeEach(module('buildMonitor.services'));

            it('produces a hash code uniquely identifying a string of text', inject(function (hashCode) {
                expect(hashCode('')).toEqual(0);
                expect(hashCode('name')).toEqual(3373707)
                expect(hashCode('Name')).toEqual(2420395);
            }));

            it('should treat "undefined" the same way as empty string', inject(function (hashCode) {
                expect(hashCode()).toEqual(hashCode(''));
            }));

            it('should treat "null" the same way as empty string', inject(function (hashCode) {
                expect(hashCode(null)).toEqual(hashCode(''));
            }));
        });
    });
});