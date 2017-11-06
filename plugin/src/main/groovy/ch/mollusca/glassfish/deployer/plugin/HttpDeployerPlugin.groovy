package ch.mollusca.glassfish.deployer.plugin

import ch.mollusca.glassfish.deployer.HttpDeployer
import ch.mollusca.glassfish.deployer.plugin.tasks.DeployTask
import ch.mollusca.glassfish.deployer.plugin.tasks.UndeployTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class HttpDeployerPlugin implements Plugin<Project> {

    static final String TASK_GROUP = "deployment"
    static final String EXTENSION = "http_deployer"
    static final String PLUGIN_ID = 'ch.mollusca.glassfish.http.deployer'

    @Override
    void apply(Project project) {
        project.extensions.add(EXTENSION, new HttpDeployerExtension())
        project.task(DeployTask.TASK_NAME, type: DeployTask, description: "Deploy a deployment archive to glassfish using http/https", group: TASK_GROUP)
        project.task(UndeployTask.TASK_NAME, type: UndeployTask, description: "Deploy a deployment archive to glassfish using http/https", group: TASK_GROUP)
    }

    static HttpDeployer configuredDeployer(HttpDeployerExtension extension) {
        def builder = new HttpDeployer.Builder()
                .applicationName(extension.applicationName)
                .contextRoot(extension.contextRoot)
                .force(extension.force)
                .host(extension.host)
                .port(extension.port)
                .scheme(extension.scheme)
                .user(extension.user)
                .password(extension.password)

        if (!extension.secure) {
            builder.insecure()
        }
        return builder.build()
    }
}
