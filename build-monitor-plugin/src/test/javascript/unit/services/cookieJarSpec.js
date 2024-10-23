'use strict';

describe('buildMonitor', function () {
    describe('buildMonitor.services', function () {
        describe('cookieJar', function () {

            var services,
                fakeStorage,
                NAME          = 'numberOfColumns',
                VALUE         = 3,
                DEFAULT_VALUE = 1;

            beforeEach(function () {
                services     = angular.module('buildMonitor.services');
                var items = {};
                var calls = 0;
                fakeStorage = {
                    getItem: function(key){
                        calls++;
                        return  key in items ? items[key] : null;
                    },
                    setItem: function(key, val) {
                        calls++;
                        items[key] = "" + val;
                    },
                    verify: function(expectedCalls) {
                        expect(calls).toBe(expectedCalls);
                    }
                };
                module('buildMonitor.services', function ($provide) {
                    $provide.value('persistentStorage', fakeStorage);
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
                    cookieJar.put(NAME, VALUE);
                    expect(cookieJar.get(NAME)).toBe(VALUE + "");

                    fakeStorage.verify(2)
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
                    cookieJar.put(NAME, VALUE);
                    cookieJar.get(NAME);
                    expect(fakeStorage.getItem(prefixed(NAME))).toBe(VALUE + "");
                    fakeStorage.verify(3);
                }));

                afterEach(function() {
                    //storageSpy.restore();
                });

                function prefixed(name) {
                    return COOKIE_JAR_LABEL + '.' + name;
                };
            });
        });
    });
});