package com.sirwazowski.finnhub.stockprice.request;

import com.sirwazowski.finnhub.enumerations.Function;

public class LiveRequest extends StockPriceRequest{
    protected LiveRequest(Builder builder) {
        super(builder);
    }

    public static class Builder extends StockPriceRequest.Builder<Builder>{

        public Builder(){
            super();
            this.function(Function.LIVE_QUOTE);
        }

        @Override
        public LiveRequest build() {
            return new LiveRequest(this);
        }
    }
}
