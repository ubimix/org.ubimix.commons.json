/**
 * 
 */
package org.webreformatter.commons.json.rpc;

import junit.framework.TestCase;

import org.webreformatter.commons.json.JsonArray;
import org.webreformatter.commons.json.JsonObject;
import org.webreformatter.commons.json.JsonValue;

/**
 * @author kotelnikov
 */
public class RpcCallTest extends TestCase {

    /**
     * @param name
     */
    public RpcCallTest(String name) {
        super(name);
    }

    public void testEmptyRequest() {
        RpcRequest request = new RpcRequest();
        assertEquals("2.0", request.getVersion());
    }

    public void testEmptyResponse() {
        RpcResponse resp = new RpcResponse();
        assertEquals("2.0", resp.getVersion());
    }

    public void testParseErrors() {
        RpcError error = new RpcError("{"
            + "     'code': -32601,"
            + "     'message': 'Procedure not found.'"
            + " }");
        assertEquals(-32601, error.getCode());
        assertEquals("Procedure not found.", error.getMessage());

        // Parses completely broken original JSON.
        error = new RpcError("{"
            + "     code: -32601,"
            + "     message: Procedure not found.  ,"
            + "     titi: toto,"
            + "     rdf.type: rdf:Message"
            + " }");
        assertEquals(-32601, error.getCode());
        assertEquals("Procedure not found.", error.getMessage());

    }

    public void testParseRequest() {
        RpcRequest request = new RpcRequest("{"
            + "\"jsonrpc\": \"2.0\", "
            + "\"method\": \"subtract\", "
            + "\"params\": [42, 23], "
            + "\"id\": 1"
            + "}");
        assertEquals("2.0", request.getVersion());
        assertEquals("subtract", request.getMethod());
        assertEquals("1", request.getIdAsString());
        assertEquals(1, request.getId());
        JsonValue params = request.getParams();
        assertNotNull(params);
        assertTrue(params instanceof JsonArray);
        JsonArray array = (JsonArray) params;
        assertEquals(2, array.getSize());
        assertEquals(42, array.getInteger(0, -1));
        assertEquals(23, array.getInteger(1, -1));
    }

    public void testParseResponse() {
        RpcResponse response = new RpcResponse(
            "{\"jsonrpc\": \"2.0\", \"result\": -19, \"id\": 2}");
        assertEquals("2.0", response.getVersion());
        assertEquals(2, response.getId());
        assertEquals(-19, response.getResult());
        JsonValue result = response.getResultAsJson();
        assertNotNull(result);
        assertTrue(result instanceof JsonArray);
        JsonArray array = (JsonArray) result;
        assertEquals(1, array.getSize());
        assertEquals(-19, array.getInteger(0, Integer.MAX_VALUE));

        response = new RpcResponse(
            "{\"jsonrpc\": \"2.0\", \"result\": [\"hello\", 5], \"id\": \"9\"}");
        assertNull(response.getError());
        assertEquals("2.0", response.getVersion());
        assertEquals("9", response.getId());
        result = response.getResultAsJson();
        assertNotNull(result);
        assertTrue(result instanceof JsonArray);
        array = (JsonArray) result;
        assertEquals(2, array.getSize());
        assertEquals("hello", array.getObject(0));
        assertEquals(5, array.getObject(1));

        response = new RpcResponse(
            "{\"jsonrpc\": \"2.0\", \"result\": {a:b, c:5}, \"id\": 123.45}");
        assertNull(response.getError());
        assertEquals("2.0", response.getVersion());
        assertEquals(123.45, response.getId());
        result = response.getResultAsJson();
        assertNotNull(result);
        assertTrue(result instanceof JsonObject);
        JsonObject obj = (JsonObject) result;
        assertEquals("b", obj.getValue("a"));
        assertEquals(5, obj.getValue("c"));

        response = new RpcResponse(""
            + "{"
            + " 'jsonrpc': '2.0',"
            + " 'error': {"
            + "     'code': -32601,"
            + "     'message': 'Procedure not found.'"
            + " },"
            + " 'id': '1'"
            + "}");
        assertEquals("2.0", response.getVersion());
        assertEquals("1", response.getId());
        result = response.getResultAsJson();
        assertNull(result);

        RpcError error = response.getError();
        assertNotNull(error);
        assertEquals("Procedure not found.", error.getMessage());
        assertEquals(-32601, error.getCode());
    }
}
