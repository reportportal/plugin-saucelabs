#!groovy

node {

    load "$JENKINS_HOME/jobvars.env"

    env.JAVA_HOME = "${tool 'openjdk-11'}"
    env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"

    stage('Checkout') {
            checkout scm
    }

    stage('Build') {
        sh "./gradlew clean build -P version=${VERSION}"
    }

}
