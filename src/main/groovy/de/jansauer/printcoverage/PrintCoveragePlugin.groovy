package de.jansauer.printcoverage

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.testing.jacoco.tasks.JacocoReport

class PrintCoveragePlugin implements Plugin<Project> {

  void apply(Project target) {
    target.tasks.withType(JacocoReport).configureEach() {
      reports {
        xml.enabled true
      }
    }

    def extension = target.extensions.create('printcoverage', PrintCoverageExtension, target)
    target.tasks.register('printCoverage', PrintCoverageTask) {
      it.coverageType.set(extension.coverageType.get())
      it.dependsOn(target.tasks.withType(JacocoReport))
    }
  }
}
