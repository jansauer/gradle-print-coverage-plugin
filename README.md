# Gradle Print Codecoverage Plugin

Scraps [jacoco](http://www.eclemma.org/jacoco/) test reports and prints the 
code coverage to the console. Tools like [GitLab](https://about.gitlab.com/)
can then parse for it for better integration.

For more information see [Gitalb test coverage parsing](https://docs.gitlab.com/ee/user/project/pipelines/settings.html#test-coverage-parsing)

## Getting Started

Add this snippet to yout build script.

```
plugins {
  id "de.jansauer.printcoverage" version "0.1.0"
} 
```

Extend your ci job configuration.

```
build-gradle:
  stage: build
  script:
    - ./gradlew build printCoverage
  coverage: '/^Coverage:\s(\d+\.\d+%)/'
```

## Tasks

* `printCoverage` The one that prints the coverage ;-) 

## Configuration

```
printcoverage {
  coverageType = 'INSTRUCTION'
}
```

* `coverageType`: Type of [coverage metric](http://www.eclemma.org/jacoco/trunk/doc/counters.html) to be printed.<br>
  One of 'INSTRUCTION', 'BRANCH', 'LINE', 'COMPLEXITY', 'METHOD' or 'CLASS'<br>
  Default: 'INSTRUCTION'

## Publishing Workflow

Every commit on this repository gets tested via [circleci](https://circleci.com/gh/jansauer/gradle-print-coverage-plugin).
Commits that are tagged with a semantic version are also automatically 
published to the gradle plugin directory as a new version.

## Contributing

Pull requests are always welcome. I'm grateful for any help or inspiration.

## License and Authors

Author: Jan Sauer
<[jan@jansauer.de](mailto:jan@jansauer.de)>
([https://jansauer.de](https://jansauer.de))

```text
Copyright 2018, Jan Sauer <jan@jansauer.de> (https://jansauer.de)

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```