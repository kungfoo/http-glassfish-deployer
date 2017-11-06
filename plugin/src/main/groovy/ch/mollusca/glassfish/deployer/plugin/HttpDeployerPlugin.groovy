package ch.mollusca.glassfish.deployer.plugin

import ch.mollusca.glassfish.deployer.HttpDeployer
import org.gradle.api.Plugin
import org.gradle.api.Project

class HttpDeployerPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def deployer = new HttpDeployer.Builder()
                .applicationName("habba")
                .force(true)
                .build()

    }
}
