package com.github.adrianbk.tcitrigger

import com.github.adrianbk.tcitrigger.plugin.TravisciTriggerPlugin

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
     jcenter()
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
   jcenter()
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
        name = "T_GRADLE_HOME"
        value = '/opt/gradle2'
        visible = true
      }
      travisVariable {
        name = "T_JAVA_HOME"
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