package dev.keii.chunks.error;

import java.util.function.Consumer;
import java.util.function.Function;

record Failure<TValue, TError>(TError error) implements Result<TValue, TError> {
    @Override
    public <R> R match(
            Function<? super TValue, ? extends R> successMapper,
            Function<? super TError, ? extends R> failureMapper
    ) {
        return failureMapper.apply(error);
    }

    @Override
    public void match(
            Consumer<? super TValue> successConsumer,
            Consumer<? super TError> failureConsumer
    ) {
        failureConsumer.accept(error);
    }

    @Override
    public boolean isSuccess()
    {
        return match(
                value -> true,
                value -> false
        );
    }

    @Override
    public boolean isFailure()
    {
        return !isSuccess();
    }
}