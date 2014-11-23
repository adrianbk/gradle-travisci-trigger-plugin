package com.github.adrianbk.tcitrigger.api

import com.github.adrianbk.tcitrigger.plugin.TravisciTriggerExtension

class GitHubApiKeyProvider {

  def String getGitHubApiKey(TravisciTriggerExtension travisciTriggerExtension) {
    System.getenv(travisciTriggerExtension.gitHubApiKeyVarName)
  }
}
