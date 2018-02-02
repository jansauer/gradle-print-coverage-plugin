package de.jansauer.printcoverage

import org.gradle.api.Plugin
import org.gradle.api.Project

class PrintCoveragePlugin implements Plugin<Project> {

    void apply(Project target) {
        target.plugins.with {
            apply 'jacoco'
        }

        target.task('printCoverage', type: PrintCoverageTask)
    }
}
