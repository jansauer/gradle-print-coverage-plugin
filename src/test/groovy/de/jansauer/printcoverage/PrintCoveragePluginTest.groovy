package de.jansauer.printcoverage

import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class PrintCoveragePluginTest extends Specification {

  @Rule
  final TemporaryFolder testProjectDir = new TemporaryFolder()

  File buildFile

  File reportFile

  def setup() {
    testProjectDir.create()
    buildFile = testProjectDir.newFile('build.gradle')
    reportFile = testProjectDir
        .newFolder('build', 'reports', 'jacoco', 'test')
        .toPath()
        .resolve('jacocoTestReport.xml')
        .toFile()
  }

  def "hello world task prints hello world"() {
    given:
    buildFile << """
        plugins {
          id 'de.jansauer.printcoverage'
        }
    """
    reportFile << new File("src/test/resources/jacocoTestReport.xml").text

    when:
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withArguments('printCoverage')
        .withPluginClasspath()
        .build()

    then:
    result.output.contains('Coverage: 0.0%')
    result.task(":printCoverage").outcome == SUCCESS
  }
}
