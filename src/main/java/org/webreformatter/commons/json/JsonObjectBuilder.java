/**
 * 
 */
package org.webreformatter.commons.json;

import java.util.Stack;

/**
 * This class is used to convert JSON to Java objects and vice versa.
 * 
 * @author kotelnikov
 */
public class JsonObjectBuilder implements IJsonListener {

    private IJsonAccessor fAccessor;

    private Stack<Object> fStack = new Stack<Object>();

    private Object fTop;

    private Object fValue;

    public JsonObjectBuilder() {
        this(JsonAccessor.getInstance());
    }

    public JsonObjectBuilder(IJsonAccessor accessor) {
        fAccessor = accessor;
    }

    public void beginArray() {
        Object array = fAccessor.newArray();
        if (fTop == null) {
            fTop = array;
        }
        fStack.push(array);
    }

    public void beginArrayElement() {
        fValue = null;
    }

    public void beginObject() {
        Object object = fAccessor.newObject();
        if (fTop == null) {
            fTop = object;
        }
        fStack.push(object);
    }

    public void beginObjectProperty(String property) {
    }

    public void endArray() {
        fValue = fStack.pop();
    }

    public void endArrayElement() {
        Object array = fStack.peek();
        int size = fAccessor.getArraySize(array);
        fAccessor.setArrayValue(array, size, fValue);
        fValue = null;
    }

    public void endObject() {
        fValue = fStack.pop();
    }

    public void endObjectProperty(String property) {
        Object obj = fStack.peek();
        fAccessor.setValue(obj, property, fValue);
        fValue = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof JsonObjectBuilder)) {
            return false;
        }
        JsonObjectBuilder o = (JsonObjectBuilder) obj;
        return fAccessor.equals(o.fAccessor)
            && (fTop != null && o.fTop != null
                ? fTop.equals(o.fTop)
                : fTop == o.fTop);
    }

    public Object getTop() {
        return fTop;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void onValue(boolean value) {
        fValue = value;
    }

    public void onValue(double value) {
        fValue = value;
    }

    public void onValue(int value) {
        fValue = value;
    }

    public void onValue(long value) {
        fValue = value;
    }

    public void onValue(String value) {
        fValue = value;
    }

    public void reset() {
        fStack.clear();
        fTop = null;
        fValue = null;
    }

    @Override
    public String toString() {
        return fAccessor.toString(fTop);
    }

}
