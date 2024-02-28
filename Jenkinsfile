pipeline {
    options {
        // This is required if you want to clean before build
        skipDefaultCheckout(true)
    }
    agent {
        docker {
            image 'ccitest/android:2024.01.1' //cimg/android:2024.01
        }
    }
    environment {
        NEXUS_URL = '10.16.33.232:8081'
        NEXUS_CREDENTIAL_ID = '3' 
    }
    
    stages {
        stage('SCM') {
            steps {
                checkout scm
            }
        } 
        stage('setup') {
            steps {
                sh 'chmod +x gradlew'
            }   
        }
        
        stage('Build') {
            steps {
                sh 'java -version'
                sh './gradlew assemble'
                //sh './gradlew build'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test'
            }
            
        }
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('mysonar') {
                    sh "./gradlew sonar"
                }
            }
        }
        stage('Deploy to Nexus') {
            steps {
                script {
                    nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        nexusUrl: "10.16.33.232:8081",
                        groupId: 'test',
                        version: '9',
                        repository: 'myrepo',
                        credentialsId: "${NEXUS_CREDENTIAL_ID}",
                        artifacts: [
                            [artifactId: 'movies', classifier: '', file: './app/build/outputs/apk/debug/app-debug.apk', type: 'apk']
                        ]
                    )
                }
            }
        }
    }
    /*post { 
        always { 
            cleanWs()
        }
    }*/
}
