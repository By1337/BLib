package org.by1337.blib.property.property;

import org.by1337.blib.property.PropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The base class for properties in the application.
 * Properties are used to store and manipulate various types of data.
 *
 * @param <T> The type of data stored by this property.
 */
@Deprecated(forRemoval = true)
public abstract class Property<T> {
    /**
     * The value stored in the property.
     */
    @Nullable
    private T value;

    /**
     * Constructor to create a Property instance with an initial value.
     *
     * @param value The initial value to set for the property.
     */
    protected Property(@Nullable T value) {
        this.value = value;
    }

    /**
     * Parse a string and convert it into the property's data type.
     *
     * @param str The string to parse.
     * @return The parsed value.
     */
    public abstract T parse(@NotNull String str);

    /**
     * Get the type of the property.
     *
     * @return The PropertyType representing the type of data stored.
     */
    abstract public PropertyType<?> getType();

    /**
     * Get the current value of the property.
     *
     * @return The current value of the property, which may be null.
     */
    @Nullable
    public T getValue() {
        return this.value;
    }

    /**
     * Set the value of the property.
     *
     * @param value The new value to set for the property, which may be null.
     */
    public void setValue(@Nullable T value) {
        this.value = value;
    }
}

