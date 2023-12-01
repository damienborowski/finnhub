package com.sirwazowski.finnhub;

/**
 * Defines an interface for pulling data from the API source.
 * A fetch operation can either fail or succeed
 *
 * @since 1.0.0
 * @author Damien Borowski
 */
public interface Fetcher {

    /** Perform a fetch operation */
    void fetch();

    /**
     * Callback when the fetch operation succeeds
     *
     * @param <V> the type of the response of the fetch operation
     */
    interface SuccessCallback<V> {
        /**
         * Call this method with a response when the fetch operation is successful
         *
         * @param response response object
         */
        void onSuccess(V response);
    }

    /** Callback when the fetch operation fails */
    interface FailureCallback {
        /**
         * Call this method with an exception when the fetch operation fails
         *
         * @param ex exception
         */
        void onFailure(FinnhubException ex);
    }
}