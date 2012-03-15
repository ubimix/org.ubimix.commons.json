/**
 * 
 */
package org.webreformatter.commons.json.ext;

import junit.framework.TestCase;

/**
 * @author kotelnikov
 */
public class FormattedDateTest extends TestCase {

    /**
     * @param name
     */
    public FormattedDateTest(String name) {
        super(name);
    }

    public void testDates() throws Exception {
        String str = "2011-03-08T07:59:27Z";
        FormattedDate date = new FormattedDate(str);
        assertEquals(2011, date.getYear());
        assertEquals(3, date.getMonth());
        assertEquals(8, date.getDay());
        assertEquals(7, date.getHour());
        assertEquals(59, date.getMinutes());
        assertEquals(27, date.getSeconds());
        assertEquals(str, date.toString());

        try {
            new FormattedDate("2011-03-08T07:99:27Z");
            fail();
        } catch (IllegalStateException e) {
        }
        try {
            new FormattedDate("2011-03-08T37:59:27Z");
            fail();
        } catch (IllegalStateException e) {
        }

        assertEquals(2011, date.getYear());
        date.setYear(2020);
        assertEquals(2020, date.getYear());

        assertEquals(3, date.getMonth());
        date.setMonth(5);
        assertEquals(5, date.getMonth());

        assertEquals(8, date.getDay());
        date.setDay(11);
        assertEquals(11, date.getDay());

        assertEquals(7, date.getHour());
        date.setHour(18);
        assertEquals(18, date.getHour());

        assertEquals(59, date.getMinutes());
        date.setMinutes(2);
        assertEquals(2, date.getMinutes());

        assertEquals(27, date.getSeconds());
        date.setSeconds(8);
        assertEquals(8, date.getSeconds());

        assertEquals("2020-05-11T18:02:08Z", date.toString());

    }

}
