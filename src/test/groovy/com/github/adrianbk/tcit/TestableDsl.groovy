package com.github.adrianbk.tcit

import com.github.adrianbk.tcit.plugin.TravisciTriggerPlugin

trait TestableDsl {
  Closure dslWithEnvVars() {
    def cls = {
      baseUri 'http://baseUri'
      gitHubApiKeyVarName 'gitHubApiKeyVarName'
      gitHubRepo 'gitHubRepo'
      travisVariable {
        name = "GRADLE_HOME"
        value = '/opt/gradle'
        visible = true
      }
      travisVariable {
        name = "JAVA_HOME"
        value = '/opt/java'
        visible = true
      }
    }
    return cls
  }

  def buildScriptBlock = {
    """
buildscript {
    repositories{
      mavenLocal()
    }
    dependencies {
        //Required for testing
        classpath 'org.codehaus.groovy.modules.http-builder:http-builder:0.7.1'
        classpath 'commons-collections:commons-collections:3.2.1@jar'

        //Add source to the classpath
        classpath files('../classes/main')
    }
}
apply plugin: 'java'
repositories {
    mavenLocal()
}
"""
  }

  def extensionConfiguration(String gitHubApiKeyVarName) {
    """
tciTrigger{
      baseUri 'https://api.travis-ci.org/'
      gitHubApiKeyVarName "${gitHubApiKeyVarName}"
      gitHubRepo 'adrianbk/travisci-downstream'
      travisVariable {
        name = "GRADLE_HOME"
        value = '/opt/gradle2'
        visible = true
      }
      travisVariable {
        name = "JAVA_HOME"
        value = '/opt/java2'
        visible = true
      }
    }
"""
  }

  def applyPlugin = {
    """apply plugin: ${TravisciTriggerPlugin.name}

"""
  }
}