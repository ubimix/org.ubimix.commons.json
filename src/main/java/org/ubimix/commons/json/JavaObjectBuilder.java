/**
 * 
 */
package org.ubimix.commons.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kotelnikov
 */
public class JavaObjectBuilder extends AbstractObjectBuilder {

    /**
     * 
     */
    public JavaObjectBuilder() {
    }

    @Override
    protected void addObjectValue(Object obj, String property, Object value) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) obj;
        map.put(property, value);
    }

    @Override
    protected void addToArray(Object array, Object value) {
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) array;
        list.add(value);
    }

    @Override
    protected Object newArray() {
        return new ArrayList<Object>();
    }

    @Override
    protected Object newObject() {
        return new LinkedHashMap<String, Object>();
    }

    @Override
    protected String toString(Object top) {
        return top != null ? top.toString() : null;
    }

}
