package dev.keii.chunks.error;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Result<TValue, TError> {
    <R> R match(
            Function<? super TValue, ? extends R> successMapper,
            Function<? super TError, ? extends R> failureMapper
    );

    abstract void match(
            Consumer<? super TValue> successConsumer,
            Consumer<? super TError> failureConsumer
    );

    abstract boolean isSuccess();
    abstract boolean isFailure();

    static <TValue, TError> Result<TValue, TError> success(TValue value) {
        return new Success<>(value);
    }

    static <TValue, TError> Result<TValue, TError> failure(TError error) {
        return new Failure<>(error);
    }
}