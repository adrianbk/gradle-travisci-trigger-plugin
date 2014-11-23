package com.github.adrianbk.tcitrigger.plugin

import com.github.adrianbk.tcitrigger.task.TravisciTriggerTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class TravisciTriggerPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.extensions.create("tciTrigger", TravisciTriggerExtension)
    project.task('travisciTrigger', type: TravisciTriggerTask)
  }
}
