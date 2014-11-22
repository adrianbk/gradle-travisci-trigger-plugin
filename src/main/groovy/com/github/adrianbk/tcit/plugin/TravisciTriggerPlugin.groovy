package com.github.adrianbk.tcit.plugin

import com.github.adrianbk.tcit.task.TravisciTriggerTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class TravisciTriggerPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.extensions.create("tciTrigger", TravisciTriggerExtension)
    project.task('travisciTrigger', type: TravisciTriggerTask)
  }
}
