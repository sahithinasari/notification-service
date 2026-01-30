pipeline {
    agent any

    environment {
        COMPOSE_FILE = "docker-compose.yml"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh '''
                  chmod +x mvnw || true
                  ./mvnw clean test package
                '''
            }
        }

        stage('Stop Existing Stack') {
            steps {
                sh '''
                  docker-compose down || true
                '''
            }
        }

        stage('Start Stack') {
            steps {
                sh '''
                  docker-compose up -d --build
                '''
            }
        }
    }
}