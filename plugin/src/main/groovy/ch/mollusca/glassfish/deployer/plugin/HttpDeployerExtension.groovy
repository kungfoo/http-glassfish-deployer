package ch.mollusca.glassfish.deployer.plugin

import java.util.concurrent.TimeUnit;

class HttpDeployerExtension {
    boolean secure = true
    String host = "localhost"
    String scheme = "https"
    int port = 4848

    boolean force = false
    String contextRoot = ""
    String applicationName = ""

    String user = "admin"
    String password = ""

    String deploymentArchive = ""
    
    long timeoutDuration = 12;
    TimeUnit timeoutUnit = TimeUnit.SECONDS;
}
