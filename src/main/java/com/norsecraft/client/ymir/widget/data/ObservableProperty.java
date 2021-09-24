package com.norsecraft.client.ymir.widget.data;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public final class ObservableProperty<T> implements ObservableView<T> {

    private static final String DEFAULT_NAME = "<unnamed>";
    private boolean hasValue;
    private T value;
    private final List<ChangeListener<? super T>> listeners = Lists.newArrayList();
    private final boolean allowNull;
    private final String name;

    private ObservableProperty(@Nullable T value, boolean hasValue, boolean allowNull, String name) {
        this.value = value;
        this.hasValue = hasValue;
        this.allowNull = allowNull;
        this.name = name;

        if (hasValue && value == null && !allowNull) {
            throw new NullPointerException("Cannot initialise nonnull property " + name + " with null value");
        }
    }

    public static <T> Builder<T> empty() {
        return new Builder<>(null, false);
    }

    public static <T> Builder<T> of(T initialValue) {
        return new Builder<>(initialValue, true);
    }

    @Override
    public boolean hasValue() {
        return this.hasValue;
    }

    @Override
    public T get() {
        if (!hasValue) {
            throw new IllegalStateException("Property " + name + " not initialised!");
        }

        return value;
    }

    public void set(T value) {
        if(value == null && !allowNull)
            throw new NullPointerException("Trying to set null value for nonnull property " + name);
        T oldValue = this.value;
        this.value = value;
        hasValue = true;

        if(oldValue != value) {
            for(ChangeListener<? super T> listener : listeners)
                listener.onPropertyChange(this, oldValue, value);
        }
    }

    public ObservableView<T> readOnly() {
        return new ObservableView<>() {
            @Override
            public boolean hasValue() {
                return ObservableProperty.this.hasValue;
            }

            @Override
            public T get() {
                return ObservableProperty.this.get();
            }

            @Override
            public void addListener(ChangeListener<? super T> listener) {
                ObservableProperty.this.addListener(listener);
            }

            @Override
            public void removeListener(ChangeListener<? super T> listener) {
                ObservableProperty.this.removeListener(listener);
            }
        };
    }

    public String getName() {
        return name;
    }

    @Override
    public void addListener(ChangeListener<? super T> listener) {
        Objects.requireNonNull(listener);
        listeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super T> listener) {
        Objects.requireNonNull(listener);
        listeners.remove(listener);
    }

    public static final class Builder<T> {

        private final T initialValue;
        private final boolean hasValue;
        private String name = DEFAULT_NAME;
        private boolean allowNull = true;

        Builder(@Nullable T initialValue, boolean hasValue) {
            this.initialValue = initialValue;
            this.hasValue = hasValue;
        }

        public Builder<T> nonnull() {
            allowNull = false;
            return this;
        }

        public Builder<T> name(String name) {
            this.name = Objects.requireNonNull(name, "name");
            return this;
        }

        public ObservableProperty<T> build() {
            return new ObservableProperty<>(initialValue, hasValue, allowNull, name);
        }


    }

}
