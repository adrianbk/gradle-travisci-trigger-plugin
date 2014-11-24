gradle-travisci-trigger-plugin
==============================

[![Build Status](https://travis-ci.org/adrianbk/gradle-travisci-trigger-plugin.svg?branch=master)](https://travis-ci.org/adrianbk/gradle-travisci-trigger-plugin.svg)

A Gradle plugin to trigger parametrized Travis CI builds belonging to another GitHub repo.

## Overview
This plugin uses the [Travis CI API](http://docs.travis-ci.com/api/) to optionally set environment variables on a 'downstream'
build before re-building it's last known build, with those variables. Consider a parent - child relationship between two GitHub
repositories, each with it's own Travis CI build. When the parent build is executed it may publish a new versioned artefact.
It may be desirable to execute the child's build using the latest version of the parents published artefact.
This plugin can be used to set the latest parent artefact version as an environment variable on the child build and then trigger
a re-buid of the child's last know build. If the child build is configured to use an environment variable to determine
the parent's artefact version, the end result is that the child build is executed with the latest version of the parents artefact.

## Prerequisites
In order for the plugin to use the Travis CI API, the 'parent' Travis CI build needs a GitHub access token.
See [here](http://docs.travis-ci.com/api/#creating-a-temporary-github-token) for details on how that Token is used by the
Travis CI API. A personal access token can be created using the
GitHub UI: [Personal settings >> Applications](https://github.com/settings/tokens/new).

Set that GitHub access token as a __SECURE__ travis variable using
the [Travis CI command line client](http://blog.travis-ci.com/2013-01-14-new-client/)

```bash
travis encrypt TRIGGER_GIT_HUB_API_KEY=<git-hub-api-key> --add
```

## Gradle Plugin Usage

```groovy
buildscript {
  repositories {
    jcenter()
  }
  dependencies {
    classpath "com.github.adrianbk:gradle-travisci-trigger-plugin:1.0.0"
  }
}
apply plugin: "com.github.adrianbk.tcitrigger"
```

## Gradle Configuration
```groovy
tciTrigger {
  gitHubRepo '<your_github_id>/<github_repo_name>' //Downstream repository
  travisVariable {
    name = "LATEST_PROJECT_VERSION"
    value = "${version}" //e.g. Parent projects software version
  }
  travisVariable {
    name = "SAMPELE_VARIABLE_2"
    value = 'SAMPLE_VALUE_2'
  }
}
```

## Tasks
This plugin adds a single task 'travisciTrigger'

```bash
./gradlew travisciTrigger -i

```
The task will add or update, as appropriate, any configured `travisVariable's` on the target build before executing that target build.

### License
[Apache-2.0] (http://www.apache.org/licenses/LICENSE-2.0.html)