/**
 * 
 */
package org.ubimix.commons.json;

/**
 * This class is used to convert JSON to Java objects and vice versa.
 * 
 * @author kotelnikov
 */
public class JsonObjectBuilder extends AbstractObjectBuilder {

    private IJsonAccessor fAccessor;

    public JsonObjectBuilder() {
        this(JsonAccessor.getInstance());
    }

    public JsonObjectBuilder(IJsonAccessor accessor) {
        fAccessor = accessor;
    }

    @Override
    protected void addObjectValue(Object obj, String property, Object value) {
        fAccessor.setValue(obj, property, value);
    }

    @Override
    protected void addToArray(Object array, Object value) {
        int size = fAccessor.getArraySize(array);
        fAccessor.setArrayValue(array, size, value);
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
        return fAccessor.equals(o.fAccessor) && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    protected Object newArray() {
        return fAccessor.newArray();
    }

    @Override
    protected Object newObject() {
        return fAccessor.newObject();
    }

    @Override
    protected String toString(Object top) {
        return top.toString();
    }

}
