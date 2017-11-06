package ch.mollusca.glassfish.deployer;

import com.squareup.moshi.Moshi;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HttpDeployer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpDeployer.class);

    private final OkHttpClient client;
    private final String scheme;
    private final String host;
    private final int port;
    private final boolean force;
    private final String contextRoot;
    private final String applicationName;
    private final String user;
    private final String password;

    private HttpDeployer(boolean sslValidation, String scheme, String host, int port, boolean force, String contextRoot, String applicationName, String user, String password) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.force = force;
        this.contextRoot = contextRoot;
        this.applicationName = applicationName;
        this.user = user;
        this.password = password;

        if (sslValidation) {
            this.client = createOkHttpClient();
        } else {
            try {
                this.client = createInsecureOkHttpClient();

            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                throw new RuntimeException("Could not create insecure ssl client", e);
            }
        }
    }

    public GlassfishAdministrationResult deploy(File applicationArchive) {
        if(!applicationArchive.exists()) {
            LOGGER.error("Deployment archive does not exist: {}", applicationArchive.getAbsolutePath());
            throw new IllegalArgumentException(String.format("Deployment archive %s does not exist!", applicationArchive));
        }

        LOGGER.info("About to deploy {} to {}", applicationArchive, host);

        MediaType octetStream = MediaType.parse("application/octet-stream");

        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", applicationArchive.getName(), RequestBody.create(octetStream, applicationArchive))
                .addFormDataPart("force", Boolean.toString(force));

        if (!contextRoot.isEmpty()) {
            bodyBuilder.addFormDataPart("contextroot", contextRoot);
        }

        if (!applicationName.isEmpty()) {
            bodyBuilder.addFormDataPart("name", applicationName);
        }

        Request request = new Request.Builder()
                .url(domainManagementUrl())
                .addHeader("Authorization", Credentials.basic(user, password))
                .addHeader("Accept", "application/json")
                .addHeader("X-Requested-By", "simple-deployer")
                .post(bodyBuilder.build())
                .build();

        return executeRequestAndParseResponse(request);
    }

    public GlassfishAdministrationResult undeploy() {
        if(applicationName.isEmpty()) {
            LOGGER.error("Undeploy only works with a defined applicationName!");
            throw new IllegalArgumentException("Undeploy only works with a defined applicationName!");
        }

        LOGGER.info("About to undeploy {} from {}", applicationName, host);

        Request request = new Request.Builder()
                .url(domainManagementUrl() + applicationName)
                .addHeader("Authorization", Credentials.basic(user, password))
                .addHeader("Accept", "application/json")
                .addHeader("X-Requested-By", "simple-deployer")
                .delete()
                .build();

        return executeRequestAndParseResponse(request);
    }

    private GlassfishAdministrationResult executeRequestAndParseResponse(Request request) {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                LOGGER.info("Successfully executed request {}", request);
                return parseResponse(response);
            } else {
                LOGGER.error("Could not execute request. Error code: {}, message: {}", response.code(), response.message());
                return new GlassfishAdministrationResult(GlassfishAdministrationResult.ExitCode.FAILURE, response.message());
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not execute %s", request), e);
        }
    }

    private String domainManagementUrl() {
        return String.format("%s://%s:%d/management/domain/applications/application/", scheme, host, port);
    }

    private GlassfishAdministrationResult parseResponse(Response response) {
        Moshi moshi = new Moshi.Builder().build();
        try {
            return moshi
                    .adapter(GlassfishAdministrationResult.class)
                    .fromJson(response.body().string());

        } catch (IOException e) {
            return new GlassfishAdministrationResult(GlassfishAdministrationResult.ExitCode.FAILURE, "Could not parse response from server!");
        }
    }

    private OkHttpClient createInsecureOkHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
        X509TrustManager trustAllTrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                // Intentionally left blank
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                // Intentionally left blank
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[]{trustAllTrustManager}, new SecureRandom());
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        return createClientBuilder()
                .sslSocketFactory(sslSocketFactory, trustAllTrustManager)
                .hostnameVerifier((s, sslSession) -> true)
                .build();
    }

    private OkHttpClient.Builder createClientBuilder() {
        return new OkHttpClient.Builder()
                .connectTimeout(12, TimeUnit.SECONDS)
                .readTimeout(12, TimeUnit.SECONDS)
                .writeTimeout(12, TimeUnit.SECONDS);
    }

    private OkHttpClient createOkHttpClient() {
        return createClientBuilder().build();
    }

    public static final class Builder {
        private boolean sslValidation = true;
        private String host = "localhost";
        private String scheme = "https";
        private int port = 4848;

        private boolean force = false;
        private String contextRoot = "";
        private String applicationName = "";

        private String user = "admin";
        private String password = "";

        public Builder host(String host) {
            this.host = Objects.requireNonNull(host, "host cannot be null!");
            return this;
        }

        public Builder scheme(String scheme) {
            Objects.requireNonNull(scheme, "scheme cannot be null");
            if (scheme.equals("http") || scheme.equals("https")) {
                this.scheme = scheme;
            } else {
                throw new IllegalArgumentException("scheme should either be http or https");
            }
            return this;
        }

        public Builder port(int adminPort) {
            this.port = adminPort;
            return this;
        }

        public Builder force(boolean force) {
            this.force = force;
            return this;
        }

        public Builder contextRoot(String contextRoot) {
            this.contextRoot = Objects.requireNonNull(contextRoot, "context root cannot be null!");
            return this;
        }

        public Builder applicationName(String applicationName) {
            this.applicationName = Objects.requireNonNull(applicationName, "application name cannot be null!");
            return this;
        }

        public Builder user(String user) {
            this.user = Objects.requireNonNull(user, "user cannot be null!");
            return this;
        }

        public Builder password(String password) {
            this.password = Objects.requireNonNull(password, "password cannot be null!");
            return this;
        }

        public Builder insecure() {
            this.sslValidation = false;
            return this;
        }

        public HttpDeployer build() {
            return new HttpDeployer(sslValidation, scheme, host, port, force, contextRoot, applicationName, user, password);
        }
    }
}
