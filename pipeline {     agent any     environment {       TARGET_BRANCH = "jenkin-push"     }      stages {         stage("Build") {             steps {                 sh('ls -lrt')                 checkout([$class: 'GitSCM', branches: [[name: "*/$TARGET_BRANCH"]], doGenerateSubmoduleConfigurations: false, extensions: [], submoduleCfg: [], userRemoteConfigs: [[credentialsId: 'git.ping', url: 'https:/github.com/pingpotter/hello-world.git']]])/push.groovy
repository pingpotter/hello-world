pipeline {
    agent any
    environment {
      TARGET_BRANCH = "jenkin-push"
      GIT_ID = "jenkin-push"
    }

    stages {
        stage("Build") {
            steps {
                sh('ls -lrt')
                checkout([$class: 'GitSCM', branches: [[name: "*/$TARGET_BRANCH"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "$GIT_ID", url: 'https://github.com/pingpotter/hello-world.git']]])
                // Create a dummy file in the repo
                sh('echo \$BUILD_NUMBER > example-\$BUILD_NUMBER.md')
            }
        }
        stage("Commit") {
            steps {
                sh('''
                    git checkout -B $TARGET_BRANCH
                    git config user.name 'my-ci-user'
                    git config user.email 'my-ci-user@users.noreply.github.example.com'
                    git add . && git commit -am "[Jenkins CI] Add build file $BUILD_NUMBER"
                ''')
            }
        }
        stage("Push") {
            environment { 
                GIT_AUTH = credentials("$GIT_ID") 
            }
            steps {
                sh('''
                    git config --local credential.helper "!f() { echo username=\\$GIT_AUTH_USR; echo password=\\$GIT_AUTH_PSW; }; f"
                    git push origin HEAD:$TARGET_BRANCH
                ''')
            }
        }
    }
    post {
      always {
        cleanWs()
      }
}
}
