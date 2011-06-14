/**
 * 
 */
package org.webreformatter.commons.json;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author kotelnikov
 */
// getArray(String, boolean)
// getDouble(String, double)
// getInteger(String, int)
// getKeys()
// getList(String, IJsonValueFactory<W>)
// getLong(String, long)
// getSet(String, IJsonValueFactory<W>)
// getString(String)
// getObject(String, IJsonValueFactory<W>)
// removeValue(String)
// setValue(String, Object)
// setValues(String, Iterable<?>)
// setValues(String, T...)
public class JsonObjectTest extends TestCase {

    public static class MyList extends JsonObject {
        public List<MyValue> getList() {
            return getList("list", MyValue.FACTORY);
        }

        public String getTitle() {
            return getString("title");
        }

        public MyList setList(List<MyValue> values) {
            setValue("list", values);
            return this;
        }

        public MyList setTitle(String title) {
            setValue("title", title);
            return this;
        }
    }

    public static class MyValue extends JsonObject {
        public static IJsonValueFactory<MyValue> FACTORY = new IJsonValueFactory<JsonObjectTest.MyValue>() {
            public MyValue newValue(Object object) {
                return new MyValue().setJsonObject(object);
            }
        };

        protected MyValue() {
        }

        public MyValue(int id) {
            setValue("id", id);
        }

        public int getId() {
            return getInteger("id", -1);
        }
    }

    /**
     * @param name
     */
    public JsonObjectTest(String name) {
        super(name);
    }

    public void testBoolean() {
        JsonObject o = new JsonObject();
        o.setValue("a", true);
        assertEquals(true, o.getBoolean("a", false));
        o.setValue("a", false);
        assertEquals(false, o.getBoolean("a", true));
    }

    public void testDouble() {
        JsonObject o = new JsonObject();
        Double value = 0.3;
        o.setValue("a", value);
        assertEquals(value, o.getDouble("a", -123));
        value = -0.3;
        o.setValue("a", value);
        assertEquals(value, o.getDouble("a", 123));
    }

    public void testInteger() {
        JsonObject o = new JsonObject();
        Integer value = 345;
        o.setValue("a", value);
        assertEquals((int) value, o.getInteger("a", -123));
        value = -345;
        o.setValue("a", value);
        assertEquals((int) value, o.getInteger("a", 123));
    }

    public void testList() throws Exception {
        List<MyValue> list = new ArrayList<JsonObjectTest.MyValue>();
        int i = 0;
        list.add(new MyValue(i++));
        list.add(new MyValue(i++));
        list.add(new MyValue(i++));
        list.add(new MyValue(i++));
        list.add(new MyValue(i++));
        MyList obj = new MyList().setTitle("This is a title").setList(list);
        assertEquals("This is a title", obj.getTitle());
        assertEquals(list, obj.getList());
    }

    public void testLong() {
        JsonObject o = new JsonObject();
        Long value = ((long) Integer.MAX_VALUE) + 10000;
        o.setValue("a", value);
        assertEquals((long) value, o.getLong("a", -123));
        value = ((long) Integer.MIN_VALUE) - 10000;
        o.setValue("a", value);
        assertEquals((long) value, o.getLong("a", 123));
    }

}
