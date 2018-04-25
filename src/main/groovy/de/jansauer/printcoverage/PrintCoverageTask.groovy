package de.jansauer.printcoverage

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class PrintCoverageTask extends DefaultTask {

  @Input
  final Property<String> coverageType = project.objects.property(String)

  PrintCoverageTask() {
    setDescription('Prints code coverage for gitlab.')
    setGroup('coverage')
  }

  @TaskAction
  def printcoverage() {
    def slurper = new XmlSlurper()
    slurper.setFeature('http://apache.org/xml/features/disallow-doctype-decl', false)
    slurper.setFeature('http://apache.org/xml/features/nonvalidating/load-external-dtd', false)

    File jacocoTestReport = new File("${project.buildDir}/reports/jacoco/test/jacocoTestReport.xml")
    if (!jacocoTestReport.exists()) {
      logger.error('Jacoco test report is missing.')
      throw new GradleException('Jacoco test report is missing.')
    }

    def report = slurper.parse(jacocoTestReport)
    double missed = report.counter.find { it.'@type' == coverageType.get() }.@missed.toDouble()
    double covered = report.counter.find { it.'@type' == coverageType.get() }.@covered.toDouble()
    def coverage = (100 / (missed + covered) * covered).round(2)
    println 'Coverage: ' + coverage + '%'
  }
}
