package com.github.adrianbk.tcitrigger.task

import com.github.adrianbk.tcitrigger.api.GitHubApiKeyProvider
import com.github.adrianbk.tcitrigger.api.TravisApi
import com.github.adrianbk.tcitrigger.api.TravisciTrigger
import com.github.adrianbk.tcitrigger.plugin.TravisciTriggerExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class TravisciTriggerTask extends DefaultTask {

  @TaskAction
  def triggerDownstream() {
    TravisciTriggerExtension travisciTriggerExtension = project.tciTrigger
    logger.info("Triggering task with extension settings: ${travisciTriggerExtension}")
    GitHubApiKeyProvider gitHubApiKeyProvider = new GitHubApiKeyProvider()
    TravisApi travisApi = new TravisApi(travisciTriggerExtension.getBaseUri(), project.name)
    TravisciTrigger travisciTrigger = new TravisciTrigger(gitHubApiKeyProvider, travisApi)
    travisciTrigger.trigger(travisciTriggerExtension)
  }
}
