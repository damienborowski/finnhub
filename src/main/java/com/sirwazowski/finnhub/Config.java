package com.sirwazowski.finnhub;

import okhttp3.OkHttpClient;

/**
 * Allows you to set the library com.sirwazowski.finnhub.configuration parameters.
 *
 * @since 1.0.0
 * @author Damien Borowski
 */
public class Config {

    public static final String BASE_URL = "https://api.finnhub.io/api/v1/";
    private final String apiKey;
    private final int timeOut;
    private final OkHttpClient httpClient;

    private Config(Builder builder) {
        this.apiKey = builder.apiKey;
        this.timeOut = builder.timeOut;
        this.httpClient = builder.httpClient == null ? defaultClient(): builder.httpClient;

    }

    public String getKey() {
        return apiKey;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public OkHttpClient getOkHttpClient(){
        return this.httpClient;
    }

    public static Builder builder(){
        return new Builder();
    }

    /**
     * Make sure the config is not null and is with an api key
     *
     * @param config config instance
     */
    public static void checkNotNullOrKeyEmpty(Config config) {
        if (config == null) throw new FinnhubException("Config not set");
        if (config.getKey() == null) throw new FinnhubException("API Key not set");
    }

    /**
     * Configure a default http client for the library
     *
     * @return a default HTTP client for fetching data
     */
    private OkHttpClient defaultClient(){
        return new OkHttpClient.Builder()
                .build();
    }

    public static class Builder {

        private String apiKey;
        private int timeOut;
        private OkHttpClient httpClient;

        public Builder setApiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder timeOut(int timeOut){
            this.timeOut = timeOut;
            return this;
        }

        public Builder httpClient(OkHttpClient httpClient){
            this.httpClient = httpClient;
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }
}
