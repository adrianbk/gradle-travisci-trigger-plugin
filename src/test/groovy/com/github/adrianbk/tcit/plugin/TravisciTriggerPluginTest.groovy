package com.github.adrianbk.tcit.plugin

import org.gradle.api.Project
import spock.lang.Specification

class TravisciTriggerPluginTest extends Specification {
  def "should add tasks and extensions"() {
    given:
      Project project = new org.gradle.testfixtures.ProjectBuilder().build()

    when:
      new TravisciTriggerPlugin().apply(project)

    then:
      project.getExtensions().tciTrigger
      project.tasks.travisciTrigger
  }
}
