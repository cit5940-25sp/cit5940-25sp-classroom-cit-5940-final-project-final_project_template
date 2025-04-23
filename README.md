 # Final Project

## Development Setup

IntelliJ comes with Maven bundled so you can use the buttons in the UI and IntelliJ commands to run things.

Otherwise, read on.

Install Maven:
- If you have a package manager: https://maven.apache.org/install.html
- If you do not, why don't you? Also: https://maven.apache.org/download.cgi

To compile:
```sh
mvn compile
```

To test:
```sh
mvn test
```

To run:
```sh
mvn exec:java
```