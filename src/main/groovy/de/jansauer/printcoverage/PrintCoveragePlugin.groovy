package de.jansauer.printcoverage

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testing.jacoco.tasks.JacocoReport

class PrintCoveragePlugin implements Plugin<Project> {

  void apply(Project target) {
    target.tasks.withType(JacocoReport) {
      reports {
        xml.enabled true
      }
    }

    def extension = target.extensions.create('printcoverage', PrintCoverageExtension, target)

    Task task = target.tasks.create('printCoverage', PrintCoverageTask) {
      coverageType = extension.coverageType
      reportFile = extension.reportFile
      message = extension.message
    }

    target.ext.PrintCoverage = PrintCoverageTask

    task.dependsOn(target.tasks.withType(JacocoReport))
  }
}
