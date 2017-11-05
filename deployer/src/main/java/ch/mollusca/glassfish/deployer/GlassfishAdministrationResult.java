package ch.mollusca.glassfish.deployer;

public class GlassfishAdministrationResult {

    private final ExitCode exit_code;
    private final String message;

    public GlassfishAdministrationResult(ExitCode exit_code, String message) {
        this.exit_code = exit_code;
        this.message = message;
    }

    public ExitCode getExit_code() {
        return exit_code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "GlassfishAdministrationResult{" +
                "exit_code=" + exit_code +
                ", message='" + message + '\'' +
                '}';
    }

    public enum ExitCode {
        SUCCESS,
        FAILURE
    }
}
