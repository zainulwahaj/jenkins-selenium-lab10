# Jenkins configuration steps

## 1) Install required plugins
- Pipeline
- Git
- GitHub Integration
- Docker Pipeline
- JUnit
- Email Extension Plugin

## 2) Configure SMTP in Jenkins
Open `Manage Jenkins` -> `System` and set:
- `SMTP server` (example: `smtp.gmail.com`)
- `Default user e-mail suffix` (optional)
- `Use SMTP Authentication` with username/password or app password
- `Use SSL` for port `465` or `Use TLS` for port `587`
- Save and use `Test configuration by sending test e-mail`

## 3) Create pipeline job
1. New Item -> Pipeline -> name: `lab10-jenkins-selenium`.
2. Pipeline definition: `Pipeline script from SCM`.
3. SCM: Git.
4. Repository URL: `https://github.com/zainulwahaj/jenkins-selenium-lab10.git`.
5. Branch: `*/main`.
6. Script Path: `Jenkinsfile`.

## 4) Enable GitHub webhook trigger
In job config, enable:
- `GitHub hook trigger for GITScm polling`

In GitHub repository webhook settings:
- URL: `http://103.139.122.250:8080/github-webhook/`
- Content type: `application/json`
- Event: `Just the push event`

## 5) Validate complete flow
1. Commit a small change in repo and push.
2. Confirm Jenkins build is triggered by webhook.
3. Confirm test report is published in build.
4. Confirm committer receives summary email.
