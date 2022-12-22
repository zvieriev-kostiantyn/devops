package javaposse.jobdsl.plugin

import hudson.Extension
import jenkins.model.GlobalConfiguration
import jenkins.model.GlobalConfigurationCategory
import net.sf.json.JSONObject
import org.kohsuke.stapler.StaplerRequest

@Extension
class GlobalJobDslSecurityConfiguration extends GlobalConfiguration {

    GlobalConfigurationCategory getCategory() {
        GlobalConfigurationCategory.get(GlobalConfigurationCategory.Security)
    }

    boolean useScriptSecurity = true

    GlobalJobDslSecurityConfiguration() {
        load()
    }

    @Override
    boolean configure(StaplerRequest req, JSONObject json) {
        useScriptSecurity = json.has('useScriptSecurity')
        save()
        true
    }
}
def job_name = 'simple-java-maven-app'

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
        branchFilter('origin/(.*)')
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
            github('jenkins-docs/simple-java-maven-app')
          }
          branches('${GIT_BRANCH}')
        }
      }
      scriptPath('jenkins/Jenkinsfile')
      lightweight(false)
    }
  }
}