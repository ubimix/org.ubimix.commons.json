/**
 * 
 */
package org.webreformatter.commons.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.webreformatter.commons.json.IJsonAccessor.JsonType;

/**
 * @author kotelnikov
 */
public class JsonArray extends JsonValue {

    public static IJsonValueFactory<JsonArray> FACTORY = ARRAY_FACTORY;

    public static JsonArray newValue(Object o) {
        return FACTORY.newValue(o);
    }

    public JsonArray() {
        this((Object) null);
    }

    /**
     * @param accessor
     * @param object
     */
    protected JsonArray(Object object) {
        super(object);
    }

    /**
     * Adds a new value to the array property.
     * 
     * @param name the name of an array property
     * @param value the value to add
     */
    public JsonArray addValue(Object value) {
        Object array = getArray();
        Object val = JsonValue.toJsonValue(value);
        int pos = JsonValue.fAccessor.getArraySize(array);
        JsonValue.fAccessor.addArrayValue(array, pos, val);
        return this;
    }

    /**
     * Removes all values from this array
     */
    public void clear() {
        Object array = getArray();
        int size = JsonValue.fAccessor.getArraySize(array);
        for (int i = size - 1; i >= 0; i--) {
            JsonValue.fAccessor.removeArrayValue(array, i);
        }
    }

    private Object getArray() {
        return fObject;
    }

    /**
     * Returns the value of the specified position as an array.
     * 
     * @param pos the position in the array
     * @return the value of the specified position as an array.
     */
    public JsonArray getArray(int pos) {
        return getObject(pos, FACTORY);
    }

    /**
     * Returns the boolean representation of the array cell corresponding to the
     * specified position.
     * 
     * @param pos the position in the array
     * @param defaultValue the default value; used if the specified property is
     *        not an array or if the required cell could not be interpreted as a
     *        boolean
     * @return the boolean representation of an array cell corresponding to the
     *         specified property and position.
     */
    public boolean getBoolean(int pos, boolean defaultValue) {
        Boolean value = getValue(pos, JsonType.BOOLEAN);
        return value != null ? value : defaultValue;
    }

    /**
     * Returns the float representation of the array cell corresponding to the
     * specified position.
     * 
     * @param pos the position in the array
     * @param defaultValue the default value; used if the specified property is
     *        not an array or if the required cell could not be interpreted as a
     *        float
     * @return the float representation of an array cell corresponding to the
     *         specified property and position.
     */
    public double getDouble(int pos, double defaultValue) {
        Double value = getValue(pos, JsonType.DOUBLE);
        return value != null ? value : defaultValue;
    }

    /**
     * Returns the integer representation of the array cell corresponding to the
     * specified property and position.
     * 
     * @param pos the position in the array
     * @param defaultValue the default value; used if the specified property is
     *        not an array or if the required cell could not be interpreted as a
     *        integer
     * @return the integer representation of an array cell corresponding to the
     *         specified property and position.
     */
    public int getInteger(int pos, int defaultValue) {
        Integer value = getValue(pos, JsonType.INTEGER);
        return value != null ? value : defaultValue;
    }

    /**
     * Transforms values of this array into a list of objects of the required
     * type.
     * 
     * @param <W>
     * @param factory the factory used to create final objects from individual
     *        array values
     * @return a list of values
     */
    public <W> List<W> getList(IJsonValueFactory<W> factory) {
        return addValues(fObject, new ArrayList<W>(), factory);
    }

    /**
     * Returns the integer representation of the array cell corresponding to the
     * specified property and position.
     * 
     * @param pos the position in the array
     * @param defaultValue the default value; used if the specified property is
     *        not an array or if the required cell could not be interpreted as a
     *        integer
     * @return the integer representation of an array cell corresponding to the
     *         specified property and position.
     */
    public long getLong(int pos, long defaultValue) {
        Long value = getValue(pos, JsonType.LONG);
        return value != null ? value : defaultValue;
    }

    /**
     * Returns the JSON value from the specified position.
     * 
     * @param pos the value position
     * @return the JSON value from the specified position.
     */
    public Object getObject(int pos) {
        return getObject(pos, NULL_FACTORY);
    }

    /**
     * Returns the array value of the expected type from the specified position.
     * 
     * @param <T> the expected type of the returned value
     * @param pos the array position
     * @param type the expected type of the returned property
     * @return the value of the specified type
     */
    public <T> T getObject(int pos, IJsonValueFactory<T> factory) {
        Object value = getValue(pos);
        return value != null ? factory.newValue(value) : null;
    }

    /**
     * Transforms values of this array into a set of objects of the required
     * type.
     * 
     * @param <W>
     * @param factory the factory used to create final objects from individual
     *        array values
     * @return a list of values
     */
    public <W> Set<W> getSet(IJsonValueFactory<W> factory) {
        return addValues(fObject, new LinkedHashSet<W>(), factory);
    }

    /**
     * Returns the size of the array
     * 
     * @return the size of the array
     */
    public int getSize() {
        Object array = getArray();
        return array != null ? JsonValue.fAccessor.getArraySize(array) : 0;
    }

    /**
     * Returns the string representation of the array cell corresponding to the
     * specified position.
     * 
     * @param pos the position in the array
     * @param defaultValue the default value; used if the specified property is
     *        not an array or if the required cell could not be interpreted as a
     *        string
     * @return the string representation of an array cell corresponding to the
     *         specified property and position.
     */
    public String getString(int pos) {
        Object value = getValue(pos);
        if (value == null) {
            return null;
        }
        return JsonValue.fAccessor.toString(value);
    }

    /**
     * Returns the "native" array value from the specified position.
     * 
     * @param pos the array position
     * @return the "native" array value from the specified position
     */
    private Object getValue(int pos) {
        Object array = getArray();
        return JsonValue.fAccessor.getArrayValue(array, pos);
    }

    /**
     * Returns the array value of the expected type from the specified position.
     * 
     * @param <T> the expected type of the returned value
     * @param pos the array position
     * @param type the expected type of the returned property
     * @return the value of the specified type
     */
    @SuppressWarnings("unchecked")
    private <T> T getValue(int pos, JsonType type) {
        Object value = getValue(pos);
        return (T) (JsonValue.fAccessor.getType(value) == type ? value : null);
    }

    /**
     * Creates an fills values from the internal array in the given collection.
     * 
     * @param <W>
     * @param name the name of the property
     * @param collection the result collection where all values from the
     *        original internal array should be added
     * @param factory the factory used to create JSON wrappers for each array
     *        value
     */
    public <W, C extends Collection<? super W>> C getValues(
        C collection,
        IJsonValueFactory<W> factory) {
        return addValues(fObject, collection, factory);
    }

    /**
     * Inserts a new value of an array property in the specified position.
     * 
     * @param pos the position in the array
     * @param value the value to insert
     */
    public JsonArray insertValue(int pos, Object value) {
        Object array = getArray();
        Object val = JsonValue.toJsonValue(value);
        JsonValue.fAccessor.addArrayValue(array, pos, val);
        return this;
    }

    @Override
    protected Object newJsonInstance() {
        return fAccessor.newArray();
    }

    /**
     * Removes a value of an array property from the specified position.
     * 
     * @param pos the position in the array
     * @return <code>true</code> if the value was successfully removed
     */
    public boolean removeValue(int pos) {
        Object array = getArray();
        if (array == null) {
            return false;
        }
        JsonValue.fAccessor.removeArrayValue(array, pos);
        return true;
    }

    /**
     * Sets a new value in the specified position.
     * 
     * @param pos the position in the array
     * @param value the value to set in the array
     */
    public JsonArray setValue(int pos, Object value) {
        Object array = getArray();
        Object val = JsonValue.toJsonValue(value);
        JsonValue.fAccessor.setArrayValue(array, pos, val);
        return this;
    }

    /**
     * Replaces the property array by the given values
     * 
     * @param name the name of the property
     * @param values the values to set
     */
    public JsonArray setValues(Iterable<?> values) {
        clear();
        int i = 0;
        Object array = getArray();
        for (Object value : values) {
            Object val = JsonValue.toJsonValue(value);
            JsonValue.fAccessor.setArrayValue(array, i, val);
            i++;
        }
        return this;
    }

    /**
     * Replaces the property array by the given values
     * 
     * @param <T> the type of values to set
     * @param name the name of the property
     * @param values the values to set
     */
    public <T> JsonArray setValues(T... values) {
        clear();
        int i = 0;
        Object array = getArray();
        for (Object value : values) {
            Object val = JsonValue.toJsonValue(value);
            JsonValue.fAccessor.setArrayValue(array, i, val);
            i++;
        }
        return this;
    }

}
