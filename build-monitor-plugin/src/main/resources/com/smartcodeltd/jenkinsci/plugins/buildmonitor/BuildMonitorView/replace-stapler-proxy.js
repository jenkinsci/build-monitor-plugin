/*
 * todo: (13.08.2013) Replace the below workaround with a custom Jelly tag (ExposeBindTag)
 *   extending either org.kohsuke.stapler.jelly.BindTag or AbstractStaplerTag,
 *   that would supersede currently defective BindTag implementation:
 *   - https://groups.google.com/forum/#!topic/jenkinsci-dev/S9bhX4ts0g4
 *   - https://issues.jenkins-ci.org/browse/JENKINS-18641
 *
 *   Defect in BindTag manifests itself by causing a JavaScript error and preventing scripts after
 *   the &lt;st:bind&gt; invocation from executing, which results in an "empty Build Monitor".
 *   The issue occurs on Jenkins 1.521-1.526, only if the jQuery plugin is used.
 *
 * Motivation behind a custom Jelly tag:
 *   Original implementation of the BindTag doesn't provide an easy way of handling AJAX errors,
 *   which may happen if a network connection is lost or when Jenkins is restarted (which then makes
 *   Stapler's binding hash obsolete and Jenkins return 404 for any subsequent requests).
 *
 *   Custom Jelly tag should generate a JSON object exposing the binding, leaving the implementation
 *   of the proxy to the Developer. It makes more sense for a developer to require a binding adapter
 *   implementation specific to their JavaScript framework of choice, rather than for Stapler to try
 *   to predict what JavaScript libraries will ever be used with it in the future...
 */
window.originalMakeStaplerProxy = window.makeStaplerProxy;
window.makeStaplerProxy = function(url, crumb, methods) {
    return { url, crumb, methods }
};
window.bindings = {};
