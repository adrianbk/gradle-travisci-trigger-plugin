package com.github.adrianbk.tcitrigger.plugin

import com.github.adrianbk.tcitrigger.TestableDsl
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll

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

  @Unroll
  def "should add to travisEnvVars"() {
    TravisciTriggerExtension extension = new TravisciTriggerExtension()
    when:
      extension.travisVariable {
        name = 'n'
        value = 'v'
        visible = true
      }

    then:
      def var = extension.travisEnvVars[0]
      var.name == 'n'
      var.value == 'v'
      var.visible
  }

  def "should override toString"() {
    def extension = new TravisciTriggerExtension()
    extension.travisEnvVars = [new EnvVar('testVarName')]
    expect:
      extension.toString() == 'com.github.adrianbk.tcitrigger.plugin.TravisciTriggerExtension(https://api.travis-ci.org/,' +
              ' TRIGGER_GIT_HUB_API_KEY, null, [com.github.adrianbk.tcitrigger.plugin.EnvVar(testVarName, null, false)])'
  }
}
