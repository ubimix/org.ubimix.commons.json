package org.webreformatter.commons.json.rpc;

import org.webreformatter.commons.json.JsonArray;
import org.webreformatter.commons.json.JsonObject;
import org.webreformatter.commons.json.JsonValue;

/**
 * This object is used to represent RPC response objects.
 * 
 * @author kotelnikov
 * @see http://groups.google.com/group/json-rpc/web/json-rpc-2-0
 */
public class RpcResponse extends RpcObject {

    /**
     * Factory for {@link RpcResponse} instances.
     */
    public static final IJsonValueFactory<RpcResponse> FACTORY = new IJsonValueFactory<RpcResponse>() {
        public RpcResponse newValue(Object object) {
            return new RpcResponse().setJsonObject(object);
        }
    };

    /**
     * Name of the error field.
     */
    private static final String KEY_ERROR = "error";

    /**
     * Name of the field with the results of the call.
     */
    public final static String KEY_RESULT = "result";

    /**
     * The default constructor;
     * 
     * @param request
     */
    public RpcResponse() {
        super();
    }

    /**
     * A copy-constructor. It copies the protocol version and call identifiers
     * from the given object.
     * 
     * @param obj
     */
    public RpcResponse(RpcObject obj) {
        super(obj);
    }

    /**
     * This constructor is used to parse the given string JSON object and set
     * all internal fields.
     * 
     * @param json the serialized JSON object
     */
    public RpcResponse(String json) {
        super(json);
    }

    /**
     * Returns an {@link RpcError} instance if there are errors in this
     * response.
     * 
     * @return an {@link RpcError} instance if there are errors in this response
     */
    public RpcError getError() {
        return getValue(KEY_ERROR, RpcError.FACTORY);
    }

    /**
     * Returns the resulting object.
     * 
     * @return the resulting object
     */
    public Object getResult() {
        Object object = getResult(NULL_FACTORY);
        return object;
    }

    /**
     * Transforms results of the to an object of the required type.
     * 
     * @param <T> the type of the returned value
     * @param factory the factory used to transform the result value to an
     *        object of the required type
     * @return the result of the call
     */
    public <T> T getResult(IJsonValueFactory<T> factory) {
        return getValue(KEY_RESULT, factory);
    }

    /**
     * Returns result value as a generic {@link JsonValue}.
     * 
     * @return result value as a generic {@link JsonValue}.
     * @see #getResult(IJsonValueFactory)
     */
    public JsonValue getResultAsJson() {
        return getResult(JsonValue.VALUE_FACTORY);
    }

    /**
     * Returns the result as a {@link JsonObject}..
     * 
     * @return the result as a {@link JsonObject}.
     */
    public JsonObject getResultObject() {
        return getResult(JsonObject.FACTORY);
    }

    /**
     * Returns <code>true</code> if this response contains errors.
     * 
     * @return <code>true</code> if this response contains errors
     */
    public boolean hasErrors() {
        RpcError error = getError();
        return error != null;
    }

    /**
     * Sets a new error for this RPC response.
     * 
     * @param code the error code.
     * @param message a human-readable message describing the error.
     * @return this object
     */
    public RpcResponse setError(int code, String message) {
        RpcError error = new RpcError(code, message);
        setError(error);
        return this;
    }

    /**
     * Sets a new error for this RPC response.
     * 
     * @param error the error to set
     * @return this object
     */
    public RpcResponse setError(RpcError error) {
        setValue(KEY_ERROR, error);
        return this;
    }

    /**
     * @see org.webreformatter.commons.json.rpc.RpcObject#setId(java.lang.Object)
     */
    @Override
    public RpcResponse setId(Object id) {
        return (RpcResponse) super.setId(id);
    }

    /**
     * Sets a new result value.
     * 
     * @param result the result of the call
     * @return this object
     */
    public RpcResponse setResult(Object result) {
        setValue(KEY_RESULT, result);
        return this;
    }

    /**
     * Sets new result values as an {@link JsonArray} object.
     * 
     * @param values resulting values to set
     * @return this object
     */
    public RpcResponse setResultArray(Object... values) {
        JsonArray array = toJsonArray(values);
        setResult(array);
        return this;
    }

    /**
     * Sets resulting value as a {@link JsonObject}. Impair objects are used as
     * keys and pair objects are the corresponding values.
     * 
     * @param values key/value pairs
     * @return this object
     */
    public RpcResponse setResultObject(Object... values) {
        JsonObject array = toJsonObject(values);
        setResult(array);
        return this;
    }

    @Override
    public RpcResponse setVersion(String version) {
        return (RpcResponse) super.setVersion(version);
    }

}