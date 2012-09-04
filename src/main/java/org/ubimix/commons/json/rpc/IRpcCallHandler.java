package org.ubimix.commons.json.rpc;

/**
 * Instances of this type are used to handle requests and send responses using a
 * specified callback object.
 * 
 * @author kotelnikov
 */
public interface IRpcCallHandler {

    /**
     * Callbacks are used to notify the caller when the result is ready.
     */
    public interface IRpcCallback {
        /**
         * This method should be called by the
         * {@link IRpcCallHandler#handle(RpcRequest, IRpcCallback)} method to
         * notify the caller that the calculation results are ready.
         * 
         * @param response the response object ready
         */
        void finish(RpcResponse response);
    }

    /**
     * This method should perform main calculations and it MUST call the
     * {@link IRpcCallback#finish(RpcResponse)} method when results are ready.
     * 
     * @param request the request object containing method call parameters
     * @param callback the callback object to call when results are ready
     */
    void handle(RpcRequest request, IRpcCallback callback);
}