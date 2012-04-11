package org.webreformatter.commons.json.rpc;

import org.webreformatter.commons.json.JsonObject;

/**
 * This object is used to represent RPC errors.
 * 
 * @author kotelnikov
 * @see http://groups.google.com/group/json-rpc/web/json-rpc-2-0
 */
public class RpcError extends JsonObject {

    /**
     * Internal error Internal JSON-RPC error.
     */
    public static int ERROR_INTERNAL_ERROR = -32603;

    /**
     * Invalid params Invalid method parameter(s).
     */
    public static int ERROR_INVALID_PARAMS = -32602;

    /**
     * Invalid Request The JSON sent is not a valid Request object.
     */
    public static int ERROR_INVALID_REQUEST = -32600;

    /**
     * Method not found The method does not exist / is not available.
     */
    public static int ERROR_METHOD_NOT_FOUND = -32601;

    /**
     * Parse error Invalid JSON was received by the server. An error occurred on
     * the server while parsing the JSON text.
     */
    public static int ERROR_PARSE = -32700;

    /**
     * Server error maximal possible value. Reserved for implementation-defined
     * server-errors.
     */
    public static int ERROR_SERVER_ERROR_MAX = -32000;

    /**
     * Server error minimal possible value. Reserved for implementation-defined
     * server-errors.
     */
    public static int ERROR_SERVER_ERROR_MIN = -32099;

    /**
     * Factory for {@link RpcResponse} instances.
     */
    public static final IJsonValueFactory<RpcError> FACTORY = new IJsonValueFactory<RpcError>() {
        public RpcError newValue(Object object) {
            return new RpcError().setJsonObject(object);
        }
    };

    /**
     * Field name for the error codes
     */
    public static final String KEY_CODE = "code";

    /**
     * This field is used to store an array of string-serialized exceptions.
     */
    public static final String KEY_ERRORS = "errors";

    /**
     * Name of the field containing human-readable messages describing the
     * problem.
     */
    public static final String KEY_MESSAGE = "message";

    /**
     * Creates and returns a new {@link RpcError} instance using the information
     * from the given exception
     * 
     * @param code the code of the error to set
     * @param t the exception used as a source of information for the resulting
     *        error object
     * @return an {@link RpcError} instance filled with the information from the
     *         given exception (or error)
     */
    public static RpcError getError(int code, Throwable t) {
        RpcError error = new RpcError();
        StringBuilder buf = new StringBuilder();
        StackTraceElement[] stackTrace = t.getStackTrace();
        for (StackTraceElement e : stackTrace) {
            buf.append("\n  ");
            buf.append(e.getClassName()
                + "#"
                + e.getMethodName()
                + " ("
                + e.getFileName()
                + ":"
                + e.getLineNumber()
                + ")");
            buf.append(e);
        }
        buf.append("\n");
        error
            .setValue("code", code)
            .setValue("message", t.getMessage())
            .setValue("stackTrace", buf.toString());
        return error;
    }

    /**
     * Creates and returns a new {@link RpcError} instance using the information
     * from the given exception. This method sets the
     * {@link RpcError#ERROR_INTERNAL_ERROR} internal error code.
     * 
     * @param t the exception used as a source of information for the resulting
     *        error object
     * @return an {@link RpcError} instance filled with the information from the
     *         given exception (or error)
     */
    public static RpcError getError(Throwable t) {
        return getError(RpcError.ERROR_INTERNAL_ERROR, t);
    }

    /**
     * 
     */
    public RpcError() {
        setCode(ERROR_INTERNAL_ERROR);
    }

    /**
     * Sets error code and a human-readable message describing the problem.
     * 
     * @param code error code to set
     * @param message the error message
     */
    public RpcError(int code, String message) {
        setError(code, message);
    }

    /**
     * This constructor is used to build instances of this class from serialized
     * JSON objects.
     * 
     * @param json
     */
    public RpcError(String json) {
        super(json);
    }

    /**
     * Returns the code of the error.
     * 
     * @return the code of the error.
     */
    public int getCode() {
        return getInteger(KEY_CODE, 0);
    }

    /**
     * Returns a human-readable message describing the problem.
     * 
     * @return a human-readable message describing the problem.
     */
    public String getMessage() {
        return getString(KEY_MESSAGE);
    }

    /**
     * Sets a new value of the error code.
     * 
     * @param code the error code
     * @return this object
     */
    public RpcError setCode(int code) {
        setValue(KEY_CODE, code);
        return this;
    }

    /**
     * Sets error code and a human-readable message describing the problem.
     * 
     * @param code error code to set
     * @param message the error message
     */
    public void setError(int code, String message) {
        setCode(code);
        setMessage(message);
    }

    /**
     * Sets serialized java exceptions in this object.
     * 
     * @param errors
     */
    public void setErrors(Throwable... errors) {
        setValue(KEY_ERRORS, errors);
    }

    /**
     * Sets a human-readable message describing the problem.
     * 
     * @param message the error message
     * @return this object
     */
    public RpcError setMessage(String message) {
        setValue(KEY_MESSAGE, message);
        return this;
    }

}