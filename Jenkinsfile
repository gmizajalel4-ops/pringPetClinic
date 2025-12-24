pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    environment {
        IMAGE_NAME = "jalelgm/springpetclinic"
        MAVEN_OPTS = "-Djava.awt.headless=true"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/gmizajalel4-ops/pringPetClinic.git'
            }
        }

        stage('Build & Unit Tests') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('Selenium Tests') {
            steps {
                sh '''
                docker rm -f selenium 2>/dev/null || true
                docker run -d --rm -p 4444:4444 --name selenium selenium/standalone-chrome:latest
                sleep 5

                # Change to the correct directory
                cd selenium-tests2

                # List contents to verify pom.xml exists
                ls -la

                # Run Maven tests
                mvn test

                docker stop selenium
                '''
            }
        }

        stage('SonarQube Analysis') {
    steps {
        withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
            withSonarQubeEnv('SonarQube') {
                sh '''
                mvn -U org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594:sonar \
                    -DskipTests \
                    -Dsonar.projectKey=springpetclinic \
                    -Dsonar.login=$SONAR_TOKEN
                '''
            }
        }
    }
}

        stage('Docker Build') {
            steps {
                sh 'docker build --no-cache -t $IMAGE_NAME:latest .'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'USER',
                    passwordVariable: 'PASS'
                )]) {
                    sh '''
                    echo $PASS | docker login -u $USER --password-stdin
                    docker push $IMAGE_NAME:latest
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes (Minikube)') {
            steps {
                sh '''
                echo "Starting Minikube if not running..."
                minikube status || minikube start --driver=docker

                echo "Using Minikube context"
                kubectl config use-context minikube

                echo "Loading image into Minikube"
                minikube image load $IMAGE_NAME:latest

                echo "Deploying to Kubernetes"
                cd k8s
                kubectl apply -f deployment.yaml
                kubectl apply -f service.yaml

                echo "Deployment status"
                kubectl get pods
                kubectl get svc
                '''
            }
        }
    }

    post {
        success {
            echo "Pipeline SUCCESS ✅"
        }
        failure {
            echo "Pipeline FAILED ❌"
        }
    }
}
