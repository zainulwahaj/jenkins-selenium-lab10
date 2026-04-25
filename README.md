# Lab-10 Jenkins Pipeline - Test Stage

This project contains a Maven test suite using JUnit 5 and Selenium for login validation at `http://103.139.122.250:4000/`.

## Test case
- Test class: `src/test/java/com/lab10/LoginTest.java`
- Scenario: incorrect credentials should show `Incorrect email or password`

## Local test run
1. Install Java 17+ and Maven.
2. Run:
   - `mvn test`

## Jenkins pipeline
Pipeline is defined in `Jenkinsfile` and uses:
- Docker image: `markhobson/maven-chrome`
- Test command: `mvn -B test`
- JUnit report publication: `**/target/surefire-reports/*.xml`
- Post build email: sent to latest committer email with pass/fail summary.

## GitHub webhook setup
In repository settings, add webhook:
- Payload URL: `http://<jenkins-public-ip>:8080/github-webhook/`
- Content type: `application/json`
- Event: `Just the push event`

## Jenkins prerequisites
- Install plugins:
  - Docker Pipeline
  - Pipeline
  - Git
  - JUnit
  - Email Extension Plugin
- Configure SMTP in Manage Jenkins > System.

## Lab submission checklist
- Push code to GitHub main branch.
- Add collaborator: `qasimalik@gmail.com`.
- Trigger build with a new commit.
- Confirm JUnit report appears.
- Confirm committer receives email.
