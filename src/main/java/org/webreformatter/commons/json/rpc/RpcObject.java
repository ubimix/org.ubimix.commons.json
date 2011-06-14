package org.webreformatter.commons.json.rpc;

import org.webreformatter.commons.json.JsonObject;

/**
 * Common superclass for RPC request and response objects. It contains common
 * fields such as call identifier and RPC version number.
 * 
 * @author kotelnikov
 * @see http://groups.google.com/group/json-rpc/web/json-rpc-2-0
 */
public abstract class RpcObject extends JsonObject {

    /**
     * The default RPC version number.
     */
    public static final String DEFAULT_RPC_VERSION = "2.0";

    /**
     * The key of the call identifier.
     */
    public static final String KEY_ID = "id";

    /**
     * The key of the RPC version number.
     */
    public static final String KEY_VERSION = "jsonrpc";

    /**
     * Default constructor.
     */
    public RpcObject() {
        setVersion(DEFAULT_RPC_VERSION);
    }

    /**
     * A copy constructor. Copies the identifier and the version number from the
     * given object.
     * 
     * @param obj
     */
    public RpcObject(RpcObject obj) {
        setVersion(obj.getVersion());
        setId(obj.getId());
    }

    /**
     * @param json
     */
    public RpcObject(String json) {
        setJsonObject(json);
    }

    /**
     * Returns the unique identifier of this call. It could be <code>null</code>
     * for notification calls.
     * 
     * @return the unique identifier of this call. It could be <code>null</code>
     *         .
     */
    public Object getId() {
        return getValue(KEY_ID, NULL_FACTORY);
    }

    /**
     * Returns the unique identifier of this call as a string. It could be
     * <code>null</code> for notification calls.
     * 
     * @return the unique identifier of this call. It could be <code>null</code>
     *         .
     */
    public String getIdAsString() {
        Object id = getId();
        return fAccessor.toString(id);
    }

    /**
     * Returns the version of the used RPC protocol.
     * 
     * @return the version of the used RPC protocol.
     */
    public String getVersion() {
        return getString(KEY_VERSION);
    }

    /**
     * Sets a new unique identifier of this call. This identifier will be used
     * to retrieve responses; could be <code>null</code> for notification calls
     * (see "4.1 Notification" section in the specification)
     * 
     * @param id a unique identifier of this call
     * @return this object
     */
    public RpcObject setId(Object id) {
        setValue(KEY_ID, id);
        return this;
    }

    /**
     * Sets the version of the used RPC protocol. By default it is the "2.0"
     * value.
     * 
     * @param version the version of the RPC protocol.
     * @return this object
     */
    public RpcObject setVersion(String version) {
        setValue(KEY_VERSION, version);
        return this;
    }

}