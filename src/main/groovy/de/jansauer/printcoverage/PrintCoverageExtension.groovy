package de.jansauer.printcoverage

import org.gradle.api.Project
import org.gradle.api.provider.Property

class PrintCoverageExtension {

  final Property<String> coverageType
  final Property<String> reportFile
  final Property<String> message

  PrintCoverageExtension(Project project) {
    coverageType = project.objects.property(String)
    reportFile = project.objects.property(String)
    message = project.objects.property(String)
  }
}
