# API Test Automation Framework

## Overview

This repository contains a test automation framework designed for API testing. The framework is built using:

- Java programming language
- RestAssured for API interactions
- TestNG for test management
- Allure for reporting.
- lombok for reducing boilerplate code
- Jackson for databinding
- Maven for dependency management
- GitHub Actions for CI/CD

The framework is designed to be modular, allowing for easy addition of new tests and configurations.

## Features

- Easy to use and extend
- Parameterized tests
- Detailed reporting with Allure
- Integration with GitHub Actions for CI/CD
- Supports data-driven testing
- Supports different HTTP methods (GET, POST, PUT, DELETE, etc.)
- Supports request and response validation

## Getting Started

### Prerequisites

- Java programming language (using 21 on my end)
- Maven
- GitHub account (for CI/CD)
- IDE (IntelliJ IDEA, Eclipse, etc.)

### Installation

1. Clone the repository:
   ```bash
   git clone
2. Navigate to the project directory:
   ```bash
   cd api-test-automation-framework
   ```
3. Open the project in your preferred IDE.
4. Build the project using Maven:
   ```bash
   mvn clean install
   ```

### Running Tests

- Please refer to the [testng.xml](src/test/resources/testng.xml) file for the test suite configuration.
- You can also run tests from the files in the [tests](src/test/java/bookstore/tests) directory.