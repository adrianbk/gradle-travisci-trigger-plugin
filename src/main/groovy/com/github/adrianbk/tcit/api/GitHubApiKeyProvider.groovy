package com.github.adrianbk.tcit.api

import com.github.adrianbk.tcit.plugin.TravisciTriggerExtension

class GitHubApiKeyProvider {

  def String getGitHubApiKey(TravisciTriggerExtension travisciTriggerExtension) {
    System.getenv(travisciTriggerExtension.gitHubApiKeyVarName)
  }
}
