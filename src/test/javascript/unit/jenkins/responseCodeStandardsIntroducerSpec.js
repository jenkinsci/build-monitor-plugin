'use strict';

describe('buildMonitor', function () {

    describe('jenkins', function () {

        describe('responseCodeStandardsIntroducer', function () {
            beforeEach(module('jenkins'));

            it('marks an error response with 500 status without affecting the original response object', inject(function (responseCodeStandardsIntroducer, STAPLER_CONTENT_TYPE, $rootScope) {
                var OK = 200,
                    INTERNAL_SERVER_ERROR = 500,
                    staplerErrorResponse = {
                        config: {
                            headers: {
                                'Content-Type': STAPLER_CONTENT_TYPE
                            }
                        },
                        status: OK,
                        data: {
                            "cause": null,
                            "localizedMessage": "",
                            "message": "",
                            "stackTrace": [
                                {"className": "java.util.TreeSet", "fileName": "TreeSet.java", "lineNumber": 154, "methodName": "<init>", "nativeMethod": false},
                                {"className": "hudson.model.ListView", "fileName": "ListView.java", "lineNumber": 144, "methodName": "getItems", "nativeMethod": false}
                            ]}
                    }


                responseCodeStandardsIntroducer.response(staplerErrorResponse).then(null, function (response) {
                    expect(response.status).toBe(INTERNAL_SERVER_ERROR);
                });

                $rootScope.$digest();

                expect(staplerErrorResponse.status).toBe(OK);
            }));
        });
    });
});