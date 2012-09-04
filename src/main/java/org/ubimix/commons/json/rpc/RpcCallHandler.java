package org.ubimix.commons.json.rpc;

import org.ubimix.commons.json.JsonObject;

/**
 * An utility class used as a common superclass for individual synchronous RPC
 * call handlers.
 * 
 * @author kotelnikov
 */
public abstract class RpcCallHandler implements IRpcCallHandler {

    /**
     * @author kotelnikov
     */
    public static class RpcException extends Exception {
        private static final long serialVersionUID = -1361676532446926691L;

        private int fErrorCode;

        private String fFormattedMessage;

        public RpcException() {
            this(RpcError.ERROR_INTERNAL_ERROR, "", null);
        }

        public RpcException(int code, String message, Throwable cause) {
            super(message, cause);
            fErrorCode = code;
        }

        public RpcException(String message) {
            this(RpcError.ERROR_INTERNAL_ERROR, message, null);
        }

        public RpcException(Throwable cause) {
            this(RpcError.ERROR_INTERNAL_ERROR, null, cause);
        }

        @SuppressWarnings("unchecked")
        protected <T extends RpcCallHandler.RpcException> T cast() {
            return (T) this;
        }

        public RpcError getAsRpcError() {
            RpcError error = new RpcError();
            error.setCode(fErrorCode);
            error.setErrors(this);
            RpcCallHandler.setFormattedMessage(
                error,
                getMessage(),
                fFormattedMessage);
            return error;
        }

        public int getErrorCode() {
            return fErrorCode;
        }

        public String getFormattedMessage() {
            return fFormattedMessage;
        }

        public <T extends RpcCallHandler.RpcException> T setErrorCode(
            int errorCode) {
            fErrorCode = errorCode;
            return cast();
        }

        public <T extends RpcCallHandler.RpcException> T setFormattedMessage(
            String message) {
            fFormattedMessage = message;
            return cast();
        }
    }

    private static String fIdBase = "id-" + System.currentTimeMillis() + "-";

    private static int fIdCounter = 0;

    public static synchronized String newRequestId() {
        String id = fIdBase + System.currentTimeMillis() + "-" + (fIdCounter++);
        return id;
    }

    public static void setFormattedMessage(
        JsonObject obj,
        String message,
        String formattedMessage) {
        if (message != null) {
            obj.setValue("message", message);
        }
        if (formattedMessage != null) {
            obj.setValue("formattedMessage", formattedMessage);
        }
    }

    protected abstract RpcResponse doHandle(RpcRequest request)
        throws Exception;

    /**
     * @see org.ubimix.commons.json.rpc.IRpcCallHandler#handle(org.ubimix.commons.json.rpc.RpcRequest,
     *      org.ubimix.commons.json.rpc.IRpcCallHandler.IRpcCallback)
     */
    public final void handle(RpcRequest request, IRpcCallback callback) {
        RpcResponse response = null;
        try {
            response = doHandle(request);
        } catch (RpcCallHandler.RpcException e) {
            response = newResponse(request);
            response.setError(e.getAsRpcError());
        } catch (Throwable t) {
            response = newResponse(request);
            RpcError error = new RpcError(
                RpcError.ERROR_INTERNAL_ERROR,
                t.getMessage());
            error.setErrors(t);
            response.setError(error);
        } finally {
            callback.finish(response);
        }
    }

    protected RpcResponse newResponse(RpcRequest request) {
        Object requestId = request.getId();
        if (requestId == null) {
            requestId = newRequestId();
        }
        return new RpcResponse().<RpcResponse> setId(requestId);
    }
}