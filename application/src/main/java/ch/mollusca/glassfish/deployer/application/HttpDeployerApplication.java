package ch.mollusca.glassfish.deployer.application;

import ch.mollusca.glassfish.deployer.GlassfishAdministrationResult;
import ch.mollusca.glassfish.deployer.HttpDeployer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static java.lang.System.exit;

public class HttpDeployerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpDeployerApplication.class);

    private static final String APPLICATION_NAME = "ch.mollusca.sample";
    private static final String CONTEXT_ROOT = "ch.mollusca.sample";

    private static final String PASSWORD = "b0c81d64";

    public static void main(String... args) throws InterruptedException {
        File deploymentArchive = ensureValidArguments(args);

        HttpDeployer deployer = new HttpDeployer.Builder()
                .insecure()
                .password(PASSWORD)
                .applicationName(APPLICATION_NAME)
                .contextRoot(CONTEXT_ROOT)
                .build();

        GlassfishAdministrationResult result = deployer.deploy(deploymentArchive);
        LOGGER.info("Result: {}", result);

        Thread.sleep(500);
        result = deployer.undeploy();
        LOGGER.info("Result: {}", result);
    }

    private static File ensureValidArguments(String[] args) {
        if(args.length < 1) {
            LOGGER.error("usage: HttpDeployerApplication ${path-to-deployment-archive}");
            exit(1);
        }
        return new File(args[0]);
    }
}
