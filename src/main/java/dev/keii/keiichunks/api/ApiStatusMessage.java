package dev.keii.keiichunks.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiStatusMessage {
    @JsonProperty("status")
    private final int status;
    @JsonProperty("message")
    private final String message;

    @JsonCreator
    public ApiStatusMessage(@JsonProperty("status") int status, @JsonProperty("message") String message)
    {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public int getStatus() {
        return this.status;
    }
}
