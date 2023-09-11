package dev.keii.chunks.error;


import java.util.function.Consumer;
import java.util.function.Function;

record Success<TValue, TError>(TValue value) implements Result<TValue, TError> {
    @Override
    public <R> R match(
            Function<? super TValue, ? extends R> successMapper,
            Function<? super TError, ? extends R> failureMapper
    ) {
        return successMapper.apply(value);
    }

    @Override
    public void match(
            Consumer<? super TValue> successConsumer,
            Consumer<? super TError> failureConsumer
    ) {
        successConsumer.accept(value);
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