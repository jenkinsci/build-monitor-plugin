function toBeA(expected) {
    return typeof this.actual === expected;
}

function toBeInstanceOf(expected) {
    return this.actual instanceof expected && this.actual.length > 0;
}

function toHaveFollowingMethods(expectedMethods) {
    var missingMethods = [],
        foundMethods   = [],
        method;

    function isAFunction(candidate) {
        return typeof candidate == 'function';
    }

    for (var i = 0; i < expectedMethods.length; i++) {
        method       = expectedMethods[i];

        if (! isAFunction(this.actual[method])) {
            missingMethods.push(method);
        }
    }

    for (method in this.actual) {
        if (this.actual.hasOwnProperty(method) && isAFunction(this.actual[method])) {
            foundMethods.push(method);
        }
    }

    this.message = function () {
        return "Expected the object to have following methods: " +
            jasmine.pp(missingMethods.sort()) +
            "\ninstead, it only had the following: " +
            jasmine.pp(foundMethods.sort())

    }

    return missingMethods.length == 0;
}

jasmine.Matchers.prototype.toBeInstanceOf = toBeInstanceOf;
jasmine.Matchers.prototype.toHaveFollowingMethods = toHaveFollowingMethods;
jasmine.Matchers.prototype.toBeA = toBeA;
jasmine.Matchers.prototype.toBeAn = toBeA;