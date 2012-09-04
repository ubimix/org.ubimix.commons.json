/**
 * 
 */
package org.ubimix.commons.json;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ubimix.commons.json.IJsonAccessor.JsonType;

/**
 * This is a common super-class for JSON objects ({@link JsonObject}) or arrays
 * ({@link JsonArray}).
 * 
 * @author kotelnikov
 */
public abstract class JsonValue {

    /**
     * Instances of this type are used to create wrappers of a specific type for
     * JSON objects.
     * 
     * @author kotelnikov
     * @param <W> the type of created wrappers
     */
    public interface IJsonValueFactory<W> {
        /**
         * Transforms the specified JSON value (string, boolean, number, JSON
         * array or JSON object) into the target object.
         * 
         * @return a newly created target for the specified JSON value
         * @param object the JSON value to wrap
         */
        W newValue(Object object);
    }

    public static IJsonValueFactory<JsonArray> ARRAY_FACTORY = new IJsonValueFactory<JsonArray>() {
        public JsonArray newValue(Object object) {
            return new JsonArray(object);
        }
    };

    /**
     * Transforms the given value to double.
     */
    public static final IJsonValueFactory<Double> DOUBLE_FACTORY = new IJsonValueFactory<Double>() {
        public Double newValue(Object object) {
            return fAccessor.toDouble(object);
        }
    };

    /**
     * Internal field.
     */
    protected static IJsonAccessor fAccessor = JsonAccessor.getInstance();

    /**
     * Use {@link #OBJECT_FACTORY} instead
     * 
     * @see #OBJECT_FACTORY
     */
    @Deprecated
    public static final IJsonValueFactory<JsonObject> FACTORY;

    /**
     * This "factory" transforms the given value in a JSON internal value and
     * returns it.
     */
    public static IJsonValueFactory<Object> NULL_FACTORY = new IJsonValueFactory<Object>() {
        public Object newValue(Object object) {
            return toJsonValue(object);
        }
    };

    /**
     * Creates and returns {@link JsonObject} instance wrapping the specified
     * java value.
     */
    public static final IJsonValueFactory<JsonObject> OBJECT_FACTORY = new IJsonValueFactory<JsonObject>() {
        public JsonObject newValue(Object object) {
            return new JsonObject(object);
        }
    };

    /**
     * Transforms the given value to string.
     */
    public static final IJsonValueFactory<String> STRING_FACTORY = new IJsonValueFactory<String>() {
        public String newValue(Object object) {
            return fAccessor.toString(object);
        }
    };

    /**
     * Creates a {@link JsonValue} object from the given Java object. If the
     * given parameter is already a {@link JsonValue} instance then it just
     * return it.
     */
    public static final IJsonValueFactory<JsonValue> VALUE_FACTORY = new IJsonValueFactory<JsonValue>() {
        public JsonValue newValue(Object object) {
            Object val = toJsonValue(object);
            if (val instanceof JsonValue) {
                return (JsonValue) val;
            }
            JsonType type = fAccessor.getType(val);
            if (type == JsonType.ARRAY) {
                return new JsonArray(val);
            } else if (type == JsonType.OBJECT) {
                return new JsonObject(val);
            } else {
                JsonArray array = new JsonArray();
                array.addValue(val);
                return array;
            }
        }
    };

    static {
        FACTORY = OBJECT_FACTORY;
    }

    /**
     * Adds values from the given array to the specified {@link JsonObject}. All
     * impair objects are used as keys and pair objects are used as the
     * corresponding values.
     * 
     * @param values key/value pairs; impair objects are keys, pair objects are
     *        values
     */
    public static void addValues(JsonObject obj, Object... values) {
        for (int i = 0; i < values.length;) {
            Object str = values[i++];
            String key = fAccessor.toString(str);
            Object value = i < values.length ? values[i++] : null;
            obj.setValue(key, value);
        }
    }

    /**
     * Transforms values from an internal array into objects using the specified
     * factory and puts them in the collection.
     * 
     * @param <W>
     * @param <C>
     * @param array the internal array object
     * @param collection the collection to fill with values
     * @param factory the factory of values
     * @return the collection
     */
    protected static <W, C extends Collection<? super W>> C addValues(
        Object array,
        C collection,
        IJsonValueFactory<W> factory) {
        if (array != null && fAccessor.getType(array) == JsonType.ARRAY) {
            int len = fAccessor.getArraySize(array);
            for (int i = 0; i < len; i++) {
                Object value = fAccessor.getArrayValue(array, i);
                W wrapper = factory.newValue(value);
                collection.add(wrapper);
            }
        }
        return collection;
    }

    /**
     * @return the accessorInstance
     */
    public static IJsonAccessor getAccessorInstance() {
        return fAccessor;
    }

    /**
     * @param accessorInstance the accessorInstance to set
     */
    public static void setAccessorInstance(IJsonAccessor accessorInstance) {
        fAccessor = accessorInstance;
    }

    /**
     * Transforms the given array in a {@link JsonArray}.
     * 
     * @param values a list of values to transform in a {@link JsonObject}
     *        object
     * @return a newly created {@link JsonObject}
     */
    public static JsonArray toJsonArray(Object... values) {
        JsonArray array = new JsonArray(values);
        return array;
    }

    /**
     * Transforms the given array in a {@link JsonObject} where all impair
     * objects are used as keys and pair objects are used as the corresponding
     * values.
     * 
     * @param values a list of values to transform in a {@link JsonObject}
     *        object
     * @return a newly created {@link JsonObject}
     */
    public static JsonObject toJsonObject(Object... values) {
        JsonObject obj = new JsonObject();
        addValues(obj, values);
        return obj;
    }

    /**
     * Transforms the given java object into a JSON object.
     * 
     * @param value the java object to transform into a JSON instance
     * @return a newly created JSON object
     */
    public static Object toJsonValue(Object value) {
        return toJsonValue(value, null);
    }

    /**
     * Transforms the given java object into a JSON object.
     * 
     * @param value the java object to transform into a JSON instance
     * @return a newly created JSON object
     */
    private static Object toJsonValue(Object value, Set<Object> stack) {
        if (value == null) {
            return null;
        }
        Set<Object> s = stack;
        if (s != null) {
            if (s.contains(value)) {
                return null;
            }
            s.add(value);
        }
        try {
            Object val = value;
            if (value instanceof Map<?, ?>) {
                Object obj = fAccessor.newObject();
                for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                    String name = entry.getKey() + "";
                    if (stack == null) {
                        stack = new HashSet<Object>();
                    }
                    Object v = toJsonValue(entry.getValue(), stack);
                    fAccessor.setValue(obj, name, v);
                }
                val = obj;
            } else if (value instanceof Iterable<?>) {
                Object array = fAccessor.newArray();
                int pos = 0;
                for (Object o : (Iterable<?>) value) {
                    if (stack == null) {
                        stack = new HashSet<Object>();
                    }
                    Object v = toJsonValue(o, stack);
                    fAccessor.setArrayValue(array, pos, v);
                    pos++;
                }
                val = array;
            } else if (value.getClass().isArray()) {
                Object array = fAccessor.newArray();
                int pos = 0;
                for (Object o : (Object[]) value) {
                    if (stack == null) {
                        stack = new HashSet<Object>();
                    }
                    Object v = toJsonValue(o, stack);
                    fAccessor.setArrayValue(array, pos, v);
                    pos++;
                }
                val = array;
            } else {
                if (value instanceof JsonValue) {
                    val = ((JsonValue) value).fObject;
                } else if ((value instanceof Integer)
                    || (value instanceof Boolean)
                    || (value instanceof Long)
                    || (value instanceof Short)
                    || (value instanceof Double)
                    || (value instanceof Float)
                    || (value instanceof String)) {
                    // do nothing
                } else {
                    val = value.toString();
                }
            }
            return val;
        } finally {
            if (s != null) {
                s.remove(value);
            }
        }
    }

    protected Object fObject;

    /**
     * @param object
     */
    public JsonValue(Object object) {
        setJsonObject(object);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof JsonValue)) {
            return false;
        }
        JsonValue o = (JsonValue) obj;
        if (fObject == o.fObject || fObject.equals(o.fObject)) {
            return true;
        }
        Object first = fObject;
        Object second = o.fObject;
        return fAccessor.equals(first, second);
    }

    /**
     * Returns the native JSON object.
     * 
     * @return the "native" JSON object
     */
    public Object getJsonObject() {
        return fObject;
    }

    @Override
    public int hashCode() {
        return fAccessor.hashCode(fObject);
    }

    protected abstract Object newJsonInstance();

    /**
     * Replaces the old JSON internal object by the given one.
     * 
     * @param json the JSON object to set.
     */
    @SuppressWarnings("unchecked")
    public <T extends JsonValue> T setJsonObject(Object json) {
        if (json instanceof JsonValue) {
            json = ((JsonValue) json).fObject;
        } else if (json instanceof String) {
            String str = (String) json;
            str = str.trim();
            if (!"".equals(str)) {
                Object result = setJsonObject(fAccessor.parse(str));
                return (T) result;
            } else {
                json = newJsonInstance();
            }
        } else if (json == null) {
            json = newJsonInstance();
        } else {
            if (fAccessor.getType(json) == JsonType.NONE) {
                // Copy the object only if it has an unknown type
                json = toJsonValue(json);
            }
        }
        fObject = json;
        return (T) this;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return fAccessor.serialize(fObject);
    }

}
