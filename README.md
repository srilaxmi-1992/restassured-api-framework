# 🚀 REST Assured API Automation Framework

A robust, scalable, and extensible API automation framework built using 
**REST Assured**, **TestNG**, **Maven**, and **Log4j**, now enhanced with **Allure Reporting** 
and fully executable through **Jenkins CI/CD Pipeline**.

---

## ✨ Features

- **REST Assured** for fluent and powerful REST & GraphQL API testing
- **TestNG** for test orchestration, grouping, and parallel execution
- **Maven** for dependency management and build lifecycle
- **Log4j** integrated for detailed logging
- **Allure Reporting** for rich, interactive test reports
- **Supports parallel execution** (recent enhancement)
- **Modular folder structure** for easy maintenance
- **Easily extendable** for new endpoints and test suites
- **CI/CD ready** with Jenkins Pipeline support

---

## 🧰 Tech Stack

| Technology       | Purpose                               |
|------------------|----------------------------------------|
| **Java**         | Programming language                   |
| **REST Assured** | API testing library                    |
| **TestNG**       | Test runner + parallel execution       |
| **Maven**        | Build + dependency management          |
| **Log4j**        | Logging framework                      |
| **Allure**       | Rich HTML reporting                    |

---

## ▶️ Running Tests Locally

### Run full test suite
```bash

    mvn clean test -DsuiteXmlFile=testng.xml
``` 
### Run smoke test suite
```bash

    mvn clean test -DsuiteXmlFile=testng-smoke.xml
``` 
### Run regression test suite
```bash

    mvn clean test -DsuiteXmlFile=testng-regression.xml
``` 
To view the Allure report locally

1. Install Allure on your machine. 
2. Allure bin folder to your system PATH environment variable.

### Generate Allure report locally

```bash
allure serve target/allure-results
``` 
🏗️ Jenkins CI/CD Pipeline Support
This project is fully integrated with Jenkins Pipeline, enabling automated:

✔️ Prerequisites in Jenkins
Before running the pipeline:

1. Install Allure Jenkins Plugin
2. Manage Jenkins → Manage Plugins → Available → search Allure Jenkins Plugin
3. Configure Allure Tool
4. Manage Jenkins → Global Tool Configuration
5. Add Allure Commandline
6. Name it: allure
7. Jenkins will use this tool in the pipeline
8. Create Jenkins pipeline with jenkins_pipeline.groovy script.

## 👩‍💻 Author

Srilaxmi Amaraneni
