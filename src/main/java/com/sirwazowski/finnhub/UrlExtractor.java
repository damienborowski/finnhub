package com.sirwazowski.finnhub;

import com.sirwazowski.finnhub.enumerations.Function;
import okhttp3.Request;

import java.lang.reflect.Field;

/**
 * Extracts a valid url from a request object. The request object should contain valid
 * api endpoint parameters
 *
 * @since 1.0.0
 * @author Damien Borowski
 */
public class UrlExtractor{

    private UrlExtractor(){}

    /**
     * Get an API url from a request object
     *
     * @param object a request object with the valid API parameters
     * @return valid API url
     */
    public static String extract(Object object){

        //url
        StringBuilder stringBuilder = new StringBuilder();

        Class<?> cls = object.getClass();
        while(cls != null){
            //access all fields in object
            Field[] fields = cls.getDeclaredFields();
            for(Field field : fields){
                field.setAccessible(true);
                try {
                    //extract non-null and non-synthetic fields
                    if (!field.isSynthetic() && field.get(object) != null){
                        if(!field.getName().equalsIgnoreCase("function")){
                            stringBuilder.append(field.getName().toLowerCase())
                                    .append("=");
                            String value = (field.get(object)).toString();
                            stringBuilder.append(value).append("&");
                        }else {
                            String value =  Function.valueOf((field.get(object)).toString()).name;
                            stringBuilder.append(value).append("?");
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new FinnhubException(e.getLocalizedMessage());
                }
            }
            cls = cls.getSuperclass();
        }

        return stringBuilder.append("token=").toString();

    }

    /**
     * Build a http request with the parameters and the api key
     *
     * @param request any endpoint request object
     * @param apiKey Finnhub API key
     */
    public static Request extract(Object request, String apiKey){
        return new Request.Builder().url(Config.BASE_URL + UrlExtractor.extract(request) + apiKey).build();
    }
}
