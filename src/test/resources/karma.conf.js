// Karma configuration
// Generated on Sat Jul 06 2013 11:56:28 GMT+0100 (BST)


// base path, that will be used to resolve files and exclude
basePath = '../../../';

frameworks = ['jasmine'];

// list of files / patterns to load in the browser
files = [
    JASMINE,
    JASMINE_ADAPTER,
    'src/test/resources/syntacticSugar.js',
    'src/main/webapp/libs/angular-1.1.5.js',
    'src/main/webapp/libs/angular-cookies-1.1.5.min.js',
    'src/main/webapp/libs/angular-slider.js',
    'src/main/webapp/libs/ui-*.js',
    'src/test/resources/angular-mocks.js',
    'src/test/resources/sinon-1.7.3.js',
    'src/main/webapp/scripts/**/*.js',
    'src/test/javascript/**/*Spec.js'
];


// list of files to exclude
exclude = [

];

preprocessors = {
    'src/main/webapp/scripts/**/*.js': 'coverage'
};


// test results reporter to use
// possible values: 'dots', 'progress', 'junit'
reporters = ['progress', 'junit', 'coverage'];

junitReporter = {
    outputFile: 'target/javascript/test-results.xml'
};

coverageReporter = {
    type: 'cobertura',
    dir: 'target/javascript/coverage/',
    file: 'coverage.xml'
};

// web server port
port = 9876;


// cli runner port
runnerPort = 9100;


// enable / disable colors in the output (reporters and logs)
colors = true;


// level of logging
// possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
logLevel = LOG_INFO;


// enable / disable watching file and executing tests whenever any file changes
autoWatch = false;


// Start these browsers, currently available:
// - Chrome
// - ChromeCanary
// - Firefox
// - Opera
// - Safari (only Mac)
// - PhantomJS
// - IE (only Windows)
browsers = ['PhantomJS'];


// If browser does not capture in given timeout [ms], kill it
captureTimeout = 60000;


// Continuous Integration mode
// if true, it capture browsers, run tests and exit
singleRun = false;
