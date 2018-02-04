package de.jansauer.printcoverage

import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.FAILED
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

  def "should print the coverage for a example jacoco report"() {
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
    result.output.contains('Coverage: 3.13%')
    result.task(":printCoverage").outcome == SUCCESS
  }

  def "should print from a example class with tests"() {
    given:
    buildFile << """
        plugins {
          id 'java'
          id 'de.jansauer.printcoverage'
        }
        
        repositories {
          mavenCentral()
        }
        
        dependencies {
          testCompile "junit:junit:4.11"
        }
    """
    File classFile = testProjectDir
        .newFolder('src', 'main', 'java')
        .toPath()
        .resolve('Calculator.java')
        .toFile()
    classFile << new File("src/test/resources/Calculator.java").text
    File junitFile = testProjectDir
        .newFolder('src', 'test', 'java')
        .toPath()
        .resolve('CalculatorTest.java')
        .toFile()
    junitFile << new File("src/test/resources/CalculatorTest.java").text

    when:
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withArguments('build', 'printCoverage')
        .withPluginClasspath()
        .build()

    then:
    result.output.contains('Coverage: 100.0%')
    result.task(":printCoverage").outcome == SUCCESS
  }

  def "should fail if jacoco test report is missing"() {
    given:
    buildFile << """
        plugins {
          id 'de.jansauer.printcoverage'
        }
    """

    when:
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withArguments('printCoverage')
        .withPluginClasspath()
        .buildAndFail()

    then:
    result.output.contains('Jacoco test report is missing.')
    result.task(':printCoverage').outcome == FAILED
  }

  def "should print the configured coverage type"() {
    expect:
    buildFile << """
        plugins {
          id 'de.jansauer.printcoverage'
        }
        
        printcoverage {
          coverageType = '${type}'
        }
    """
    reportFile << new File("src/test/resources/jacocoTestReport.xml").text

    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withArguments('printCoverage')
        .withPluginClasspath()
        .build()

    result.output.contains("Coverage: ${coverage}")

    where:
    type          | coverage
    'INSTRUCTION' |   '3.13%'
    'BRANCH'      |  '20.0%'
    'LINE'        |  '40.0%'
    'COMPLEXITY'  |  '60.0%'
    'METHOD'      |  '66.67%'
    'CLASS'       | '100.0%'
  }
}
