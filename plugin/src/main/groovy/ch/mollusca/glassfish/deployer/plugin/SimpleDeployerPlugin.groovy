package ch.mollusca.glassfish.deployer.plugin

import ch.mollusca.glassfish.deployer.SimpleDeployer
import org.gradle.api.Plugin
import org.gradle.api.Project

class SimpleDeployerPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def deployer = new SimpleDeployer.Builder()
                .applicationName("habba")
                .force(true)
                .build()

    }
}
