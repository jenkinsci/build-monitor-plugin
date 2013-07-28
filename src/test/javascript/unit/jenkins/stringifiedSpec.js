'use strict';

describe('buildMonitor', function () {

    describe('jenkins', function () {

        describe('stringified converts', function() {
            beforeEach(module('jenkins'));

            it('empty objects to Jenkins-accepted format', inject(function (stringified) {
                expect(stringified([])).toBe('[]');
                expect(stringified({})).toBe('{}');
                expect(stringified(null)).toBe('null');
                expect(stringified('')).toBe('');
            }));

            it('an array of parameters to Jenkins-accepted format', inject(function (stringified) {
                expect(stringified(['a', 2, "c"])).toBe('["a",2,"c"]');
            }));

            it('a complex object to Jenkins-accepted format', inject(function (stringified) {
                var input          = { array: ['a', { key: 'value'}]},
                    expectedOutput = '{"array":["a",{"key":"value"}]}';

                expect(stringified(input)).toBe(expectedOutput);
            }));
        });
    });
});