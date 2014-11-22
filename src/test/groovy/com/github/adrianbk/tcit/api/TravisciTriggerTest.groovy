package com.github.adrianbk.tcit.api

import com.github.adrianbk.tcit.plugin.TravisciTriggerExtension
import spock.lang.Specification

class TravisciTriggerTest extends Specification {
  public static final String GIT_KEY = 'gitKey'
  public static final String TRAVIS_TOKEN = 'travis_token'
  public static final String REPO_ID = '12345'
  public static final String GIT_HUB_REPO_SLUG = 'arianbk/travisci-downstream'

  def "should follow trigger steps"() {
    setup:
      TravisciTriggerExtension travisciTriggerExtension = Mock() {
        1 * getGitHubRepo() >> GIT_HUB_REPO_SLUG
      }
      GitHubApiKeyProvider gitHubApiKeyProvider = Mock()
      TravisApi travisApi = Mock()
      TravisciTrigger travisciTrigger = new TravisciTrigger(gitHubApiKeyProvider, travisApi)

    when:
      travisciTrigger.trigger(travisciTriggerExtension)
    then:
      1 * gitHubApiKeyProvider.getGitHubApiKey(travisciTriggerExtension) >> GIT_KEY
      1 * travisApi.fetchApiToken(GIT_KEY) >> TRAVIS_TOKEN
      1 * travisApi.findRepositoryId(GIT_HUB_REPO_SLUG, TRAVIS_TOKEN) >> REPO_ID
      1 * travisApi.saveOrUpdate(travisciTriggerExtension.travisEnvVars, REPO_ID, TRAVIS_TOKEN)
  }


}
