package de.jansauer.printcoverage

import org.gradle.api.Project
import org.gradle.api.provider.Property

class PrintCoverageExtension {

  final Property<String> coverageType

  PrintCoverageExtension(Project project) {
    coverageType = project.objects.property(String)
    coverageType.set('INSTRUCTION')
  }
}
