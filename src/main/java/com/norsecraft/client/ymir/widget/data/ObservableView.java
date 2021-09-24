package com.norsecraft.client.ymir.widget.data;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public interface ObservableView<T> extends Supplier<T> {

    boolean hasValue();

    @Override
    T get();

    default @Nullable T getOrNull() {
        return hasValue() ? get() : null;
    }

    default Optional<T> find() {
        return Optional.ofNullable(getOrNull());
    }

    void addListener(ChangeListener<? super T> listener);

    void removeListener(ChangeListener<? super T> listener);

    interface ChangeListener<T> {

        void onPropertyChange(ObservableView<? extends T> property, @Nullable T from, @Nullable T to);

    }

}
