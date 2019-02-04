package de.jansauer.printcoverage

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class PrintCoveragePlugin implements Plugin<Project> {

  void apply(Project target) {


    PrintCoverageExtension extension = target.getExtensions().create('printcoverage', PrintCoverageExtension.class, target)

    target.tasks.register('printCoverage', PrintCoverageTask.class, new Action<PrintCoverageTask>() {
      void execute(PrintCoverageTask printCoverageTask) {
        printCoverageTask.reportFile.set(extension.reportFile)
        printCoverageTask.coverageType.set(extension.coverageType)
        //printCoverageTask.dependsOn(target.tasks.named("jacocoTestReport").name)
        printCoverageTask.dependsOn(target.tasks.withType(JacocoReport.class))
      }
    })

//    target.tasks.withType(JacocoReport.class).configureEach {
//      it.reports {
//        xml.enabled true
//      }
//    }
  }
}
