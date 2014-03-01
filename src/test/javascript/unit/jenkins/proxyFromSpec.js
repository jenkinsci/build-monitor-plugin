'use strict';

describe('buildMonitor', function () {

    describe('jenkins', function () {

        var httpBackend,
            stringified;

        beforeEach(module('jenkins'));

        beforeEach(inject(function($injector) {
            httpBackend = $injector.get('$httpBackend');
            stringified = $injector.get('stringified');
        }));

        afterEach(function() {
            httpBackend.verifyNoOutstandingExpectation();
            httpBackend.verifyNoOutstandingRequest();
        });

        describe('proxyFrom', function() {

            var http  = {
                success:             200,
                internalServerError: 500
            };

            var url   = '/$stapler/bound/97daf472-ab2b-4e96-a02b-968970e68030',
                crumb = '1d7eae2a-3c33-4fbe-b717-726ac4da8a52';

            var jellyGeneratedServerSideCalculatorBinding = {
                url: url,
                crumb: crumb,
                methods: ['add']
            };

            describe('factory', function() {

                it('creates proxies configured with jelly-generated server-side bindings', inject(function (proxyFrom) {
                    var serverSideCalculator = proxyFrom(jellyGeneratedServerSideCalculatorBinding);

                    expect(serverSideCalculator).toBeAn('object');
                    expect(serverSideCalculator).toHaveFollowingMethods(['add']);
                }));
            });

            describe('proxy', function() {

                afterEach(function() {
                    httpBackend.flush();
                });

                it('allows to call bound server-side objects', inject(function(proxyFrom) {
                    var validResponse = 3;

                    givenRemoteMethodCall('add', [1, 2]).
                        willSucceedWith(validResponse);


                    var serverSideCalculator = proxyFrom(jellyGeneratedServerSideCalculatorBinding);

                    serverSideCalculator.add(1, 2).then(function (response) {
                        expect(response.data).toBe(validResponse);
                    });
                }));

                it('recognises back-end failures', inject(function(proxyFrom) {
                    var someExplanation = "Unknown error has occurred";

                    givenRemoteMethodCall('add', [1, 2]).
                        willFailWith(http.internalServerError, someExplanation);


                    var serverSideCalculator = proxyFrom(jellyGeneratedServerSideCalculatorBinding);

                    serverSideCalculator.add(1, 2).error(function (error, status) {
                        expect(status).toBe(http.internalServerError);
                        expect(error).toBe(someExplanation);
                    });
                }));
            });


            // teeny-tiny DSL to simplify setting up the httpBackend mock
            function givenRemoteMethodCall(method, parameters) {
                var expected = {
                    url:     url + '/' + method,
                    data:    stringified(parameters),
                    headers: {
                        'Accept':       'application/json, text/plain, */*',
                        'Content-Type': 'application/x-stapler-method-invocation;charset=UTF-8',
                        'Crumb':        crumb,
                        '.crumb':       crumb
                    }
                };

                function respondsWith(httpStatus, data) {
                    httpBackend.
                        whenPOST(expected.url, expected.data, expected.headers).
                        respond(httpStatus, data);
                }

                return {
                    willSucceedWith: function(result) {
                        respondsWith(http.success, result);
                    },
                    willFailWith: function(errorCode, explanation) {
                        respondsWith(errorCode, explanation);
                    }
                }
            }
        });
    });
});