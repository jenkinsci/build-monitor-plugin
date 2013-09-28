'use strict';

describe('buildMonitor', function () {
    describe('buildMonitor.services', function () {
        describe('cookieJar', function () {

            var services,
                YahooCookie   = YAHOO.util.Cookie,
                mockedCookie,   // YAHOO.util.Cookie backend used by the cookieJar

                NAME          = 'numberOfColumns',
                VALUE         = 3,
                DEFAULT_VALUE = 1;

            beforeEach(function () {
                services     = angular.module('buildMonitor.services');

                mockedCookie = sinon.mock(YahooCookie);

                module('buildMonitor.services', function ($provide) {
                    $provide.value('YahooCookie', YahooCookie);
                });
            });

            describe('default configuration', function() {

                it('returns "undefined" if requested value couldn\'t be found in a cookie and no defaults are provided', inject(function (cookieJar) {
                    expect(cookieJar.get('aCookieThatDoesNotExist')).toBeUndefined();
                }));

                it('returns default value if the one requested couldn\'t be found in a cookie', inject(function (cookieJar) {
                    expect(cookieJar.get('aCookieThatDoesNotExist', DEFAULT_VALUE)).toBe(DEFAULT_VALUE);
                }));

                it('allows for a value to be persisted and then retrieved', inject(function (cookieJar) {
                    mockedCookie.expects("set").withArgs(NAME, VALUE);
                    mockedCookie.expects("get").withArgs(NAME).returns(VALUE);


                    cookieJar.put(NAME, VALUE);

                    expect(cookieJar.get(NAME)).toBe(VALUE);

                    mockedCookie.verify()
                }));

                it('should use cookies that expire at the end of the session', inject(function (cookieJar) {
                    var noExpiryDateSpecified = {};

                    mockedCookie.expects("set").withArgs(NAME, VALUE, noExpiryDateSpecified);


                    cookieJar.put(NAME, VALUE);

                    mockedCookie.verify();
                }));
            });

            describe('custom label', function () {

                var COOKIE_JAR_LABEL   = 'chocolateChipCookies';

                beforeEach(function () {
                    services.config(function(cookieJarProvider) {
                        cookieJarProvider.describe({
                            label: COOKIE_JAR_LABEL
                        });
                    });
                });

                it('should prefix each cookie with a label', inject(function (cookieJar) {
                    mockedCookie.expects("set").withArgs(prefixed(NAME), VALUE);
                    mockedCookie.expects("get").withArgs(prefixed(NAME));


                    cookieJar.put(NAME, VALUE);
                    cookieJar.get(NAME);

                    mockedCookie.verify();
                }));

                afterEach(function() {
                    mockedCookie.restore();
                });

                function prefixed(name) {
                    return COOKIE_JAR_LABEL + '.' + name;
                };
            });

            describe('custom shelf life', function() {

                var SHELF_LIFE_IN_DAYS = 7;

                beforeEach(function () {

                    services.config(function(cookieJarProvider) {
                        cookieJarProvider.describe({
                            shelfLife: SHELF_LIFE_IN_DAYS
                        });
                    });
                });

                it('should use cookies that expire when specified', inject(function (cookieJar) {
                    mockedCookie.expects("set").withArgs(NAME, VALUE, {
                        expires: dateIn(SHELF_LIFE_IN_DAYS)}
                    );

                    cookieJar.put(NAME, VALUE);

                    mockedCookie.verify();
                }));

                afterEach(function() {
                    mockedCookie.restore();
                });

                function dateIn(days) {
                    return new Date(+new Date() + (days * 24 * 3600 * 1000));
                };
            });
        });
    });
});