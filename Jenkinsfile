pipeline {
    agent {
        docker {
            image 'markhobson/maven-chrome'
            args '-u root:root -v /var/lib/jenkins/.m2:/root/.m2'
        }
    }

    stages {
        stage('Clone Repository') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }

    post {
        always {
            script {
                sh "git config --global --add safe.directory ${env.WORKSPACE}"

                def committer = sh(
                    script: "git log -1 --pretty=format:'%ae'",
                    returnStdout: true
                ).trim()

                int total = 0
                int passed = 0
                int failed = 0
                int skipped = 0
                def details = ""

                def reportFiles = findFiles(glob: '**/target/surefire-reports/*.xml')
                reportFiles.each { file ->
                    def suite = new XmlSlurper().parse(new File(file.path))
                    suite.testcase.each { tc ->
                        total++
                        def name = "${tc.@classname}.${tc.@name}"
                        if (tc.failure.size() > 0 || tc.error.size() > 0) {
                            failed++
                            details += "${name} - FAILED\n"
                        } else if (tc.skipped.size() > 0) {
                            skipped++
                            details += "${name} - SKIPPED\n"
                        } else {
                            passed++
                            details += "${name} - PASSED\n"
                        }
                    }
                }

                def emailBody = """Test Summary (Build #${env.BUILD_NUMBER})

Total Tests:   ${total}
Passed:        ${passed}
Failed:        ${failed}
Skipped:       ${skipped}

Detailed Results:
${details}
"""

                emailext(
                    to: committer,
                    subject: "Build #${env.BUILD_NUMBER} Test Results",
                    body: emailBody
                )
            }
        }
    }
}
