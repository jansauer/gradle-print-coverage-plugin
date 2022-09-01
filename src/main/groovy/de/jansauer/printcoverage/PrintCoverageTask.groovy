package de.jansauer.printcoverage

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Pattern

class PrintCoverageTask extends DefaultTask {

  @Input
  final Property<String> coverageType = project.objects.property(String)

  def testReportPattern = Pattern.compile("jacoco.*Report\\.xml", Pattern.CASE_INSENSITIVE)

  PrintCoverageTask() {
    setDescription('Prints code coverage for gitlab.')
    setGroup('coverage')
  }

  @TaskAction
  def printcoverage() {
    def slurper = new XmlSlurper()
    slurper.setFeature('http://apache.org/xml/features/disallow-doctype-decl', false)
    slurper.setFeature('http://apache.org/xml/features/nonvalidating/load-external-dtd', false)

    Collection<Path> jacocoTestReports = Files.find(
            Path.of(project.buildDir.path, "reports", "jacoco"),
            2,
            { path, att -> path.fileName.toString().matches(testReportPattern) })
            .collect()

    if (!jacocoTestReports.any()) {
      logger.error('Jacoco test report is missing.')
      throw new GradleException('Jacoco test report is missing.')
    }

    double maxCoverage = 0

    jacocoTestReports.each {Path jacocoTestReport ->
      def report = slurper.parse(jacocoTestReport.toFile())
      def coverageTag = report.counter.find { it.'@type' == coverageType.get() }
      double missed = coverageTag.@missed.toDouble()
      double covered = coverageTag.@covered.toDouble()
      double coverage = (100 / (missed + covered) * covered).round(2)
      // we cannot differ, if the coverage covers the same or different code, so we can only take the maximum value
      maxCoverage = Math.max(maxCoverage, coverage)
      println "Coverage ($jacocoTestReport.fileName): $coverage%"
    }
    println "Coverage: $maxCoverage%"
  }
}
