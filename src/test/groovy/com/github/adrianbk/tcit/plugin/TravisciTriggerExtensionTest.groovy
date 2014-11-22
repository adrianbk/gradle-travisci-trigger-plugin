package com.github.adrianbk.tcit.plugin

import com.github.adrianbk.tcit.TestableDsl
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class TravisciTriggerExtensionTest extends Specification implements TestableDsl {
  final DefaultProject testProject = new ProjectBuilder().build()

  def "should apply the plugin and populate the extension"() {
    given:
      testProject.apply plugin: TravisciTriggerPlugin
    when:
      testProject.tciTrigger dslWithEnvVars()

    then:
      testProject.tciTrigger.baseUri == 'http://baseUri'
      testProject.tciTrigger.gitHubApiKeyVarName == 'gitHubApiKeyVarName'
      testProject.tciTrigger.gitHubRepo == 'gitHubRepo'
    and:
      def javaVar = testProject.tciTrigger.travisEnvVars[0]
      javaVar.name == 'GRADLE_HOME'
      javaVar.value == '/opt/gradle'
      javaVar.visible == true

    and:
      def gradleVar = testProject.tciTrigger.travisEnvVars[1]
      gradleVar.name == 'JAVA_HOME'
      gradleVar.value == '/opt/java'
      gradleVar.visible == true

  }
}
