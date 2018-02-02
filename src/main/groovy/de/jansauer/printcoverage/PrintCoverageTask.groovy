package de.jansauer.printcoverage

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class PrintCoverageTask extends DefaultTask {

    public PrintCoverageTask() {
        setDescription('Prints code coveage for gitlab.')
        setGroup('Coverage')
//        setDependsOn(project.task.jacocoTestReport)
    }

    @TaskAction
    def printcoverage() {
        def slurper = new XmlSlurper()
        slurper.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
        slurper.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        def report = slurper.parse(new File("${project.buildDir}/reports/jacoco/test/jacocoTestReport.xml"))
        double missed = report.counter[0].@missed.toDouble()
        double covered = report.counter[0].@covered.toDouble()
        def coverage = (100 / (missed + covered) * covered).round(2)
        println 'Coverage: ' + coverage + '%'
    }
}
