package org.webreformatter.commons.json;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(AllTests.class.getName());
        // $JUnit-BEGIN$
        suite.addTestSuite(JsonParserTest.class);
        suite.addTestSuite(JsonHelperTest.class);
        suite.addTestSuite(JsonSerializeDeserializeTest.class);
        suite.addTestSuite(JsonObjectTest.class);
        // $JUnit-END$
        return suite;
    }

}
