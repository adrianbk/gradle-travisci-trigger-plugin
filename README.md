gradle-travisci-trigger-plugin
==============================

[![Build Status](https://travis-ci.org/adrianbk/gradle-travisci-trigger-plugin.svg?branch=master)](https://travis-ci.org/adrianbk/gradle-travisci-trigger-plugin.svg)

A Gradle plugin to trigger parametrized Travis CI builds belonging to other GitHub repositories.

## Overview
This plugin uses the [Travis CI API](http://docs.travis-ci.com/api/) to optionally set environment variables on, and trigger a
build of, another 'downstream' TravisCI build.

Consider two GitHub projects: 'upstream' and 'downstream', each with it's own Travis CI build. The 'downstream' build depends on
a versioned software artefact produced by 'upstream'. On a successful build of 'upstream', it may be desirable to execute
'downstream' using the latest version of upstream's published artefact. This plugin can be used to set upstreams latest version
number as an environment variable on downstream before re-building downstreams last know build.

## Prerequisites
In order for the plugin to use the Travis CI API, the 'upstream' Travis CI build needs a GitHub access token.
See [here](http://docs.travis-ci.com/api/#creating-a-temporary-github-token) for details on how that Token is used by the
Travis CI API. A personal access token can be created using the
GitHub UI: [Personal settings >> Applications](https://github.com/settings/tokens/new).

Set that GitHub access token as a __SECURE__ travis variable using
the [Travis CI command line client](http://blog.travis-ci.com/2013-01-14-new-client/)

```bash
travis encrypt TRIGGER_GIT_HUB_API_KEY=<git-hub-api-key> --add
```

There must be at least one executed build on the downstream/target Travis CI build.

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

## Minimum Gradle Configuration
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

## Optional configuration
```groovy

tciTrigger {
  baseUri 'https://api.travis-ci.org/'            //Not required defaults to https://api.travis-ci.org/
  gitHubApiKeyVarName "OTHER_GIT_HUB_API_KEY"     //Not required defaults to TRIGGER_GIT_HUB_API_KEY
  gitHubRepo '<your_github_id>/<github_repo_name>'
  travisVariable {
    name = "MY_VAR"
    value = "my_vars_value"
    visible = true                                //Not required - defaults to false(secure)
  }
  travisVariable {
    name = "MY_VAR_2"
    value = "my_vars_value_2"
    visible = true                                //Not required - defaults to false(secure)
  }
}
```

## Tasks
This plugin adds a single task 'travisciTrigger'

```bash
./gradlew travisciTrigger -i

```
- The task will add or update, as appropriate, any configured `travisVariable's` on the target build before executing that target build.

### License
[Apache-2.0] (http://www.apache.org/licenses/LICENSE-2.0.html)