package org.webreformatter.commons.json.rpc;

import org.webreformatter.commons.json.JsonArray;
import org.webreformatter.commons.json.JsonObject;
import org.webreformatter.commons.json.JsonValue;

/**
 * This object is used to represent RPC request objects.
 * 
 * @author kotelnikov
 * @see http://groups.google.com/group/json-rpc/web/json-rpc-2-0
 */
public class RpcRequest extends RpcObject {

    /**
     * Factory for {@link RpcRequest} instances.
     */
    public static final IJsonValueFactory<RpcRequest> FACTORY = new IJsonValueFactory<RpcRequest>() {
        public RpcRequest newValue(Object object) {
            return new RpcRequest().setJsonObject(object);
        }
    };

    private static final String KEY_METHOD = "method";

    private static final String KEY_PARAMS = "params";

    /**
     * Default constructor.
     */
    public RpcRequest() {
        super();
    }

    /**
     * @param request
     */
    public RpcRequest(String request) {
        super(request);
    }

    /**
     * @param id a unique identifier of this call; this identifier will be used
     *        to retrieve responses; could be <code>null</code> for notification
     *        calls (see "4.1 Notification" section in the spec)
     * @param method the name of the method to call
     * @param params parameter object. It could be {@link JsonObject} or a
     *        {@link JsonArray} instance
     */
    public RpcRequest(String id, String method, JsonValue params) {
        super();
        setId(id);
        setMethod(method);
        setParams(params);
    }

    /**
     * Returns the name of the method to call.
     * 
     * @return the name of the method to call.
     */
    public String getMethod() {
        return getString(KEY_METHOD);
    }

    /**
     * Returns method parameters as a generic {@link JsonValue}.
     * 
     * @return method parameters as a generic {@link JsonValue}
     * @see #getParams(IJsonValueFactory)
     */
    public JsonValue getParams() {
        return getParams(JsonValue.VALUE_FACTORY);
    }

    /**
     * Transforms method parameters in an object of the required type.
     * 
     * @param <T> the type of the returned value
     * @param factory the factory used to transform method parameters to an
     *        object of the required type
     * @return parameters of the call
     */
    public <T> T getParams(IJsonValueFactory<T> factory) {
        return getValue(KEY_PARAMS, factory);
    }

    /**
     * Returns method parameters as an array.
     * 
     * @return method parameters as an array.
     */
    public JsonArray getParamsAsArray() {
        JsonValue value = getParams();
        if (value instanceof JsonArray) {
            return (JsonArray) value;
        }
        JsonArray array = new JsonArray();
        array.addValue(value);
        return array;
    }

    /**
     * Returns method parameters as an object or <code>null</code> if it can not
     * be represented as an object.
     * 
     * @return method parameters as an object.
     */
    public JsonObject getParamsAsObject() {
        JsonValue value = getParams();
        if (value instanceof JsonObject) {
            return (JsonObject) value;
        }
        if (value instanceof JsonArray) {
            JsonArray array = (JsonArray) value;
            if (array.getSize() == 1) {
                return array.getObject(0, JsonObject.FACTORY);
            }
        }
        return null;
    }

    @Override
    public RpcRequest setId(Object id) {
        return (RpcRequest) super.setId(id);
    }

    /**
     * Sets a new name of the method to call.
     * 
     * @param method the name of the called method
     * @return this object
     */
    public RpcRequest setMethod(String method) {
        setValue(KEY_METHOD, method);
        return this;
    }

    /**
     * Creates and sets a parameter object using the given values. Impair object
     * in the given list are used as keys and pair objects are used as the
     * corresponding values.
     * 
     * @param params parameter values to set
     * @return this object
     */
    public RpcRequest setParamObject(Object... params) {
        JsonObject obj = toJsonObject(params);
        setValue(KEY_PARAMS, obj);
        return this;
    }

    /**
     * Sets a new method call parameter. It could be a {@link JsonObject} or an
     * {@link JsonArray} instance.
     * 
     * @param value the parameter value
     * @return this object
     */
    public RpcRequest setParams(JsonValue value) {
        setValue(KEY_PARAMS, value);
        return this;
    }

    /**
     * Sets method call parameters.
     * 
     * @param params parameter values to set
     * @return this object
     */
    public RpcRequest setParams(Object... params) {
        setValue(KEY_PARAMS, params);
        return this;
    }

    @Override
    public RpcRequest setVersion(String version) {
        return (RpcRequest) super.setVersion(version);
    }

}