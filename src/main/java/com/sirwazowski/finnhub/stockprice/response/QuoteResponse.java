package com.sirwazowski.finnhub.stockprice.response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuoteResponse {

    private String currentPrice;
    private String highPriceOfDay;
    private String lowPriceOfDay;
    private String openPriceOfDay;
    private String previousClosePrice;
    private String percentageChange;
    private String change;
    private String timestamp;

    private String errorMessage;

    public QuoteResponse(String currentPrice, String highPriceOfDay, String lowPriceOfDay, String openPriceOfDay, String previousClosePrice, String percentageChange, String change, String timestamp) {
        this.currentPrice = currentPrice;
        this.highPriceOfDay = highPriceOfDay;
        this.lowPriceOfDay = lowPriceOfDay;
        this.openPriceOfDay = openPriceOfDay;
        this.previousClosePrice = previousClosePrice;
        this.percentageChange = percentageChange;
        this.change = change;
        this.timestamp = timestamp;
        this.errorMessage = StringUtils.EMPTY;
    }

    private QuoteResponse(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public static QuoteResponse of(Map<String, Object> stringObjectMap){
        return parse(stringObjectMap);
    }

    private static QuoteResponse parse(Map<String, Object> stringObjectMap){
        List<String> keys = new ArrayList<>(stringObjectMap.keySet());
        if (keys.isEmpty()) {
            return onParseError("Empty JSON returned by the API, the symbol might not be supported.");
        } else {
            try{
                String currentPrice = String.valueOf(stringObjectMap.get("c"));
                String highPriceOfDay = String.valueOf(stringObjectMap.get("h"));
                String lowPriceOfDay = String.valueOf(stringObjectMap.get("l"));
                String openPriceOfDay = String.valueOf(stringObjectMap.get("o"));
                String previousClosePrice = String.valueOf(stringObjectMap.get("pc"));
                String percentageChange = String.valueOf(stringObjectMap.get("dp"));
                String change = String.valueOf(stringObjectMap.get("d"));
                String timestamp = String.valueOf(stringObjectMap.get("t"));
                return new QuoteResponse(currentPrice, highPriceOfDay, lowPriceOfDay, openPriceOfDay, previousClosePrice, percentageChange, change, timestamp);

            }catch (ClassCastException e){
                return onParseError(stringObjectMap.get(keys.get(0)).toString());
            }
        }
    }

    private static QuoteResponse onParseError(String error) {
        return new QuoteResponse(error);
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public String getHighPriceOfDay() {
        return highPriceOfDay;
    }

    public String getLowPriceOfDay() {
        return lowPriceOfDay;
    }

    public String getOpenPriceOfDay() {
        return openPriceOfDay;
    }

    public String getPreviousClosePrice() {
        return previousClosePrice;
    }

    public String getPercentageChange() {
        return percentageChange;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
