package com.github.adrianbk.tcit.api

import com.github.adrianbk.tcit.plugin.TravisciTriggerExtension
import spock.lang.Specification

class GitHubApiKeyProviderTest extends Specification {

  def "should find api key"() {
    setup:
      TravisciTriggerExtension travisciTriggerExtension = Mock {
        getGitHubApiKeyVarName() >> 'PATH'
      }

    expect:
      GitHubApiKeyProvider gitHubApiKeyProvider = new GitHubApiKeyProvider()
      gitHubApiKeyProvider.getGitHubApiKey(travisciTriggerExtension)
  }
}
