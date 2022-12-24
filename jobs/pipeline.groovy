def job_name = 'simple-java-maven-app'

pipelineJob(job_name) {
  label('built-in')
  displayName(job_name)
  description('simple-pipeline using sh')
  logRotator {
    numToKeep(5)
    artifactNumToKeep(1)
  }
  triggers {
      hudsonStartupTrigger {
      quietPeriod('5')
      runOnChoice('ON_CONNECT')
      label('built-in')
      nodeParameterName('')
    }
  }
  parameters {
    gitParameter {
      name('BRANCH')
      defaultValue('master')
      description('Branch or tag to use for seedJobs')
      type(gitType)
      branch('')
      branchFilter('origin/(.)')
      tagFilter('')
      sortMode('DESCENDING_SMART')
      selectedValue('NONE')
      useRepository('')
      quickFilterEnabled(true)
    }
  }
  definition {
    cpsScm {
      scm {
        git {
          remote {
            github('zvieriev-kostiantyn/devops-semple')
          }
          branches('${BRANCH}')
        }
      }
      scriptPath('Jenkinsfile')
      lightweight(false)
    }
  }
}