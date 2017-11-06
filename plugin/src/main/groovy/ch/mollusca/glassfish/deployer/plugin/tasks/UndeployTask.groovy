package ch.mollusca.glassfish.deployer.plugin.tasks

import ch.mollusca.glassfish.deployer.plugin.HttpDeployerExtension
import ch.mollusca.glassfish.deployer.plugin.HttpDeployerPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class UndeployTask extends DefaultTask {

    static final String TASK_NAME = 'undeploy'

    @Input
    HttpDeployerExtension extension

    @TaskAction
    void undeploy() {
        HttpDeployerPlugin.configuredDeployer(extension).undeploy()
    }
}