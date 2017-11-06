package ch.mollusca.glassfish.deployer.plugin;

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
}
