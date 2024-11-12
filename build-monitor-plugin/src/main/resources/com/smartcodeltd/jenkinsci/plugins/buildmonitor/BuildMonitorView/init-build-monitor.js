'use strict';

(function() {
    const { buildMonitorVersion, csrfCrumbFieldName } = document.querySelector(".build-monitor-data-holder").dataset;

    angular
        .module('buildMonitor')
        .constant('BUILD_MONITOR_VERSION', buildMonitorVersion)
        .constant('CSRF_CRUMB_FIELD_NAME', csrfCrumbFieldName)
        .config(function(proxyProvider, cookieJarProvider, hashCodeProvider) {
        var hashCodeOf = hashCodeProvider.hashCodeOf;

        proxyProvider.configureProxiesUsing(window.bindings);

        cookieJarProvider.describe({
            label: 'buildMonitor.' + hashCodeOf(document.body.dataset.displayName)
        });
    });
})();
