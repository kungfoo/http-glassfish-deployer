package ch.mollusca.glassfish.deployer.plugin

import ch.mollusca.glassfish.deployer.plugin.tasks.DeployTask
import ch.mollusca.glassfish.deployer.plugin.tasks.UndeployTask
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

class HttpDeployerPluginTest {

    Project project = ProjectBuilder.builder().build()

    private applyPlugin() {
        project.pluginManager.apply(HttpDeployerPlugin.PLUGIN_ID)
    }

    @Test
    void ensureExtensionExistsWithDefaults() {
        applyPlugin()

        def extension = project.extensions.getByName(HttpDeployerPlugin.EXTENSION)

        assertEquals("localhost", extension.host)
        assertEquals(4848, extension.port)
    }

    @Test
    void ensureDeployTaskExists() {
        applyPlugin()

        assertTrue(project.tasks.getByName(DeployTask.TASK_NAME) instanceof DeployTask)
    }

    @Test
    void ensureUndeployTaskExists() {
        applyPlugin()

        assertNotNull(project.tasks.getByName(UndeployTask.TASK_NAME) instanceof UndeployTask)
    }
}
