package ch.mollusca.glassfish.deployer.plugin.tasks

import ch.mollusca.glassfish.deployer.plugin.HttpDeployerExtension
import ch.mollusca.glassfish.deployer.plugin.HttpDeployerPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class DeployTask extends DefaultTask {

    static final String TASK_NAME = "deploy"

    @Input
    HttpDeployerExtension extension

    @TaskAction
    void deploy() {
        HttpDeployerPlugin
                .configuredDeployer(extension)
                .deploy(new File(extension.deploymentArchive))
    }
}
