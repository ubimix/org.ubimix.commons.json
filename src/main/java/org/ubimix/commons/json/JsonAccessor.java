/**
 * 
 */
package org.ubimix.commons.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author kotelnikov
 */
public class JsonAccessor extends AbstractJsonAccessor {

    private static JsonAccessor fInstance;

    public static JsonAccessor getInstance() {
        if (fInstance == null) {
            fInstance = new JsonAccessor();
        }
        return fInstance;
    }

    /**
     * 
     */
    public JsonAccessor() {
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#addArrayValue(java.lang.Object,
     *      int, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public void addArrayValue(Object array, int pos, Object value) {
        try {
            List<Object> a = (List<Object>) array;
            for (int i = a.size() - 1; i > pos; i++) {
                Object o = a.get(i - 1);
                a.add(i, o);
            }
            a.add(pos, value);
        } catch (Throwable e) {
            throw handleError("Can not add the given value in the array", e);
        }
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#getArraySize(java.lang.Object)
     */
    public int getArraySize(Object array) {
        List<?> list = (List<?>) array;
        return list.size();
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#getArrayValue(java.lang.Object,
     *      int)
     */
    public Object getArrayValue(Object array, int pos) {
        List<?> a = (List<?>) array;
        Object o = a.get(pos);
        Object value = getJavaValue(o);
        return value;
    }

    private Object getJavaValue(Object o) {
        Object result = null;
        if ((o instanceof List<?>)
            || (o instanceof Map<?, ?>)
            || (o instanceof String)
            || (o instanceof Boolean)
            || (o instanceof Integer)
            || (o instanceof Long)
            || (o instanceof Double)) {
            result = o;
        } else if ((o instanceof Byte) || (o instanceof Short)) {
            result = Integer.valueOf(o.toString());
        } else if ((o instanceof Double)) {
            result = Double.valueOf(o.toString());
        }
        return result;
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#getObjectKeys(java.lang.Object)
     */
    public Set<String> getObjectKeys(Object obj) {
        Set<String> result = new LinkedHashSet<String>();
        Map<?, ?> o = (Map<?, ?>) obj;
        for (Object key : o.keySet()) {
            String name = getString(key);
            result.add(name);
        }
        return result;
    }

    private String getString(Object key) {
        return key != null ? key.toString() : null;
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#getType(java.lang.Object)
     */
    public JsonType getType(Object o) {
        JsonType result = JsonType.NONE;
        if (o instanceof List<?>) {
            result = JsonType.ARRAY;
        } else if (o instanceof Map<?, ?>) {
            result = JsonType.OBJECT;
        } else if (o instanceof String) {
            result = JsonType.STRING;
        } else if (o instanceof Boolean) {
            result = JsonType.BOOLEAN;
        } else if (o instanceof Integer) {
            result = JsonType.INTEGER;
        } else if (o instanceof Long) {
            result = JsonType.LONG;
        } else if (o instanceof Double) {
            result = JsonType.DOUBLE;
        }
        return result;
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#getValue(java.lang.Object,
     *      java.lang.String)
     */
    public Object getValue(Object obj, String key) {
        Map<?, ?> map = (Map<?, ?>) obj;
        Object val = map.get(key);
        val = getJavaValue(val);
        return val;
    }

    private RuntimeException handleError(String msg, Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        }
        return new RuntimeException(t);
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#newArray()
     */
    public Object newArray() {
        return new ArrayList<Object>();
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#newObject()
     */
    public Object newObject() {
        return new LinkedHashMap<Object, Object>();
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#parse(java.lang.String)
     */
    public Object parse(String str) {
        try {
            JsonParser parser = new JsonParser();
            JsonObjectBuilder util = new JsonObjectBuilder();
            parser.parse(str, util);
            Object obj = util.getTop();
            return obj;
        } catch (Throwable e) {
            throw handleError(
                "Can not parse the given string. String: " + str,
                e);
        }
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#removeArrayValue(java.lang.Object,
     *      int)
     */
    public void removeArrayValue(Object array, int pos) {
        List<?> a = (List<?>) array;
        a.remove(pos);
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#removeValue(java.lang.Object,
     *      java.lang.String)
     */
    public void removeValue(Object object, String name) {
        Map<?, ?> o = (Map<?, ?>) object;
        o.remove(name);
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#serialize(java.lang.Object)
     */
    public String serialize(Object object) {
        final StringBuilder buf = new StringBuilder();
        JsonSerializer serializer = new JsonSerializer(2) {
            @Override
            protected void print(String string) {
                buf.append(string);
            }
        };
        new JsonObjectVisitor(this).visit(object, serializer);
        return buf.toString();
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#setArrayValue(java.lang.Object,
     *      int, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public void setArrayValue(Object array, int pos, Object value) {
        try {
            List<Object> a = (List<Object>) array;
            if (pos == a.size()) {
                a.add(value);
            } else {
                a.set(pos, value);
            }
        } catch (Throwable e) {
            throw handleError("Can not set the given value in the array", e);
        }
    }

    /**
     * @see org.ubimix.commons.json.IJsonAccessor#setValue(java.lang.Object,
     *      java.lang.String, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public void setValue(Object obj, String name, Object value) {
        try {
            Map<Object, Object> o = (Map<Object, Object>) obj;
            o.put(name, value);
        } catch (Throwable e) {
            throw handleError(""
                + "Can not add the specified property to the object. "
                + "Property name: '"
                + name
                + "'. Value: '"
                + value
                + "'.", e);
        }
    }

}
