package org.by1337.blib.property.property;

import org.by1337.blib.property.PropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A specific implementation of the Property class for storing lists of values of a generic type.
 *
 * @param <T> The type of elements stored in the list.
 */
@Deprecated(forRemoval = true)
public class PropertyList<T> extends Property<List<T>> {

    /**
     * The class representing the inner type of the list.
     */
    private final Class<?> innerType;

    /**
     * Constructor to create a PropertyList instance with an initial list of values and the inner type class.
     *
     * @param value     The initial list of values to set for the property.
     * @param innerType The class representing the inner type of the list elements.
     */
    public PropertyList(@Nullable List<T> value, Class<?> innerType) {
        super(value);
        this.innerType = innerType;
    }

    /**
     * This method is not implemented and will always throw an IllegalStateException.
     *
     * @param str The string to parse.
     * @throws IllegalStateException always, as parsing is not supported for a PropertyList.
     */
    @Override
    public List<T> parse(@NotNull String str) {
        throw new UnsupportedOperationException();
    }


    /**
     * Get the property type for this instance, which is List.
     *
     * @return The PropertyType representing the List type.
     */
    @Override
    public PropertyType<?> getType() {
        return PropertyType.LIST;
    }

    /**
     * Get the class representing the inner type of the list elements.
     *
     * @return The class representing the inner type.
     */
    public Class<?> getInnerType() {
        return innerType;
    }
}
