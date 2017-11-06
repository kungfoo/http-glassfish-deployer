package ch.mollusca.glassfish.deployer.plugin.tasks

import ch.mollusca.glassfish.deployer.plugin.HttpDeployerExtension
import ch.mollusca.glassfish.deployer.plugin.HttpDeployerPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class UndeployTask extends DefaultTask {

    static final String TASK_NAME = 'undeploy'

    @TaskAction
    void undeploy() {
        HttpDeployerExtension extension = project.extensions.getByName("http_deployer") as HttpDeployerExtension
        HttpDeployerPlugin
                .configuredDeployer(extension)
                .undeploy()
    }

    @Override
    String getDescription() {
        return "Undeploy an application from glassfish using http/https"
    }

    @Override
    String getGroup() {
        return HttpDeployerPlugin.TASK_GROUP
    }
}