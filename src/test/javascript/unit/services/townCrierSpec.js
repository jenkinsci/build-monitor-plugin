'use strict';

describe('buildMonitor', function () {
    describe('buildMonitor.services', function () {
        describe('townCrier', function () {

            var httpBackend,
                timeout,
                provide,
                notifyMe;

            beforeEach (module('buildMonitor.services'));

            beforeEach (module(function($provide){
                provide = $provide;
            }));

            beforeEach (inject(function($injector) {
                httpBackend = $injector.get('$httpBackend');
                timeout     = $injector.get('$timeout');

                notifyMe    = sinon.spy();
            }));

            afterEach (function() {
                httpBackend.verifyNoOutstandingExpectation();
                httpBackend.verifyNoOutstandingRequest();
            });

            it ("notifies interested parties upon a new version", inject(function ($injector) {
                given_the_installed_version_of_build_monitor_is('1.7+build.163').
                and_the_latest_version_available_is('1.7+build.164');

                var townCrier = $injector.get('townCrier');

                var notifyMe = sinon.spy();

                townCrier.uponNewVersion(notifyMe);

                townCrier.start();
                httpBackend.flush();

                expect(notifyMe).toHaveBeenCalledOnce();
            }));

            it ("notifies interested parties upon a new version when they're using a dev build", inject(function ($injector) {
                given_the_installed_version_of_build_monitor_is('1.7-SNAPSHOT').
                and_the_latest_version_available_is('1.7+build.164');

                var townCrier = $injector.get('townCrier');

                var notifyMe = sinon.spy();

                townCrier.uponNewVersion(notifyMe);

                townCrier.start();
                httpBackend.flush();

                expect(notifyMe).toHaveBeenCalledOnce();
            }));

            it ("doesn't bother anyone if there's been no release", inject(function ($injector) {

                given_the_installed_version_of_build_monitor_is('1.7+build.163').
                and_the_latest_version_available_is('1.7+build.163');

                var townCrier = $injector.get('townCrier');

                townCrier.uponNewVersion(notifyMe);

                townCrier.start();
                httpBackend.flush();

                expect(notifyMe).not.toHaveBeenCalled();
            }));

            it ("only checks github release pages once per hour", inject(function ($injector) {
                given_the_installed_version_of_build_monitor_is('1.7+build.163').
                and_the_latest_version_available_is('1.7+build.164');

                var townCrier = $injector.get('townCrier');

                townCrier.uponNewVersion(notifyMe);

                townCrier.start();
                httpBackend.flush();

                timeout.flush(3600 * 1000);
                httpBackend.flush();

                expect(notifyMe.callCount).toEqual(2);
            }));

            it ("works when upgrading from Build Monitor 1.7 to 1.8", inject(function ($injector) {
                given_the_installed_version_of_build_monitor_is('1.7+build.163').
                and_the_latest_version_available_is('1.8+build.201512282338');

                var townCrier = $injector.get('townCrier');

                townCrier.uponNewVersion(notifyMe);

                townCrier.start();
                httpBackend.flush();

                timeout.flush(3600 * 1000);
                httpBackend.flush();

                expect(notifyMe.callCount).toEqual(2);
            }));

            // --

            function given_the_installed_version_of_build_monitor_is(version) {
                provide.constant('BUILD_MONITOR_VERSION', version);

                return {
                    and_the_latest_version_available_is: function(released_version) {
                        httpBackend.
                            whenJSONP('https://api.github.com/repos/jenkinsci/build-monitor-plugin/releases/latest?callback=JSON_CALLBACK').
                            respond(200, { data: { tag_name: 'v' + released_version }});
                    }
                }
            }
        });
    });
});