(function (jasmine, beforeEach) {
    'use strict';

    function toBeA(util, customEqualityTesters) {
        return {
            compare: function (actual, expected) {
                var result = {};

                result.pass = typeof actual === expected;

                return result;
            }
        }
    }

    function toBeInstanceOf(util, customEqualityTesters) {
        return {
            compare: function (actual, expected) {
                var result = {};

                result.pass = actual instanceof expected && actual.length > 0;

                return result;
            }
        }
    }

    function toHaveFollowingMethods(util, customEqualityTesters) {
        return {
            compare: function toHaveFollowingMethods(actual, expectedMethods) {
                var result = {},
                    missingMethods = [],
                    foundMethods = [],
                    method;

                function isAFunction(candidate) {
                    return typeof candidate == 'function';
                }

                for (var i = 0; i < expectedMethods.length; i++) {
                    method = expectedMethods[i];

                    if (!isAFunction(actual[method])) {
                        missingMethods.push(method);
                    }
                }

                for (method in actual) {
                    if (actual.hasOwnProperty(method) && isAFunction(actual[method])) {
                        foundMethods.push(method);
                    }
                }

                result.pass = missingMethods.length == 0;
                result.message = "Expected the object to have following methods: " +
                    jasmine.pp(missingMethods.sort()) +
                    "\ninstead, it only had the following: " +
                    jasmine.pp(foundMethods.sort());

                return result;
            }
        }
    }

    beforeEach(function() {
        jasmine.addMatchers({
            toBeInstanceOf: toBeInstanceOf,
            toHaveFollowingMethods: toHaveFollowingMethods,
            toBeA: toBeA,
            toBeAn: toBeA
        });
    });

})(jasmine, beforeEach);