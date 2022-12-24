def job_name = 'simple-app'

pipelineJob(job_name) {
  displayName(job_name)
  logRotator {
    numToKeep(5)
  }
    parameters{
      gitParameter{
        name('GIT_BRANCH')
        defaultValue('master')
        description('Branch or tag to use for jobs')
        type('PT_BRANCH_TAG')
        branch('')
        branchFilter('origin/(.)')
        tagFilter('*')
        sortMode('DESCENDING_SMART')
        selectedValue('NONE')
        useRepository('')
        quickFilterEnabled(true)
      }
      booleanParam('FAIL_ON_XRAY', true, 'Fail the build if xray scan returns violations')
    }
  definition {
    cpsScm {
      scm {
        git {
          remote {
            github('zvieriev-kostiantyn/devops-semple')
          }
          branches('${GIT_BRANCH}')
        }
      }
      scriptPath('Jenkinsfile')
      lightweight(false)
    }
  }
}