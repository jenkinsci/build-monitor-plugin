window.bindings['buildMonitor'] = buildMonitorBind;
window.makeStaplerProxy = window.originalMakeStaplerProxy;
try {
    delete window.originalMakeStaplerProxy;
} catch(e) {
    window["originalMakeStaplerProxy"] = undefined;
}
