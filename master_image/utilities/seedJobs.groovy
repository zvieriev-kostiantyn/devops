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

def gitType = System.getenv('ENVIRONMENT') == 'prod' ? 'PT_TAG' : 'PT_BRANCH_TAG'

def folderName = 'DevOps'
def jobName = 'seedJobs'
def jobTitle = [folderName, jobName]

freeStyleJob(jobTitle.join('/')) {
    label('built-in')
    displayName('Seed Jobs - Global')
    description('Seed jobs available to Jenkins')
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
    scm {
        git {
        remote {
            github('zvieriev-kostiantyn/devops', 'https')
        }
        branches('${BRANCH}')
        }
    }
    steps {
        action = System.getenv('ENVIRONMENT') == 'test' ? 'IGNORE' : 'DELETE'
        dsl {
            external('jobs/*.groovy')
            removeAction(action)
            removeViewAction(action)
        }
    }
}