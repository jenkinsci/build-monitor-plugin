'use strict';

describe('buildMonitor', function () {
    describe('buildMonitor.services', function () {
        describe('hashCode', function () {

            beforeEach(module('buildMonitor.services'));

            it('produces a hash code uniquely identifying a string of text', inject(function (hashCode) {
                expect(hashCode.of('')).toEqual(0);
                expect(hashCode.of('name')).toEqual(3373707)
                expect(hashCode.of('Name')).toEqual(2420395);
            }));

            it('should treat "undefined" the same way as empty string', inject(function (hashCode) {
                expect(hashCode.of()).toEqual(hashCode.of(''));
            }));

            it('should treat "null" the same way as empty string', inject(function (hashCode) {
                expect(hashCode.of(null)).toEqual(hashCode.of(''));
            }));
        });
    });
});