'use strict';

describe('buildMonitor', function () {
    describe('buildMonitor.services', function () {
        describe('storage', function () {

            beforeEach(module('buildMonitor.services', function($provide) {
                $provide.value('buildMonitorName', 'Example Build Monitor Name');
                $provide.factory('hashCodeOf', dummyHashCodeOf);
            }));

            it('persist values using a unique, build monitor-specific cookie', inject(function (storage, $cookies) {
                storage.persist('numberOfColumns', 3);

                expect($cookies).toEqual({ 'buildMonitor.hashCode.numberOfColumns' : 3 });
            }));

            it('allows for a value to be persisted and then retrieved', inject(function (storage) {
                var expectedValue = 3;

                storage.persist('numberOfColumns', expectedValue);

                expect(storage.retrieve('numberOfColumns')).toBe(expectedValue);
            }));

            it('returns "undefined" if requested value couldn\'t be found in a cookie and no defaults are provided', inject(function (storage) {
                expect(storage.retrieve('numberOfColumns')).toBe(undefined);
            }));
            
            it('returns default value if the one requested couldn\'t be found in a cookie', inject(function (storage) {
                var defaultValue = 'default value';

                expect(storage.retrieve('numberOfColumns', defaultValue)).toBe(defaultValue);
            }));
            

            function dummyHashCodeOf() {
                return function() { return 'hashCode'; }
            }
        });
    });
});