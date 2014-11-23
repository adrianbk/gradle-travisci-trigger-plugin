package com.github.adrianbk.tcitrigger

import org.gradle.api.GradleException
import org.gradle.tooling.BuildLauncher
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.gradle.tooling.model.GradleProject

trait GradleIntegTest {

  String runTasks(File projectDir, String... tasks) {
    GradleConnector gradleConnector = GradleConnector.newConnector()
    gradleConnector.forProjectDirectory(projectDir)
    ProjectConnection connection = gradleConnector.connect()

    try {
      BuildLauncher builder = connection.newBuild()
      OutputStream outputStream = new ByteArrayOutputStream()
      builder.setStandardOutput(outputStream)
      builder.withArguments("-i")
      builder.forTasks(tasks).run()
      GradleProject gradleProject = connection.getModel(GradleProject)
      return new String(outputStream.toByteArray(), 'UTF-8')
    }
    finally {
      connection?.close()
    }
  }

  File createBuildFile(String fileName) {
    File file = new File(fileName)
    if (file.exists()) {
      if (!file.delete()) {
        throw new GradleException("Could not delete integTest build file ${fileName}")
      }
    } else {
      file.getParentFile().mkdirs()
      if (!new File(fileName).createNewFile()) {
        throw new GradleException("Could not create integTest build file ${fileName}")
      }
    }
    file
  }
}
