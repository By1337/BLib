package org.by1337.blib.util.value;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * A utility class that manages a value that expires after a specified lifetime.
 * The value is automatically refreshed using a provided {@link Supplier} when it expires.
 *
 * @param <T> The type of the value being managed.
 */
public class ExpiringValue<T> {
    private long lastAccess;
    private @Nullable T value;
    private final Supplier<@NotNull T> supplier;
    private final long lifetime;

    /**
     * Constructs an {@code ExpiringValue} with a specified lifetime and time unit.
     *
     * @param supplier The {@link Supplier} used to generate the value.
     * @param lifetime The lifetime of the value before it expires.
     * @param unit     The {@link TimeUnit} of the lifetime.
     * @throws NullPointerException if the {@code supplier} returns {@code null}.
     */
    public ExpiringValue(Supplier<@NotNull T> supplier, long lifetime, TimeUnit unit) {
        this.supplier = supplier;
        this.lifetime = unit.toMillis(lifetime);
    }

    /**
     * Constructs an {@code ExpiringValue} with a specified lifetime in milliseconds.
     *
     * @param supplier The {@link Supplier} used to generate the value.
     * @param lifetime The lifetime of the value in milliseconds before it expires.
     * @throws NullPointerException if the {@code supplier} returns {@code null}.
     */
    public ExpiringValue(Supplier<@NotNull T> supplier, long lifetime) {
        this.supplier = supplier;
        this.lifetime = lifetime;
    }

    /**
     * Retrieves the current value. If the value has expired, it will be refreshed
     * using the {@link Supplier}.
     *
     * @return The current value.
     * @throws NullPointerException if the {@code supplier} returns {@code null}.
     */
    public T get() {
        long time = System.currentTimeMillis();
        if (time - lastAccess > lifetime) {
            value = Objects.requireNonNull(supplier.get());
            lastAccess = time;
        }
        return value;
    }

    /**
     * Updates the value by forcibly refreshing it using the {@link Supplier},
     * regardless of whether it has expired.
     *
     * @throws NullPointerException if the {@code supplier} returns {@code null}.
     */
    public void update() {
        lastAccess = System.currentTimeMillis();
        value = Objects.requireNonNull(supplier.get());
    }

    /**
     * Sets a new value and updates the last access time.
     *
     * @param value The new value to set.
     */
    public void setValue(T value) {
        this.value = value;
        lastAccess = System.currentTimeMillis();
    }

    /**
     * Resets the last access time without changing the value.
     */
    public void resetTime() {
        lastAccess = System.currentTimeMillis();
    }


    /**
     * Retrieves the {@link Supplier} used to generate the value.
     *
     * @return The {@link Supplier}.
     */
    public Supplier<@NotNull T> getSupplier() {
        return supplier;
    }
}
