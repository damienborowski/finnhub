package com.sirwazowski.finnhub;

public class FinnhubException extends RuntimeException{

    public FinnhubException(){
        super();
    }

    public FinnhubException(String msg){
        super(msg);
    }

    public FinnhubException(String msg, Exception e){
        super(msg, e);
    }
}
