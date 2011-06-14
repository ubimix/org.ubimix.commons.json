/**
 * 
 */
package org.webreformatter.commons.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author kotelnikov
 */
public class JsonObjectVisitor extends AbstractObjectVisitor {

    private IJsonAccessor fAccessor;

    /**
     * 
     */
    public JsonObjectVisitor() {
        this(JsonAccessor.getInstance());
    }

    /**
     * 
     */
    public JsonObjectVisitor(IJsonAccessor accessor) {
        fAccessor = accessor;
    }

    /**
     * Transforms the given java object in a sequence of JSON listener calls.
     * 
     * @param value the java object to transform in JSON calls
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
        if (stack.contains(value)) {
            return;
        }
        stack.add(value);
        try {
            switch (fAccessor.getType(value)) {
                case ARRAY:
                    listener.beginArray();
                    int len = fAccessor.getArraySize(value);
                    for (int i = 0; i < len; i++) {
                        Object o = fAccessor.getArrayValue(value, i);
                        listener.beginArrayElement();
                        visit(o, sort, listener, stack, true);
                        listener.endArrayElement();
                    }
                    listener.endArray();
                    break;
                case BOOLEAN:
                    listener.onValue(fAccessor.toBoolean(value));
                    break;
                case DOUBLE:
                    listener.onValue(fAccessor.toDouble(value));
                    break;
                case INTEGER:
                    listener.onValue(fAccessor.toInteger(value));
                    break;
                case LONG:
                    listener.onValue(fAccessor.toLong(value));
                    break;
                case OBJECT:
                    Collection<String> keys = fAccessor.getObjectKeys(value);
                    if (sort) {
                        List<String> list = new ArrayList<String>(keys);
                        Collections.sort(list);
                        keys = list;
                    }
                    listener.beginObject();
                    for (String key : keys) {
                        listener.beginObjectProperty(key);
                        Object v = fAccessor.getValue(value, key);
                        visit(v, sort, listener, stack, true);
                        listener.endObjectProperty(key);
                    }
                    listener.endObject();
                    break;
                case STRING:
                case NONE:
                    listener.onValue(fAccessor.toString(value));
                    break;
            }
        } finally {
            stack.remove(value);
        }
    }

}
