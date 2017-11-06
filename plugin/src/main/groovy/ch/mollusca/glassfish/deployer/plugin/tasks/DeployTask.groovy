package ch.mollusca.glassfish.deployer.plugin.tasks

import ch.mollusca.glassfish.deployer.plugin.HttpDeployerExtension
import ch.mollusca.glassfish.deployer.plugin.HttpDeployerPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.internal.TaskInputsInternal
import org.gradle.api.tasks.TaskAction

class DeployTask extends DefaultTask {

    static final String TASK_NAME = "deploy"

    @TaskAction
    void deploy() {
        HttpDeployerExtension extension = project.extensions.getByName("http_deployer") as HttpDeployerExtension
        HttpDeployerPlugin
                .configuredDeployer(extension)
                .deploy(new File(extension.deploymentArchive))
    }

    @Override
    TaskInputsInternal getInputs() {
        return super.getInputs()
    }

    @Override
    String getDescription() {
        return "Deploy a deployment archive to glassfish using http/https"
    }

    @Override
    String getGroup() {
        return HttpDeployerPlugin.TASK_GROUP
    }
}
