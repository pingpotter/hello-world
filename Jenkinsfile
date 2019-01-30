#!/usr/bin/env groovy
// The above line is used to trigger correct syntax highlighting.

pipeline {
    
    options {
        timeout(time: 20, unit: 'MINUTES')
    }

    agent { 
        label 'COMPUTE01-Agent' 
    }
    
    // triggers {
    //     cron('* * * * *')
    // }

    // this version used go mod for run test and build test
    tools {
        go 'go1.11.4'
    }
    
    environment {
        GIT_CREDS = credentials('git.ping')
        DOCKER_CREDS = credentials('dockerhub-official')
        // REGITTRY_NAME is repo on docker hub
        REGITTRY_NAME = "tncbs/billpayment-management"
    }
    
    stages {
        stage('Prepare'){
            steps {
                script{
                    // Tag version when merged to master branch
                    def rootDir = pwd()
                    def getVersion = load "${rootDir}/_dev/version.groovy"
                    env['APP_VERSION'] = getVersion.getCurrent()
                    env['GIT_TAG'] = sh(returnStdout: true, script: "git tag --sort version:refname | tail -1 | sed -e 's/v//g'").trim()
                }
                // remove go mod catch pkg
                sh "sudo rm -rf $GOPATH/pkg/mod/*"
                echo "Local version is ${env.APP_VERSION}"
                echo "Git version is ${env.GIT_TAG}"
                //stash name: "deployments", includes: "_dev/deployments/*"
            }
        }
        stage('Unit Test'){
            steps {
                    sh 'go test ./... -count=1 -coverprofile "coverage.out"'
                    sh 'go tool cover -html="coverage.out" -o "coverage.html"'
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: pwd(), reportFiles: 'coverage.html', reportName: 'Coverage Report', reportTitles: ''])
                    sh 'rm coverage.out coverage.html'
                
            }
        }
        stage('Go Build'){
            steps {
                    sh 'GOOS=linux GOARCH=amd64 CGO_ENABLED=0 go build -a -o app "cmd/main.go"'
                    sh 'rm app'
            }
        }
 
    }
    
    post{
      
        always {
            deleteDir()
        }
    }
}