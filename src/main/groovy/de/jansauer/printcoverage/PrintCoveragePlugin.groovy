package de.jansauer.printcoverage

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport

class PrintCoveragePlugin implements Plugin<Project> {

  void apply(Project target) {
    target.plugins.apply(JacocoPlugin)
    target.tasks.withType(JacocoReport) {
      reports {
        xml.enabled true
      }
    }

    def extension = target.extensions.create('printcoverage', PrintCoverageExtension, target)
    Task task = target.tasks.create('printCoverage', PrintCoverageTask) {
      coverageType = extension.coverageType
    }
    task.description = 'Prints code coverage for gitlab.'
    task.group = 'Coverage'
    task.dependsOn(target.tasks.withType(JacocoReport))
  }
}
