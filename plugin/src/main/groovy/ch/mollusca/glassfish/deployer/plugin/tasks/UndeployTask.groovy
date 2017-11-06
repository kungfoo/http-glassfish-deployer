package ch.mollusca.glassfish.deployer.plugin.tasks

import ch.mollusca.glassfish.deployer.plugin.HttpDeployerExtension
import ch.mollusca.glassfish.deployer.plugin.HttpDeployerPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException

import static ch.mollusca.glassfish.deployer.GlassfishAdministrationResult.ExitCode.FAILURE

class UndeployTask extends DefaultTask {

    static final String TASK_NAME = 'undeploy'

    @TaskAction
    void undeploy() {
        HttpDeployerExtension extension = project.extensions.getByName(HttpDeployerPlugin.EXTENSION) as HttpDeployerExtension
        def result = HttpDeployerPlugin.configuredDeployer(extension).undeploy()

        if(result.exit_code == FAILURE) {
            throw new TaskExecutionException(this, new RuntimeException("Could not undeploy application due to:  " + result))
        }
    }
}