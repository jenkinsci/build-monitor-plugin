'use strict';

describe ('buildMonitor', function () {
    describe ('buildMonitor.services', function () {
        describe ('version', function () {

            beforeEach (module('buildMonitor.services'));

            it ('recognises identical versions', inject(function (version) {
                expect(version('1.0+build.1').isOlderThan('1.0+build.1')).toBe(false);
            }));

            it ("can be used to compare the build number of one version with another", inject(function (version) {
                expect(version('1.0+build.1').isOlderThan('1.0+build.10')).toBe(true);
            }));

            it ("considers development version to be older", inject(function (version) {
                expect(version('2.0-SNAPSHOT').isOlderThan('1.0+build.10')).toBe(true);
            }));

            it ("works with the v-prefixed release tags as well", inject(function (version) {
                expect(version('2.0-SNAPSHOT').isOlderThan('v1.0+build.10')).toBe(true);
            }));
        });
    });
});