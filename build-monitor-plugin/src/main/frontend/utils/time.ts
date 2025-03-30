/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

let KB = 1024;
let MB = KB * 1024;
let GB = MB * 1024;
let TB = GB * 1024;

let sec = 1000;
let min = sec * 60;
let hr  = min * 60;
let day = hr * 24;
let yr = day * 365;

exports.memory = function (amount: number) {
    if (amount > TB) {
        return (amount / TB).toFixed(2) + "TB";
    } else if (amount > GB) {
        return (amount / GB).toFixed(2) + "GB";
    } else if (amount > MB) {
        return (amount / MB).toFixed(2) + "MB";
    } else if (amount > KB) {
        return (amount / KB).toFixed(2) + "KB";
    } else {
        return amount + "B";
    }
}

export default function time(millis: number, numUnitsToShow?: number) {
    if (millis <= 0 || isNaN(millis)) {
        return '0ms';
    }

    if (numUnitsToShow === undefined) {
        numUnitsToShow = 3;
    }

    function mod(timeUnit: number) {
        let numUnits = Math.floor(millis / timeUnit);
        millis = millis % timeUnit;
        return numUnits;
    }

    let years = mod(yr);
    let days = mod(day);
    let hours = mod(hr);
    let minutes = mod(min);
    let seconds = mod(sec);

    let numUnitsAdded = 0;
    let formattedTime = '';

    function round(num: number) {
        return Math.round(num * 100) / 100;
    }

    function addTimeUnit(value: number, unit: string) {
        if (numUnitsAdded === numUnitsToShow) {
            // don't add any more
            return;
        }
        if (value === 0 && numUnitsAdded === 0) {
            // Don't add a leading zero
            return;
        }

        if (unit === 'ms') {
            value = Math.round(value);
        } else {
            value = round(value);
        }

        // add this one.
        if (formattedTime === '') {
            formattedTime += value + unit;
        } else {
            formattedTime += ' ' + value + unit;
        }

        numUnitsAdded++;
    }

    addTimeUnit(years, 'y');
    addTimeUnit(days, 'd');
    addTimeUnit(hours, 'h');
    addTimeUnit(minutes, 'min');
    addTimeUnit(seconds, 's');
    // Only show millis if the time is below 1 second
    if (seconds === 0) {
        addTimeUnit(millis, 'ms');
    }

    return formattedTime;
}
