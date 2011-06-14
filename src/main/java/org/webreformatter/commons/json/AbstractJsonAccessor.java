/**
 * 
 */
package org.webreformatter.commons.json;

/**
 * @author kotelnikov
 */
public abstract class AbstractJsonAccessor implements IJsonAccessor {

    /**
     * 
     */
    public AbstractJsonAccessor() {
    }

    /**
     * @see org.webreformatter.commons.json.IJsonAccessor#equals(java.lang.Object,
     *      java.lang.Object)
     */
    public boolean equals(Object first, Object second) {
        if (first == null || second == null) {
            return first == second;
        }
        if (first.equals(second)) {
            return true;
        }
        JsonType firstType = getType(first);
        JsonType secondType = getType(second);
        if (firstType != secondType) {
            return false;
        }
        String firstStr = toString(first);
        String secondStr = toString(second);
        return firstStr.equals(secondStr);

    }

    /**
     * @see org.webreformatter.commons.json.IJsonAccessor#hashCode(java.lang.Object)
     */
    public int hashCode(Object object) {
        return object != null ? object.hashCode() : 0;
    }

    /**
     * @see org.webreformatter.commons.json.IJsonAccessor#toBoolean(java.lang.Object)
     */
    public boolean toBoolean(Object value) {
        return value instanceof Boolean && ((Boolean) value).booleanValue();
    }

    /**
     * @see org.webreformatter.commons.json.IJsonAccessor#toDouble(java.lang.Object)
     */
    public double toDouble(Object value) {
        if (!(value instanceof Double)) {
            return 0;
        }
        return ((Double) value).doubleValue();
    }

    /**
     * @see org.webreformatter.commons.json.IJsonAccessor#toInteger(java.lang.Object)
     */
    public int toInteger(Object value) {
        if (!(value instanceof Integer)) {
            return 0;
        }
        return ((Integer) value).intValue();
    }

    /**
     * @see org.webreformatter.commons.json.IJsonAccessor#toLong(java.lang.Object)
     */
    public long toLong(Object value) {
        if (!(value instanceof Long)) {
            return 0;
        }
        return ((Long) value).longValue();
    }

    /**
     * @see org.webreformatter.commons.json.IJsonAccessor#toString(java.lang.Object)
     */
    public String toString(Object value) {
        return value != null ? value.toString() : null;
    }

}
