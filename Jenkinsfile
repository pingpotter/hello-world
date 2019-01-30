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
    }
    
    stages {
        stage('Prepare'){
            steps {
                script{
                    // Tag version when merged to master branch
                    def rootDir = pwd()
                    def getVersion = load "${rootDir}/version.groovy"
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
    }
    
    post{
      
        always {
            deleteDir()
        }
    }
}