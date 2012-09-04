/**
 * 
 */
package org.ubimix.commons.json;

import java.util.Stack;

/**
 * @author kotelnikov
 */
public abstract class AbstractObjectBuilder implements IJsonListener {
    private Stack<Object> fStack = new Stack<Object>();

    private Object fTop;

    private Object fValue;

    public AbstractObjectBuilder() {
    }

    protected abstract void addObjectValue(
        Object obj,
        String property,
        Object value);

    protected abstract void addToArray(Object array, Object value);

    public void beginArray() {
        Object array = newArray();
        if (fTop == null) {
            fTop = array;
        }
        fStack.push(array);
    }

    public void beginArrayElement() {
        fValue = null;
    }

    public void beginObject() {
        Object object = newObject();
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
        addToArray(array, fValue);
        fValue = null;
    }

    public void endObject() {
        fValue = fStack.pop();
    }

    public void endObjectProperty(String property) {
        Object obj = fStack.peek();
        addObjectValue(obj, property, fValue);
        fValue = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractObjectBuilder)) {
            return false;
        }
        AbstractObjectBuilder o = (AbstractObjectBuilder) obj;
        return (fTop != null && o.fTop != null
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

    protected abstract Object newArray();

    protected abstract Object newObject();

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
        return toString(fTop);
    }

    protected abstract String toString(Object top);

}
