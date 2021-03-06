package org.superbapps.integrations.utils;

import com.google.api.client.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import org.codehaus.jettison.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

public class JSONUtils {

    public static final String DEFAULT_DATETIME_FORMAT = "dd.MM.yyyy HH:mm:ss";

    //<editor-fold desc="supporting methods">
    public static String getDateTimeFormat(String... dateTimeFormat) {
        String pattern;

        if (dateTimeFormat == null || dateTimeFormat.length == 0)
            pattern = DEFAULT_DATETIME_FORMAT;
        else if (dateTimeFormat.length != 1)
            throw new RuntimeException("Method allows empty, or just one argument.");
        else
            pattern = dateTimeFormat[0];

        // throw new IllegalArgumentException if not formated properly !
        new SimpleDateFormat(pattern).format(new Date());

        return pattern;
    }

    /**
     * Gson with or without defined time format.
     *
     * @param dateTimeFormat Optional. If not provided, set it up with {@link JSONUtils#DEFAULT_DATETIME_FORMAT} value.
     */
    public static Gson getGsonWithTimeFormat(String... dateTimeFormat) {
        GsonBuilder gb = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting();

        gb.setDateFormat(getDateTimeFormat(dateTimeFormat));
        Gson gson = gb.create();
        return gson;
    }
    //</editor-fold>

    /**
     * /**
     * Parse JSON response to appropriate Dto representation of a type {@link T}
     *
     * @param response {@link T}
     * @param clazz    Class type
     */
    public static <T> T parseResponse(HttpResponse response, Class<T> clazz) {
        try {
            Scanner s = new Scanner(response.getContent()).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            JSONObject jsonObj = new JSONObject(result);
            String res = jsonObj.toString(2);

            return getGsonWithTimeFormat().fromJson(res, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Parse Response Error.", e);
        }
    }

    public static <T> T parseResponse(@NonNull String response, @NonNull Class<T> returnTypeClass, String... dateTimeFormat) {
        try {
            JSONObject jsonObj = new JSONObject(response);
            String res = jsonObj.toString(2);

            return getGsonWithTimeFormat(dateTimeFormat).fromJson(res, returnTypeClass);
        } catch (Exception e) {
            throw new RuntimeException("Parse Response Error.", e);
        }
    }

    /**
     * Parse JSON response to appropriate lsit of Dto's representation of a type {@link List<T>}
     *
     * @param {@link          T} Return (dto) type
     * @param returnTypeClass T class
     * @param dateTimeFormat  optional date format
     * @param response        {@link List<T>}
     */
    public static <T> List<T> parseResponseAsList(HttpResponse response, Class<T> returnTypeClass, String... dateTimeFormat) {
        String errMsg = "List Parse Response Error";
        Type returnType = TypeToken.getParameterized(List.class, returnTypeClass).getType();

        try {
            Scanner s = new Scanner(response.getContent()).useDelimiter("\\A");
            String respString = s.hasNext() ? s.next() : "";

            return getGsonWithTimeFormat(dateTimeFormat).fromJson(respString, returnType);
        } catch (Exception e) {
            throw new RuntimeException(errMsg, e);
        }
    }

    public static <T> List<T> parseResponseAsList(String response, Class<T> returnTypeClass) {
        String errMsg = "List Parse Response Error";
        Type returnType = TypeToken.getParameterized(List.class, returnTypeClass).getType();

        try {
            return getGsonWithTimeFormat().fromJson(response, returnType);
        } catch (Exception e) {
            throw new RuntimeException(errMsg, e);
        }
    }
}
