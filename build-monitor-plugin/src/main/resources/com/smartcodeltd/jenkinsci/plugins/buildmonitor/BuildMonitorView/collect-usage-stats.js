window.ga=window.ga||function(){(ga.q=ga.q||[]).push(arguments)};ga.l=+new Date;

const {
    buildMonitorVersion,
    jenkinsVersion,
    installationSize,
    itemsSize,
    installationAudience,
    anonymousCorrelationId
} = document.querySelector('.build-monitor-ga-data-holder').dataset;

ga('create', 'UA-61694827-4', 'auto', {
    'userId': anonymousCorrelationId,
    'sampleRate': 1
});

ga('set', {
    'forceSSL': true,
    'appName': 'Build Monitor',
    'appId': 'build-monitor-plugin',

    'appVersion': buildMonitorVersion,
    'appInstallerId': jenkinsVersion,

    'dimension1': installationSize,
    'dimension2': itemsSize,
    'dimension3': installationAudience,
    'dimension4': anonymousCorrelationId
});

ga('send', 'screenview', {screenName: 'Dashboard'});
