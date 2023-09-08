package dev.keii.chunks.error;

public abstract class Result {
    String message;

    public Result(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

