pipeline {
    agent any

    tools {
        allure 'allure'   // adding allure tool name created in jenkins
    }

    parameters {
        choice(
                name: 'SUITE',
                choices: ['smoke', 'regression', 'full'],
                description: 'Select TestNG suite to run'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                cleanWs()
                git branch: 'main',
                        url: 'https://github.com/srilaxmi-1992/restassured-api-framework.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install -DskipTests'
            }
        }

        stage('Test') {
            steps {
                script {
                    def suiteMap = [
                            smoke: 'testng-smoke',
                            regression: 'testng-regression',
                            full: 'testng'
                    ]
                    def selectedSuite = suiteMap[params.SUITE]
                    echo "Running suite: ${selectedSuite}"
                    bat "mvn test -Dsuite=${selectedSuite}"
                }
            }
        }

        stage('Publish Reports') {
            steps {
                // for testng report
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    // report after the pipeline finished
    post {
        always {
            allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'target/allure-results']]
            ])
        }
        success {
            echo '✅ Tests PASSED'
        }
        failure {
            echo '❌ Tests FAILED — check Allure report'
        }
    }
}