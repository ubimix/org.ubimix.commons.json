/**
 * 
 */
package org.webreformatter.commons.json;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author kotelnikov
 */
public class JavaObjectVisitor extends AbstractObjectVisitor {

    /**
     * Transforms the given java object in a JSON object.
     * 
     * @param value the java object to transform in a JSON instance
     * @return a newly created JSON object
     */
    @Override
    protected void visit(
        Object value,
        boolean sort,
        IJsonListener listener,
        Set<Object> stack,
        boolean acceptNull) {
        if (value == null && !acceptNull) {
            return;
        }
        Set<Object> s = stack;
        if (s != null) {
            if (s.contains(value)) {
                return;
            }
            s.add(value);
        }
        try {
            if (value instanceof Map<?, ?>) {
                listener.beginObject();
                for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                    String name = entry.getKey() + "";
                    if (stack == null) {
                        stack = new HashSet<Object>();
                    }
                    listener.beginObjectProperty(name);
                    visit(entry.getValue(), sort, listener, stack, true);
                    listener.endObjectProperty(name);
                }
                listener.endObject();
            } else if (value instanceof Iterable<?>) {
                listener.beginArray();
                int pos = 0;
                for (Object o : (Iterable<?>) value) {
                    if (stack == null) {
                        stack = new HashSet<Object>();
                    }
                    visit(o, sort, listener, stack, true);
                    pos++;
                }
                listener.endArray();
            } else if (value.getClass().isArray()) {
                listener.beginArray();
                int pos = 0;
                for (Object o : (Object[]) value) {
                    if (stack == null) {
                        stack = new HashSet<Object>();
                    }
                    visit(o, sort, listener, stack, true);
                    pos++;
                }
                listener.endArray();
            } else if (value instanceof JsonObject) {
                Object o = ((JsonObject) value).getJsonObject();
                if (stack == null) {
                    stack = new HashSet<Object>();
                }
                visit(o, sort, listener, stack, true);
            } else if (value instanceof Integer) {
                listener.onValue((Integer) value);
            } else if (value instanceof Long) {
                listener.onValue((Long) value);
            } else if (value instanceof Short) {
                listener.onValue((Short) value);
            } else if (value instanceof Character) {
                listener.onValue((Character) value);
            } else if (value instanceof Byte) {
                listener.onValue((Byte) value);
            } else if (value instanceof Double) {
                listener.onValue((Double) value);
            } else if (value instanceof Float) {
                listener.onValue((Float) value);
            } else if (value instanceof Boolean) {
                listener.onValue((Boolean) value);
            } else if (value instanceof String) {
                listener.onValue((String) value);
            } else {
                String str = value != null ? value.toString() : null;
                listener.onValue(str);
            }
        } finally {
            if (s != null) {
                s.remove(value);
            }
        }
    }

}
