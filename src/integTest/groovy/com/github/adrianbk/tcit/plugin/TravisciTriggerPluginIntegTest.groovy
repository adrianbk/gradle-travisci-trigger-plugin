package com.github.adrianbk.tcit.plugin

import com.github.adrianbk.tcit.GradleIntegTest
import com.github.adrianbk.tcit.TestableDsl
import spock.lang.IgnoreIf
import spock.lang.Specification

class TravisciTriggerPluginIntegTest extends Specification implements TestableDsl, GradleIntegTest {
  public static final String TEST_BUILD_FILE = 'build/integTest/build.gradle'

  @IgnoreIf({ System.env['TRIGGER_GIT_HUB_API_KEY'] ? false : true })
  def "should run the trigger task"() {
    given:
      File buildFile = createBuildFile(TEST_BUILD_FILE)
      buildFile << buildScriptBlock()
      buildFile << applyPlugin()
      buildFile << extensionConfiguration('TRIGGER_GIT_HUB_API_KEY')
    expect:
      runTasks(buildFile.getParentFile(), 'travisciTrigger')
  }
}
