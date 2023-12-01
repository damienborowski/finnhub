package com.sirwazowski.finnhub;

import com.sirwazowski.finnhub.stockprice.StockPrice;

/**
 * Client interface of library.
 * The API is accessed through this class.
 * Exposes a singleton instance for interaction
 *
 * @since 1.0.0
 * @author Damien Borowski
 */
public class Finnhub {

    private static Finnhub INSTANCE;
    private Config config;

    private Finnhub(){}

    /** Initialize the client with a {@link Config} instance */
    public void init(Config config){
        this.config = config;
    }

    /**
     * Access the client interface
     *
     * @return Singleton instance of {@link Finnhub}
     */
    public static Finnhub api() {
        if (INSTANCE == null) {
            INSTANCE = new Finnhub();
        }
        return INSTANCE;
    }

    /**
     * Access to Stock Price Data. All requests associated with Stock Prices are accessed through this method.
     *
     * @return A {@link StockPrice} instance for access to Stock Price data
     */
    public StockPrice stockPrice(){
        return new StockPrice(config);
    }
}
