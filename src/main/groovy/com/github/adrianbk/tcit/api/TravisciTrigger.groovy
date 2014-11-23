package com.github.adrianbk.tcit.api

import com.github.adrianbk.tcit.plugin.TravisciTriggerExtension

class TravisciTrigger {
  GitHubApiKeyProvider gitHubApiKeyProvider
  TravisApi travisApi

  TravisciTrigger(GitHubApiKeyProvider gitHubApiKeyProvider, TravisApi travisApi) {
    this.travisApi = travisApi
    this.gitHubApiKeyProvider = gitHubApiKeyProvider
  }

  public void trigger(TravisciTriggerExtension travisciTriggerExtension) {
    String apiKey = gitHubApiKeyProvider.getGitHubApiKey(travisciTriggerExtension)
    String token = travisApi.fetchApiToken(apiKey)
    String repositoryId = travisApi.findRepositoryId(travisciTriggerExtension.gitHubRepo, token)
    travisApi.saveOrUpdate(travisciTriggerExtension.getTravisEnvVars(), repositoryId, token)
    travisApi.restartLastBuild(repositoryId, token)
  }
}
