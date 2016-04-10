'use strict';

describe('buildMonitor', function () {
    describe('buildMonitor.filters', function () {
        describe('estimatedTimeElapsedSince', function () {
            var milliseconds = 1,
                seconds      = 1000 * milliseconds,
                minutes      = 60 * seconds,
                hours        = 60 * minutes,
                days         = 24 * hours,
                months       = 30 * days,
                years        = 12 * months;

            beforeEach(module('buildMonitor.filters'));

            it('converts time in milliseconds to something more user-friendly (under 5 minutes)', inject(function (estimatedTimeElapsedSinceFilter) {
                expect(estimatedTimeElapsedSinceFilter(0   * minutes)).toEqual("just now :-)");
                expect(estimatedTimeElapsedSinceFilter(0.5 * minutes)).toEqual("just now :-)");
                expect(estimatedTimeElapsedSinceFilter(3   * minutes)).toEqual("a moment ago");
                expect(estimatedTimeElapsedSinceFilter(4.5 * minutes)).toEqual("a moment ago");
            }));

            it('approximates to the nearest minute (under 1 hour)', inject(function (estimatedTimeElapsedSinceFilter) {
                expect(estimatedTimeElapsedSinceFilter( 5.5  * minutes)).toEqual("6 minutes ago");
                expect(estimatedTimeElapsedSinceFilter(10.75 * minutes)).toEqual("11 minutes ago");
                expect(estimatedTimeElapsedSinceFilter(30    * minutes)).toEqual("30 minutes ago");
                expect(estimatedTimeElapsedSinceFilter(59    * minutes)).toEqual("59 minutes ago");
            }));

            it('approximates to the nearest hour (under 1 day)', inject(function (estimatedTimeElapsedSinceFilter) {
                expect(estimatedTimeElapsedSinceFilter(61    * minutes)).toEqual("1 hour ago");
                expect(estimatedTimeElapsedSinceFilter(1.3   * hours  )).toEqual("1 hour ago");
                expect(estimatedTimeElapsedSinceFilter(1.5   * hours  )).toEqual("2 hours ago");
                expect(estimatedTimeElapsedSinceFilter( 2    * hours  )).toEqual("2 hours ago");
                expect(estimatedTimeElapsedSinceFilter(17.61 * hours  )).toEqual("18 hours ago");
            }));

            it('approximates to the nearest day (under 1 month)', inject(function (estimatedTimeElapsedSinceFilter) {
                expect(estimatedTimeElapsedSinceFilter( 1.25 * days)).toEqual("1 day ago");
                expect(estimatedTimeElapsedSinceFilter( 2    * days)).toEqual("2 days ago");
                expect(estimatedTimeElapsedSinceFilter( 7    * days)).toEqual("7 days ago");
            }));

            it('approximates to the nearest month (under 1 year)', inject(function (estimatedTimeElapsedSinceFilter) {
                expect(estimatedTimeElapsedSinceFilter(31   * days  )).toEqual("1 month ago");
                expect(estimatedTimeElapsedSinceFilter( 2.3 * months)).toEqual("2 months ago");
                expect(estimatedTimeElapsedSinceFilter(11.7 * months)).toEqual("12 months ago");
            }));

            it('approximates to the nearest year (boy, I really hope no one needs this range!)', inject(function (estimatedTimeElapsedSinceFilter) {
                expect(estimatedTimeElapsedSinceFilter(13 * months)).toEqual("over a year ago");
                expect(estimatedTimeElapsedSinceFilter(25 * months)).toEqual("hasn't run in ages!");
                expect(estimatedTimeElapsedSinceFilter(5  * years)).toEqual("hasn't run in ages!");
            }));
        });
    });
});