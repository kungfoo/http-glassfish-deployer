package ch.mollusca.glassfish.deployer.application;

import ch.mollusca.glassfish.deployer.GlassfishAdministrationResult;
import ch.mollusca.glassfish.deployer.SimpleDeployer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class SimpleDeployerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDeployerApplication.class);

    private static final String APPLICATION_NAME = "ch.mollusca.sample";
    private static final String CONTEXT_ROOT = "ch.mollusca.sample";
    private static final String WAR_FILE = "sample-war.war";

    private static final String PASSWORD = "f34ccb5c";
    private static final String HOST = "paste";

    public static void main(String... args) throws InterruptedException {
        SimpleDeployer deployer = new SimpleDeployer.Builder()
                .insecure()
                .password(PASSWORD)
                .applicationName(APPLICATION_NAME)
                .contextRoot(CONTEXT_ROOT)
                .build();

        GlassfishAdministrationResult result = deployer.deploy(new File(WAR_FILE));
        LOGGER.info("Result: {}", result);

        Thread.sleep(500);
        result = deployer.undeploy();
        LOGGER.info("Result: {}", result);
    }
}
