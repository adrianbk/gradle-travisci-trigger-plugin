package com.github.adrianbk.tcit.plugin

import groovy.transform.ToString

@ToString
class TravisciTriggerExtension {
  String baseUri = 'https://api.travis-ci.org/'
  String gitHubApiKeyVarName = 'TRIGGER_GIT_HUB_API_KEY'
  String gitHubRepo
  List<EnvVar> travisEnvVars = []

  def travisVariable(Closure closure) {
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    EnvVar envVar = new EnvVar()
    closure.delegate = envVar
    travisEnvVars << envVar
    closure()
  }
}
