package de.jansauer.printcoverage

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension

class PrintCoverageExtension {

  final Property<File> reportFile

  final Property<String> coverageType

  PrintCoverageExtension(Project project) {
//    JacocoPluginExtension jacocoPluginExtension = project.extensions.findByName("jacoco")
    reportFile = project.objects.property(File)
//    reportFile.set(jacocoPluginExtension.reportsDir)
    reportFile.set(new File("${project.buildDir}/reports/jacoco/test/jacocoTestReport.xml"))
    coverageType = project.objects.property(String)
    coverageType.set('INSTRUCTION')
  }
}
