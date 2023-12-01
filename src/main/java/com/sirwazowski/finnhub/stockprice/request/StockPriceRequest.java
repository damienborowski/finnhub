package com.sirwazowski.finnhub.stockprice.request;

import com.sirwazowski.finnhub.enumerations.Function;

/**
 * Base Stock Price Request
 *
 * @since 1.0.0
 * @author Damien Borowski
 */

public abstract class StockPriceRequest {

    protected Function function;
    protected String symbol;

    protected StockPriceRequest(Builder<?> builder){
        this.function = builder.function;
        this.symbol = builder.symbol;
    }

    public abstract static class Builder<T extends Builder<?>>{

        public Function function;
        protected String symbol;

        public T function(Function function){
            this.function = function;
            return (T) this;
        }

        public T forSymbol(String symbol){
            this.symbol = symbol;
            return (T) this;
        }

        public abstract StockPriceRequest build();
    }
}
