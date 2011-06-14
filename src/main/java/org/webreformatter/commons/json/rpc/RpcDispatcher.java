package org.webreformatter.commons.json.rpc;

import java.util.HashMap;
import java.util.Map;

/**
 * This object dispatches calls to other {@link IRpcCallHandler} instances based
 * on method names.
 * 
 * @author kotelnikov
 */
public class RpcDispatcher implements IRpcCallHandler {

    /**
     * This handler is used to handle calls for which no specific handlers were
     * registered.
     */
    private IRpcCallHandler fDefaultHandler;

    /**
     * Internal map containing method names and the corresponding
     * {@link IRpcCallHandler} instances.
     */
    private Map<String, IRpcCallHandler> fMap = new HashMap<String, IRpcCallHandler>();

    public RpcDispatcher() {
        this(new IRpcCallHandler() {
            public void handle(RpcRequest request, IRpcCallback rpcCallback) {
                RpcResponse response = new RpcResponse()
                    .setId(request.getId())
                    .setError(
                        RpcError.ERROR_METHOD_NOT_FOUND,
                        "Method not found");
                rpcCallback.finish(response);
            }
        });
    }

    /**
     * Sets the default handler.
     * 
     * @param defaulHandler the default handler to set
     */
    public RpcDispatcher(IRpcCallHandler defaulHandler) {
        setDefaultHandler(defaulHandler);
    }

    /**
     * Returns the default object used to manage all non-handled requests.
     * 
     * @return the default object used to manage all non-handled requests
     */
    public IRpcCallHandler getDefaultHandler() {
        return fDefaultHandler;
    }

    /**
     * @param method the name of the method to call
     * @return handler corresponding to the specified method
     */
    public synchronized IRpcCallHandler getHandler(String method) {
        IRpcCallHandler handler = fMap.get(method);
        if (handler == null) {
            handler = fDefaultHandler;
        }
        return handler;
    }

    /**
     * @see org.webreformatter.commons.json.rpc.IRpcCallHandler#handle(org.webreformatter.commons.json.rpc.RpcRequest,
     *      org.webreformatter.commons.json.rpc.IRpcCallHandler.IRpcCallback)
     */
    public void handle(RpcRequest request, IRpcCallback rpcCallback) {
        String method = request.getMethod();
        IRpcCallHandler handler = getHandler(method);
        handler.handle(request, rpcCallback);
    }

    /**
     * Registers a new handler for the specified method
     * 
     * @param method the name of the method
     * @param handler the handler called when the specified method invoked
     */
    public synchronized void registerHandler(
        final String method,
        final IRpcCallHandler handler) {
        fMap.put(method, handler);
    }

    /**
     * Sets the default handler.
     * 
     * @param handler the default handler to set
     */
    public void setDefaultHandler(IRpcCallHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException(
                "Default handler can not be null.");
        }
        fDefaultHandler = handler;
    }

    /**
     * Removes handler corresponding to the specified method
     * 
     * @param method the method for which the corresponding handler should
     *        unregistered.
     */
    public synchronized void unregisterHandler(String method) {
        fMap.remove(method);
    }

}