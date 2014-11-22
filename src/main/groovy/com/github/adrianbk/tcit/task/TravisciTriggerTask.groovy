package com.github.adrianbk.tcit.task

import com.github.adrianbk.tcit.api.GitHubApiKeyProvider
import com.github.adrianbk.tcit.api.TravisApi
import com.github.adrianbk.tcit.api.TravisciTrigger
import com.github.adrianbk.tcit.plugin.TravisciTriggerExtension
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
