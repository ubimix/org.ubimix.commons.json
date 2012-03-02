package org.webreformatter.commons.json.rpc;

import junit.framework.TestCase;

import org.webreformatter.commons.json.JsonArray;
import org.webreformatter.commons.json.JsonObject;
import org.webreformatter.commons.json.JsonValue;
import org.webreformatter.commons.json.rpc.IRpcCallHandler.IRpcCallback;

public class RpcDispatcherTest extends TestCase {

    public static class UserProfile extends JsonObject {
        public static IJsonValueFactory<UserProfile> FACTORY = new IJsonValueFactory<UserProfile>() {
            public UserProfile newValue(Object object) {
                return new UserProfile().setJsonObject(object);
            }
        };

        public UserProfile(Object... fields) {
            addValues(this, fields);
        }

        public int getAge() {
            return getInteger("age", 0);
        }

        public String getFirstName() {
            return getString("firstName");
        }

        public String getLastName() {
            return getString("lastName");
        }
    }

    public RpcDispatcherTest(String name) {
        super(name);
    }

    public void test() {
        RpcDispatcher dispatcher = new RpcDispatcher();

        String method = "sayHello";
        String firstName = "John";
        String lastName = "Smith";
        int age = 38;
        RpcRequest request = new RpcRequest()
            .<RpcRequest> setId(123)
            .<RpcRequest> setMethod(method)
            .<RpcRequest> setParamObject(
                "firstName",
                firstName,
                "lastName",
                lastName,
                "age",
                age);
        final RpcResponse[] response = { null };
        IRpcCallback callback = new IRpcCallback() {
            public void finish(RpcResponse r) {
                response[0] = r;
            }
        };
        dispatcher.handle(request, callback);
        assertNotNull(response[0]);
        assertTrue(response[0].hasErrors());

        // Add a new handler and test the call
        dispatcher.registerHandler(method, new IRpcCallHandler() {
            public void handle(RpcRequest request, IRpcCallback callback) {
                UserProfile params = request.getParams(UserProfile.FACTORY);
                String message = "Hello "
                    + params.getFirstName()
                    + " "
                    + params.getLastName()
                    + "!";
                RpcResponse response = new RpcResponse().setResultObject(
                    "message",
                    message,
                    "age",
                    params.getAge());
                callback.finish(response);
            }
        });
        dispatcher.handle(request, callback);
        assertNotNull(response[0]);
        assertFalse(response[0].hasErrors());
        JsonObject result = response[0].getResultObject();
        assertNotNull(result);
        String message = "Hello " + firstName + " " + lastName + "!";
        assertEquals(message, result.getString("message"));

        // Register another handler. It sets a message (string value)
        // directly as a response.
        response[0] = null;
        dispatcher.registerHandler(method, new IRpcCallHandler() {
            public void handle(RpcRequest request, IRpcCallback callback) {
                UserProfile params = request.getParams(UserProfile.FACTORY);
                String message = "Hello "
                    + params.getFirstName()
                    + " "
                    + params.getLastName()
                    + "!";
                RpcResponse response = new RpcResponse().setResult(message);
                callback.finish(response);
            }
        });
        dispatcher.handle(request, callback);
        assertNotNull(response[0]);
        assertFalse(response[0].hasErrors());
        Object val = response[0].getResult();
        assertNotNull(val);
        assertTrue(val instanceof String);
        assertEquals(message, val);
        JsonValue jsonVal = response[0].getResultAsJson();
        assertNotNull(jsonVal);
        assertTrue(jsonVal instanceof JsonArray);
        JsonArray array = (JsonArray) jsonVal;
        assertEquals(message, array.getString(0));

        // Remove handlers for the specified method and check that the
        // result is not handled.
        dispatcher.unregisterHandler(method);
        response[0] = null;
        dispatcher.handle(request, callback);
        assertNotNull(response[0]);
        assertTrue(response[0].hasErrors());
    }
}
