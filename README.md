🚀 Features

REST Assured for fluent and powerful API testing with REST API and GraphQL API

TestNG for test orchestration and parallel execution

Maven for dependency management and build lifecycle

Log4j integrated for detailed logging

Modular folder structure for easy maintenance

Supports parallel test execution (recent enhancement)

Easily extendable for new endpoints and test suites

| Technology       |           Purpose |

| **Java**         |Programming language              |
| **REST Assured** | API testing library              |
| **TestNG**       | Test runner + parallel execution |
| **Maven**        | Build + dependency management    |
| **Log4j**        | Logging framework                |

------------------------------------------------------------------------


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


## 👩‍💻 Author

Srilaxmi Amaraneni
