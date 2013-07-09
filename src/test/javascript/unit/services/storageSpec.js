'use strict';

describe('buildMonitor', function () {
    describe('buildMonitor.services', function () {
        describe('storage', function () {

            beforeEach(module('buildMonitor.services', function($provide) {
                $provide.value('buildMonitorName', 'Example Build Monitor Name');
                $provide.service('hashCode', serviceStub({ of : 'hashCode'}));
            }));

            it('persist values using a build monitor-specific cookie', inject(function (storage, $cookies) {
                storage.persist('numberOfColumns', 3);

                expect($cookies).toEqual({ 'buildMonitor.hashCode.numberOfColumns' : 3 });
            }));


            function serviceStub( methodAndReturnValuePairs ) {
                return function() {
                    for (var method in methodAndReturnValuePairs) {
                        this[method] = function() { return methodAndReturnValuePairs[method]; }
                    }
                }
            }
        });
    });
});