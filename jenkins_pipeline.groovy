pipeline {
    agent any
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
                    // Map Jenkins choice → TestNG suite file
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
                junit '**/target/surefire-reports/*.xml'
            }
        }
        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
}

