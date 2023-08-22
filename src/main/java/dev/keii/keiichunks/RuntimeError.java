package dev.keii.keiichunks;

public class RuntimeError {
    private final Exception exception;
    private final String executor;

    public RuntimeError(Exception e, String executor)
    {
        this.exception = e;
        this.executor = executor;
    }

    public String getExecutor() {
        return executor;
    }

    public Exception getException() {
        return exception;
    }
}
