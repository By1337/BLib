package org.by1337.api.property;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.by1337.api.BLib;
import org.by1337.api.property.property.*;
import org.by1337.api.world.BLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This abstract class represents a collection of editable properties using a Map structure.
 * Each property is associated with a unique name and can be retrieved by its name.
 * New properties can be registered in the collection.
 */
public class StorageProperties {

    private final Map<String, Property<?>> properties;

    public StorageProperties() {
        properties = new HashMap<>();
    }

    public StorageProperties(Map<String, Property<?>> properties) {
        this.properties = properties;
    }

    /**
     * Retrieves a property by its name.
     *
     * @param name The name of the property to retrieve.
     * @return The property with the specified name.
     * @throws NullPointerException if the provided name is null.
     */
    public Property<?> getProperty(@NotNull String name) {
        Property<?> property = properties.getOrDefault(name, null);
        if (property == null) {
            throw new IllegalArgumentException(String.format("Unknown property '%s'", name));
        }
        return property;
    }

    /**
     * Registers a new property in the collection.
     *
     * @param property The property to register.
     * @throws IllegalArgumentException if a property with the same name already exists.
     */
    protected <T> void putProperty(@NotNull String name, @NotNull Property<T> property) {
        if (properties.containsKey(name)) {
            throw new IllegalArgumentException("A property with the same name already exists!");
        }
        properties.put(name, property);
    }

    /**
     * Registers a new property in the collection, allowing replacement if it already exists.
     *
     * @param property       The property to register.
     * @param replaceIfExist Whether to replace an existing property with the same name.
     * @return The registered property.
     * @throws IllegalArgumentException if a property with the same name already exists and replacement is disallowed.
     */
    protected <T> Property<T> putProperty(@NotNull String name, @NotNull Property<T> property, boolean replaceIfExist) {
        if (properties.containsKey(name)) {
            if (!replaceIfExist) {
                throw new IllegalArgumentException("A property with the same name already exists!");
            } else if (properties.get(name).getClass() != property.getClass()) {
                BLib.getApi().getMessage().warning("the property has been replaced by a property of a different class! " + properties.get(name) + " to " + property);
            }
        }
        properties.put(name, property);
        return property;
    }

    /**
     * Get the names of all properties in the collection.
     *
     * @return A set of property names.
     */
    public Set<String> getPropertiesNames() {
        return properties.keySet();
    }

    /**
     * Get the value of a property by name and type.
     *
     * @param name  The name of the property to retrieve.
     * @param clazz The expected type of the property's value.
     * @param <T>   The generic type for the property value.
     * @return The property's value of the specified type.
     * @throws ClassCastException if the property value cannot be cast to the specified type.
     */
    public <T> T getPropertyValue(@NotNull String name, @NotNull Class<T> clazz) {
        Property<?> raw = getProperty(name);
        if (clazz.isInstance(raw.getValue())) {
            return clazz.cast(raw.getValue());
        } else {
            throw new ClassCastException("Cannot cast the object of type " + raw.getClass() + " to the specified type " + clazz.getName());
        }
    }

    /**
     * Get the value of a property by name and type with a default value.
     *
     * @param name  The name of the property to retrieve.
     * @param clazz The expected type of the property's value.
     * @param def   The default value to return if the property is not found.
     * @param <T>   The generic type for the property value.
     * @return The property's value of the specified type or the default value if the property is not found.
     * @throws ClassCastException if the property value cannot be cast to the specified type.
     */
    public <T> T getPropertyValueOrDefault(@NotNull String name, @NotNull Class<T> clazz, T def) {
        if (!hasProperty(name)) {
            return def;
        }
        return getPropertyValue(name, clazz);
    }

    /**
     * Get a property by name and type.
     *
     * @param name  The name of the property to retrieve.
     * @param clazz The expected type of the property.
     * @param <T>   The generic type for the property.
     * @return The property of the specified type.
     * @throws ClassCastException if the property cannot be cast to the specified type.
     */
    public <T extends Property<?>> T getProperty(@NotNull String name, @NotNull Class<T> clazz) {
        Property<?> raw = getProperty(name);
        if (clazz.isInstance(raw)) {
            return clazz.cast(raw);
        } else {
            throw new ClassCastException("Cannot cast the object of type " + raw.getClass() + " to the specified type " + clazz.getName());
        }
    }

    /**
     * Get a property by name and type with a default value.
     *
     * @param name  The name of the property to retrieve.
     * @param clazz The expected type of the property.
     * @param def   The default property to return if the property is not found.
     * @param <T>   The generic type for the property.
     * @return The property of the specified type or the default property if the property is not found.
     * @throws ClassCastException if the property cannot be cast to the specified type.
     */
    public <T extends Property<?>> T getPropertyOrDefault(@NotNull String name, @NotNull Class<T> clazz, T def) {
        if (!hasProperty(name)) {
            return def;
        }
        return getProperty(name, clazz);
    }

    /**
     * Check if a property with the given name exists in the collection.
     *
     * @param name The name of the property to check.
     * @return True if a property with the given name exists, otherwise false.
     */
    public boolean hasProperty(String name) {
        return properties.containsKey(name);
    }

    /**
     * Registers a new property in the collection using the provided name and value.
     *
     * @param name         The name of the property to register.
     * @param value        The value to associate with the property.
     * @param propertyType The type of property to create and register.
     * @param <T>          The generic type of the property's value.
     */
    public <T> void putProperty(String name, T value, PropertyType<T> propertyType) {
        putProperty(name, propertyType.supplier().get(value));
    }

    /**
     * Removes a property with the specified name from the collection of properties.
     *
     * @param name The name of the property to be removed.
     * @throws IllegalArgumentException if no property with the specified name is found.
     */
    public void remove(String name) {
        // Remove a property with the specified name from the collection of properties.
        Property<?> removedProperty = properties.remove(name);
        if (removedProperty == null) {
            throw new IllegalArgumentException(String.format("No property with the name '%s' found.", name));
        }
    }

    /**
     * Registers a new property in the collection as a list property.
     *
     * @param name      The name of the property to register.
     * @param list      The list to associate with the property.
     * @param innerType The class type of the elements within the list.
     */
    public void putList(String name, List<?> list, Class<?> innerType) {
        putProperty(name, new PropertyList<>(list, innerType));
    }

    /**
     * Registers a new property in the collection as a boolean property.
     *
     * @param name  The name of the property to register.
     * @param value The boolean value to associate with the property.
     */
    public void putBoolean(String name, Boolean value) {
        putProperty(name, value, PropertyType.BOOLEAN);
    }

    /**
     * Registers a new property in the collection as a color property.
     *
     * @param name  The name of the property to register.
     * @param value The Color value to associate with the property.
     */
    public void putColor(String name, Color value) {
        putProperty(name, value, PropertyType.COLOR);
    }

    /**
     * Registers a new property in the collection as a double property.
     *
     * @param name  The name of the property to register.
     * @param value The double value to associate with the property.
     */
    public void putDouble(String name, Double value) {
        putProperty(name, value, PropertyType.DOUBLE);
    }

    /**
     * Registers a new property in the collection as an integer property.
     *
     * @param name  The name of the property to register.
     * @param value The integer value to associate with the property.
     */
    public void putInteger(String name, Integer value) {
        putProperty(name, value, PropertyType.INTEGER);
    }

    /**
     * Registers a new property in the collection as a location property.
     *
     * @param name  The name of the property to register.
     * @param value The BLocation value to associate with the property.
     */
    public void putLocation(String name, BLocation value) {
        putProperty(name, value, PropertyType.LOCATION);
    }

    /**
     * Registers a new property in the collection as a long property.
     *
     * @param name  The name of the property to register.
     * @param value The long value to associate with the property.
     */
    public void putLong(String name, Long value) {
        putProperty(name, value, PropertyType.LONG);
    }

    /**
     * Registers a new property in the collection as a Material property.
     *
     * @param name  The name of the property to register.
     * @param value The Material value to associate with the property.
     */
    public void putMaterial(String name, Material value) {
        putProperty(name, value, PropertyType.MATERIAL);
    }

    /**
     * Registers a new property in the collection as a Particle property.
     *
     * @param name  The name of the property to register.
     * @param value The Particle value to associate with the property.
     */
    public void putParticle(String name, Particle value) {
        putProperty(name, value, PropertyType.PARTICLE);
    }

    /**
     * Registers a new property in the collection as a string property.
     *
     * @param name  The name of the property to register.
     * @param value The string value to associate with the property.
     */
    public void putString(String name, String value) {
        putProperty(name, value, PropertyType.STRING);
    }

    /**
     * Registers a new property in the collection as a vector property.
     *
     * @param name  The name of the property to register.
     * @param value The Vector value to associate with the property.
     */
    public void putVector(String name, Vector value) {
        putProperty(name, value, PropertyType.VECTOR);
    }

    public Boolean getAsBoolean(String name) {
        return getPropertyValue(name, PropertyBoolean.class).getValue();
    }

    public Color getAsColor(String name) {
        return getPropertyValue(name, PropertyColor.class).getValue();
    }

    public Double getAsDouble(String name) {
        return getPropertyValue(name, PropertyDouble.class).getValue();
    }

    public Integer getAsInteger(String name) {
        return getPropertyValue(name, PropertyInteger.class).getValue();
    }

    public BLocation getAsLocation(String name) {
        return getPropertyValue(name, PropertyLocation.class).getValue();
    }

    public Long getAsLong(String name) {
        return getPropertyValue(name, PropertyLong.class).getValue();
    }

    public Material getAsMaterial(String name) {
        return getPropertyValue(name, PropertyMaterial.class).getValue();
    }

    public Particle getAsParticle(String name) {
        return getPropertyValue(name, PropertyParticle.class).getValue();
    }

    public String getAsString(String name) {
        return getPropertyValue(name, PropertyString.class).getValue();
    }

    public Vector getAsVector(String name) {
        return getPropertyValue(name, PropertyVector.class).getValue();
    }

    public Map<String, Property<?>> getProperties() {
        return properties;
    }
}
