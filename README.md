# Build Monitor Plugin

[![Gitter](https://badges.gitter.im/jenkinsci/ux-sig.svg)](https://gitter.im/jenkinsci/ux-sig?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Jenkins Plugin](https://img.shields.io/jenkins/plugin/v/build-monitor-plugin.svg)](https://plugins.jenkins.io/build-monitor-plugin)
[![Jenkins Plugin Installs](https://img.shields.io/jenkins/plugin/i/build-monitor-plugin.svg?color=blue)](https://plugins.jenkins.io/build-monitor-plugin)

## Introduction

The Build Monitor Plugin provides a highly visible dashboard showing the status of selected Jenkins jobs.

It accommodates different screen sizes and is ideal as an Extreme Feedback Device displayed on an office wall monitor.

Have a question? Perhaps it's already been answered - check the [FAQ](#faq) section below.

## Features

* Displays the status and progress of selected jobs.
* The view updates automatically every few seconds using AJAX.
* Displays the names of people who might be responsible for "breaking the build".
* Supports [Claim plugin](https://plugins.jenkins.io/claim/), so that you can see who's fixing a broken build.
* Supports [View Job Filters](https://plugins.jenkins.io/view-job-filters/), so that you can easily create Build Monitors for "slow builds", "only failing", etc.
* Supports [Build Failure Analyzer](https://plugins.jenkins.io/build-failure-analyzer/), so that you know not only *who*, but also *what* broke the build.
* Supports [Folder plugin](https://plugins.jenkins.io/cloudbees-folder/), so that you can have project- and team-specific nested Build Monitors.
* Supports [Pipeline plugin](https://plugins.jenkins.io/workflow-aggregator/), so that the currently executing stage(s) are shown.
* Supports [Theme Manager plugin](https://plugins.jenkins.io/theme-manager/), so you can easily switch between different themes.
* Supports [Customizable Header plugin](https://plugins.jenkins.io/customizable-header/), so your team can have a custom header on your Build Monitor.
* The number of columns and size of the font used is easily customisable, making it trivial to accommodate screens of different sizes.
* UI is configurable, you can have a different number of columns _and_ use a different font size on each of the screens in your office.
* Can work in a color-blind-friendly mode.

## Getting started

To create a new Build Monitor View:

1. Click the **New View** tab in Jenkins.
2. Select **Build Monitor View**.
3. Choose the jobs you want to display.

Simple, right? :-) 

You can create multiple Build Monitor Views.  
A common approach is to create one monitor per team or project.

## A picture is worth a thousand words

![Adding jobs](docs/empty.png)
![Three columns view](docs/jobs.png)
![Colour-blind mode](docs/color-blind-mode.png)

## FAQ

### How do I let my teammates know that I'm fixing a broken build?

By claiming it. Build Monitor supports Jenkins [Claim Plugin](https://plugins.jenkins.io/claim/), so once you have it installed, enable "Broken build claiming" in the "Post-build actions" of your Jenkins job. From now on you'll be able to claim any further broken builds and Build Monitor will pick it up.

You might also be interested in a [script](https://wiki.jenkins.io/display/JENKINS/Allow+broken+build+claiming+on+every+jobs) that enables claiming on all your Jenkins jobs.

### How do I know what broke the build?

Wouldn't it be great to know _what_ made your build fail? Well of course it would. Build Monitor supports Jenkins [Build Failure Analyzer Plugin](https://plugins.jenkins.io/build-failure-analyzer/) so get it, teach it, and Build Monitor will tell you what the Failure Analyzer found out.

### I have too many jobs on one screen, what should I do?

You have several options here:

1. **review the granularity of your jobs**: If you have too many low-level jobs displayed on one screen, consider consolidating them using [Promoted Builds Plugin](https://plugins.jenkins.io/promoted-builds/) or [MultiJob Plugin](https://plugins.jenkins.io/jenkins-multijob-plugin/)
1. **use job filters**: Build Monitor supports Jenkins [View Job Filters Plugin](https://plugins.jenkins.io/view-job-filters/), if you have the plugin installed
1. get a bigger screen ...

## Your feedback matters!

Found a bug or want to give feedback? Raise [an issue](https://github.com/jenkinsci/build-monitor-plugin/issues?state=open)
or submit a pull request ([start with this mini-dev guide](https://github.com/jenkinsci/build-monitor-plugin/wiki/Development-Guide), it might come in handy).

Build Monitor is continuously delivered to a Jenkins near you thanks to the time and commitment of [the author](http://smartcodeltd.co.uk/) and [the contributors](https://github.com/jenkinsci/build-monitor-plugin/graphs/contributors).
