package com.github.adrianbk.tcitrigger.api

import com.github.adrianbk.tcitrigger.plugin.TravisciTriggerExtension
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
