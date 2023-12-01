package com.sirwazowski.finnhub.stockprice;

import com.sirwazowski.finnhub.*;
import com.sirwazowski.finnhub.stockprice.request.LiveRequest;
import com.sirwazowski.finnhub.stockprice.request.StockPriceRequest;
import com.sirwazowski.finnhub.stockprice.response.QuoteResponse;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Map;

import static com.sirwazowski.finnhub.FinnhubUtils.parseJSON;

/**
 * Access to Stock Price Data
 *
 * @since 1.0.0
 * @author Damien Borowski
 */

public class StockPrice implements Fetcher {

    private final Config config;
    private StockPriceRequest.Builder<?> builder;
    private Fetcher.SuccessCallback<?> successCallback;
    private Fetcher.FailureCallback failureCallback;

    public StockPrice(Config config){
        this.config = config;
    }

    /**
     * Access live stock quote data
     * @return {@link LiveRequestProxy} instance
     */
    public LiveRequestProxy live(){
        return new LiveRequestProxy();
    }

    @Override
    public void fetch() {
        Config.checkNotNullOrKeyEmpty(config);

        config.getOkHttpClient().newCall(UrlExtractor.extract(builder.build(), config.getKey())).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(failureCallback != null) failureCallback.onFailure(new FinnhubException());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try (ResponseBody body = response.body()) {
                        parseResponse(parseJSON(body.string()));
                    }
                } else {
                    if (failureCallback != null) {
                        failureCallback.onFailure(new FinnhubException());
                    }
                }
            }
        });
    }

    /**
     * Parses a JSON response to a {@link QuoteResponse} object
     *
     * @param data parsed JSON response
     */
    private void parseResponse(Map<String, Object> data) {
        switch (builder.function) {
            case LIVE_QUOTE:
                parseQuoteResponse(data);
                break;
                /* falls through */
            default:
                break;
        }
    }

    /**
     * Parses Stock Price Quote Data
     *
     * @param data parsed JSON data
     */
    private void parseQuoteResponse(Map<String, Object> data){
        QuoteResponse response = QuoteResponse.of(data);
        if(response.getErrorMessage() != null && failureCallback != null) {
            failureCallback.onFailure(new FinnhubException(response.getErrorMessage()));
        }
        if(successCallback != null) {
            ((Fetcher.SuccessCallback<QuoteResponse>)successCallback).onSuccess(response);
        }
    }

    /**
     * Proxy for building an {@link LiveRequest}
     */
    public class LiveRequestProxy extends RequestProxy<LiveRequestProxy, QuoteResponse>{
        LiveRequestProxy() {
            super();
            this.builder = new LiveRequest.Builder();
        }
    }

    /**
     * An abstract proxy for building requests. Adds the functionality of adding callbacks and a terminal method for
     * fetching data.
     * @param <T> A Concrete {@link RequestProxy} Implementation
     * @param <U> A Response Type to return during a synchronous call
     */
    @SuppressWarnings("unchecked")
    public abstract class RequestProxy<T extends RequestProxy<?, U>, U> {

        protected StockPriceRequest.Builder<?> builder;
        protected U syncResponse;

        private RequestProxy(){
        }

        /**
         * Set the symbol for the request
         * @param symbol
         * @return
         */
        public T forSymbol(String symbol){
            this.builder.forSymbol(symbol);
            return (T)this;
        }

        /**
         * Set the failure callback during an async call
         * @param callback
         * @return
         */
        public T onFailure(FailureCallback callback) {
            StockPrice.this.failureCallback = callback;
            return (T)this;
        }

        /**
         * Set the right builder and make an async http request using the {@link StockPrice#fetch()}
         */
        public void fetch() {
            StockPrice.this.builder = this.builder;
            StockPrice.this.fetch();
        }

        /**
         * Set the reponse during a synchronous call
         * @param response
         */
        public void setSyncResponse(U response) {
            this.syncResponse = response;
        }


        /**
         * Set the right builder and make a synchronous request using {@link StockPrice#fetch()}
         * <p>When calling this method, any async callbacks will be overwritten</p>
         * @return The api response
         * @throws FinnhubException
         */
        public U fetchSync() throws FinnhubException {
            SuccessCallback<U> callback = (e) -> setSyncResponse(e);
            StockPrice.this.builder = this.builder;
            StockPrice.this.fetchSync(callback);
            return this.syncResponse;
        }

    }

    /**
     * Make a blocking synchronous http request to fetch the data.
     * This will be called by the {@link RequestProxy#fetchSync()}.
     *
     * <p>Using this method will overwrite any async callback</p>

     * @param successCallback internally used {@link SuccessCallback}
     * @throws FinnhubException exception thrown
     */
    private void fetchSync(SuccessCallback<?> successCallback) throws FinnhubException {

        Config.checkNotNullOrKeyEmpty(config);

        this.successCallback = successCallback;
        this.failureCallback = null;
        okhttp3.OkHttpClient client = config.getOkHttpClient();
        try(Response response = client.newCall(UrlExtractor.extract(builder.build(), config.getKey())).execute()){
            parseResponse(parseJSON(response.body().string()));
        }catch(IOException e){
            throw new FinnhubException(e.getMessage());
        }
    }
}
