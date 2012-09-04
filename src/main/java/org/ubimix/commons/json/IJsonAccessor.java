/**
 * 
 */
package org.ubimix.commons.json;

import java.util.Set;

/**
 * Objects of this type provide access to JSON instances. This interface was
 * introduced to provide a common API to work with JSON on the server and in GWT
 * clients.
 * 
 * @author kotelnikov
 */
public interface IJsonAccessor {

    /**
     * Possible JSON types
     * 
     * @author kotelnikov
     */
    public enum JsonType {
        /**
         * JSON array
         */
        ARRAY,
        /**
         * Boolean value
         */
        BOOLEAN,
        /**
         * Double value
         */
        DOUBLE,
        /**
         * Integer value
         */
        INTEGER,
        /**
         * Long value
         */
        LONG,
        /**
         * Not defined
         */
        NONE,
        /**
         * An object. Each object can contain properties.
         */
        OBJECT,
        /**
         * String value.
         */
        STRING
    }

    /**
     * Adds a new value in the specified array.
     * 
     * @param array the array
     * @param pos the position in the array where a new element should be
     *        inserted; this value should be in the range [0,size] where
     *        <code>size</code> is the current length of the array.
     * @param value the value to set
     */
    void addArrayValue(Object array, int pos, Object value);

    /**
     * @param first the first JSON object to compare
     * @param second the second JSON object to compare
     * @return <code>true</code> if the given JSON objects are equal
     */
    boolean equals(Object first, Object second);

    /**
     * Returns the size of the given array object. If the given object is not an
     * array then this method should return 0.
     * 
     * @param array the array object
     * @return the size of the array
     */
    int getArraySize(Object array);

    /**
     * Returns the value of the array from the specified position.
     * 
     * @param array the array
     * @param pos the position in the array; this value should be in the range
     *        [0,size) where <code>size</code> is the current length of the
     *        array.
     * @return the value from the specified position
     */
    Object getArrayValue(Object array, int pos);

    /**
     * Returns a set of all property names (keys) associated with the given
     * object.
     * 
     * @param object the JSON object
     * @return a set of all property names (keys) associated with the given
     *         object
     */
    Set<String> getObjectKeys(Object object);

    /**
     * Returns the type of the specified object. If this method does not
     * recognizes the value as a valid object then it returns <code>null</code>.
     * 
     * @param obj the object to detect
     * @return a valid JSON type or <code>null</code> if the type could not be
     *         recognized.
     */
    JsonType getType(Object obj);

    /**
     * Returns the value of the specified property from the given JSON object.
     * 
     * @param obj the JSON object
     * @param key the name of the property
     * @return the string value of the property with the specified name
     */
    Object getValue(Object obj, String key);

    /**
     * @param object the JSON object
     * @return the hash code of the given JSON object
     */
    int hashCode(Object object);

    /**
     * Creates and returns a newly created JSON array.
     * 
     * @return a newly created JSON array.
     */
    Object newArray();

    /**
     * Creates and returns a new JSON object.
     * 
     * @return a newly created JSON object.
     */
    Object newObject();

    /**
     * Parses the specified string and returns the corresponding JSON object.
     * 
     * @param str the string to parse
     * @return a JSON object
     */
    Object parse(String str);

    /**
     * Removes the value from the specified array position.
     * 
     * @param array the array
     * @param pos the position where the element should be remove; this value
     *        should be in the range [0,size) where <code>size</code> is the
     *        current length of the array.
     */
    void removeArrayValue(Object array, int pos);

    /**
     * Removes the object property with the specified name.
     * 
     * @param object the object where the property should be removed.
     * @param name the name of the property to remove
     */
    void removeValue(Object object, String name);

    /**
     * Returns the serialized value of the specified JSON object.
     * 
     * @param object the object to serialize
     * @return the serialized value of the specified JSON object
     */
    String serialize(Object object);

    /**
     * Sets a new value in the specified position.
     * 
     * @param array the array
     * @param pos the position in the array where a new element should be
     *        stored; this value should be in the range [0,size] where
     *        <code>size</code> is the current length of the array.
     * @param value the value to set
     */
    void setArrayValue(Object array, int pos, Object value);

    /**
     * Sets the value of the property. The value to set should be one of the
     * following types: a JSON array, a JSON object, an integer value, a string
     * or a double. The <code>null</code> is acceptable and is used to remove
     * the property. For all other values this method should rise an exception.
     * 
     * @param obj the JSON object
     * @param name the name of the property
     * @param value the value to set
     */
    void setValue(Object obj, String name, Object value);

    /**
     * @param value a JSON object
     * @return a boolean value corresponding to the given JSON object
     */
    boolean toBoolean(Object value);

    /**
     * @param value a JSON object
     * @return a double value corresponding to the given JSON object
     */
    double toDouble(Object value);

    /**
     * @param value a JSON object
     * @return an integer value corresponding to the given JSON object
     */
    int toInteger(Object value);

    /**
     * @param value a JSON object
     * @return an long value corresponding to the given JSON object
     */
    long toLong(Object value);

    /**
     * @param value a JSON object
     * @return a string value corresponding to the given JSON object
     */
    String toString(Object value);

}
