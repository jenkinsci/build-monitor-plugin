# Jenkins CI Build Monitor Plugin

Build Monitor Plugin provides a highly visible view of the status of selected Jenkins jobs.

It easily accommodates different computer screen sizes and is ideal as an Extreme Feedback Device to be displayed on a screen on your office wall.
(Inspired by the no longer maintained RadiatorView plugin).

[![Build Status](https://smartcode.ci.cloudbees.com/buildStatus/icon?job=jenkins-build-monitor-plugin)](https://smartcode.ci.cloudbees.com/job/jenkins-build-monitor-plugin/)

## Setting up

To create a new Build Monitor View, click on the "New View" tab, select "Build Monitor View" and select jobs you wish
to display on the monitor. You can have as many Build Monitor Views as you want - the most popular approach is to have one per team
or one per project.

## Features

I'd like to keep the plugin as simple as possible and avoid cluttering the view with statistics I don't find of high value in the context of a Build Monitor.

Current functionality includes:

1. Displaying the status and progress of selected jobs
1. Displaying names of people who might be responsible for "breaking the build"
1. Easily customisable number of columns and size of the font used, making it easier to accommodate screens of different sizes
1. UI configuration is stored in a cookie, making it possible to display different number of columns and using different font size on each screen at your office

All the features I'm hoping to add in the near future are listed in the "Roadmap" section of this README

# A picture is worth a thousand words

![A place to start](https://wiki.jenkins-ci.org/download/attachments/68387008/0%20A%20place%20to%20start.png)
![Adding jobs](https://wiki.jenkins-ci.org/download/attachments/68387008/1%20Adding%20jobs.png)
![Two columns view](https://wiki.jenkins-ci.org/download/attachments/68387008/2%20Two%20columns%20view.png)
![Single column view](https://wiki.jenkins-ci.org/download/attachments/68387008/3%20Single%20column%20view.png)

## TDD

If you'd like to understand more about the logic behind the Build Monitor Plugin, 
feel free to have a look at the [tests that drove the design](/src/test/java/com/smartcodeltd/jenkinsci/plugins/buildmonitor/viewmodel/JobViewTest.java).

## Known Limitations

### Browser support - Google Chrome and Opera only.

To avoid unnecessary complexity when implementing the view layer I decided to use CSS 3 flexbox.
Regretfully, the standard is only [properly supported](http://caniuse.com/flexbox) by the
[Google Chrome](https://www.google.com/intl/en/chrome/browser/) and [Opera](http://www.opera.com/download/‎).

What this means to you is that if you'd like to use the Build Monitor plugin, please make sure
that you're using Chrome or Opera to display it. Safari 7 and Internet Explorer 11 will be supporting this functionality
in the [near future](http://caniuse.com/flexbox).

### View configuration stored in a session cookie

Current implementation of Angular.js doesn't seem to allow for storing cookies with expiry time set to
[anything longer than "session"](http://stackoverflow.com/questions/12624181/angularjs-how-to-set-expiration-date-for-cookie-in-angularjs).

What this means to you is that if you change the default font size or column count settings and restart the browser,
you'll need to apply your changes again after restart. I'm working on this one, so stay tuned.

## Roadmap

1. Display what triggered the build (SCM change, another job, manual)
1. Display how long has a given job been failing for
1. Support for [Claim Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Claim+plugin)
1. Support for [Gravatar](http://gravatar.com)
1. Display parameters of parametrized jobs
1. Persist layout configuration changes in a long-lived cookie.

## License: MIT

## Open Source Software Used

* [Angular.js](http://angularjs.org/)
* [Angular-slider](http://prajwalkman.github.io/angular-slider/)
* Customised [Angular Bootstrap](http://angular-ui.github.io/bootstrap/)
* [HTML5 Boilerplate](http://html5boilerplate.com/) normalize.css
* [OpenSans font](http://www.google.com/fonts/specimen/Open+Sans) by Steve Matteson

## Inspired by

No longer maintained [Radiator View Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Radiator+View+Plugin)
