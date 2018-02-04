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
    - ./gradlew build
  coverage: '/^Coverage:\s(\d+\.\d+%)/'
``` 
