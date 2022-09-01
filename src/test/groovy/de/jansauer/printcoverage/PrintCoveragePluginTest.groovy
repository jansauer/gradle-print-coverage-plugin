package de.jansauer.printcoverage

import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.UnexpectedBuildFailure
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class PrintCoveragePluginTest extends Specification {

  final static String[] SUPPORTED_GRADLE_VERSIONS = ['4.10', '4.10.1', '4.10.2', '5.0', '4.10.3', '5.1', '5.1.1']

  TemporaryFolder temporaryFolder

  File propertiesFile

  File buildFile

  File reportFile

  def setup() {
    temporaryFolder = new TemporaryFolder()
    temporaryFolder.create()
    propertiesFile = temporaryFolder.newFile('gradle.properties')
    propertiesFile << """
      org.gradle.daemon=false
      org.gradle.jvmargs=-Xmx512m -Xms256m
    """
    buildFile = temporaryFolder.newFile('build.gradle')
    reportFile = temporaryFolder
        .newFolder('build', 'reports', 'jacoco', 'test')
        .toPath()
        .resolve('jacocoTestReport.xml')
        .toFile()
  }

  def "should fail if the jacoco plugin is missing"() {
    given:
    buildFile << """
        plugins {
          id 'de.jansauer.printcoverage'
        }
    """

    when:
    GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(temporaryFolder.root)
        .withArguments('printCoverage')
        .withPluginClasspath()
        .build()

    then:
    thrown UnexpectedBuildFailure

    where:
    gradleVersion << SUPPORTED_GRADLE_VERSIONS
  }

  def "should print the coverage for a example jacoco report"() {
    given:
    buildFile << """
        plugins {
          id 'jacoco'
          id 'de.jansauer.printcoverage'
        }
    """
    reportFile << new File("src/test/resources/jacocoTestReport.xml").text

    when:
    def result = GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(temporaryFolder.root)
        .withArguments('printCoverage')
        .withPluginClasspath()
        .build()

    then:
    result.output.contains('Coverage: 3.13%')
    result.task(":printCoverage").outcome == SUCCESS

    where:
    gradleVersion << SUPPORTED_GRADLE_VERSIONS
  }

  def "should print from a example class with tests"() {
    given:
    buildFile << """
        plugins {
          id 'java'
          id 'jacoco'
          id 'de.jansauer.printcoverage'
        }
        
        repositories {
          mavenCentral()
        }
        
        dependencies {
          testCompile 'junit:junit:4.12'
        }
    """
    File classFile = temporaryFolder
        .newFolder('src', 'main', 'java', 'sample')
        .toPath()
        .resolve('Calculator.java')
        .toFile()
    classFile << new File("src/test/resources/Calculator.java").text
    File junitFile = temporaryFolder
        .newFolder('src', 'test', 'java', 'sample')
        .toPath()
        .resolve('CalculatorTest.java')
        .toFile()
    junitFile << new File("src/test/resources/CalculatorTest.java").text

    when:
    def result = GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(temporaryFolder.root)
        .withArguments('build', 'printCoverage')
        .withPluginClasspath()
        .build()

    then:
    result.output.contains('Coverage: 79.49%')
    result.task(":printCoverage").outcome == SUCCESS

    where:
    gradleVersion << SUPPORTED_GRADLE_VERSIONS.findAll { !(it =~ /4\.*/) }
  }

  def "should print from a example class with tests and integration tests"() {
    given:
    buildFile << """
        plugins {
          id 'java'
          id 'jacoco'
          id 'de.jansauer.printcoverage'
          id 'org.unbroken-dome.test-sets' version '2.2.1'
        }
        
        testSets {
            integration
        }
        
        repositories {
          mavenCentral()
        }
        
        dependencies {
          testCompile 'junit:junit:4.12'
        }
    """
    File classFile = temporaryFolder
        .newFolder('src', 'main', 'java', 'sample')
        .toPath()
        .resolve('Calculator.java')
        .toFile()
    classFile << new File("src/test/resources/Calculator.java").text
    File junitFile = temporaryFolder
        .newFolder('src', 'test', 'java', 'sample')
        .toPath()
        .resolve('CalculatorTest.java')
        .toFile()
    junitFile << new File("src/test/resources/CalculatorTest.java").text
    File integrationFile = temporaryFolder
        .newFolder('src', 'integration', 'java', 'sample')
        .toPath()
        .resolve('CalculatorIT.java')
        .toFile()
    integrationFile << new File("src/test/resources/CalculatorIT.java").text

    when:
    def result = GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(temporaryFolder.root)
        .withArguments('build', 'integration', 'printCoverage')
        .withPluginClasspath()
        .build()

    then:
    result.output.contains('Coverage (jacocoIntegrationReport.xml): 100.0%')
    result.output.contains('Coverage (jacocoTestReport.xml): 79.49%')
    result.output.contains('Coverage: 100.0%')
    result.task(":printCoverage").outcome == SUCCESS

    where:
    gradleVersion << SUPPORTED_GRADLE_VERSIONS.findAll { !(it =~ /4\.*/) }
  }

  def "should print from a example class with tests with gradle 4.x"() {
    given:
    buildFile << """
        plugins {
          id 'java'
          id 'jacoco'
          id 'de.jansauer.printcoverage'
        }
        
        repositories {
          mavenCentral()
        }
        
        dependencies {
          testCompile 'junit:junit:4.12'
        }
        
        // default version does not work with jdk11
        // https://github.com/vaskoz/core-java9-impatient/issues/11
        // https://github.com/jacoco/jacoco/releases/tag/v0.8.2
        jacoco {
          toolVersion = "0.8.2"
        }
    """
    File classFile = temporaryFolder
        .newFolder('src', 'main', 'java', 'sample')
        .toPath()
        .resolve('Calculator.java')
        .toFile()
    classFile << new File("src/test/resources/Calculator.java").text
    File junitFile = temporaryFolder
        .newFolder('src', 'test', 'java', 'sample')
        .toPath()
        .resolve('CalculatorTest.java')
        .toFile()
    junitFile << new File("src/test/resources/CalculatorTest.java").text

    when:
    def result = GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(temporaryFolder.root)
        .withArguments('build', 'printCoverage')
        .withPluginClasspath()
        .build()

    then:
    result.output.contains('Coverage: 79.49%')
    result.task(":printCoverage").outcome == SUCCESS

    where:
    gradleVersion << SUPPORTED_GRADLE_VERSIONS.findAll { it =~ /4\.*/ }
  }






  def "should fail if jacoco test report is missing"() {
    given:
    buildFile << """
        plugins {
          id 'jacoco'
          id 'de.jansauer.printcoverage'
        }
    """

    when:
    def result = GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(temporaryFolder.root)
        .withArguments('printCoverage')
        .withPluginClasspath()
        .buildAndFail()

    then:
    result.output.contains('Jacoco test report is missing.')
    result.task(':printCoverage').outcome == FAILED

    where:
    gradleVersion << SUPPORTED_GRADLE_VERSIONS
  }

  def "should print the configured coverage type"() {
    expect:
    buildFile << """
        plugins {
          id 'jacoco'
          id 'de.jansauer.printcoverage'
        }
        
        printcoverage {
          coverageType = '${type}'
        }
    """
    reportFile << new File("src/test/resources/jacocoTestReport.xml").text

    def result = GradleRunner.create()
        .withProjectDir(temporaryFolder.root)
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
