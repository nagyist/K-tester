package utils;

import java.util.*;
import java.util.regex.*;

/**
 * Created by eduarddedu on 12/06/16.
 */
public class QueryParser {
    /*
     Utility class for parsing a query string. The class is by no means general-purpose, it is
     designed to suit the particular case of Kontomatik web application.
     */


    /**
     * Method is guaranteed to return a Hashtable map with at least one parameter-value pair.
     *
     * @param s
     * @return
     */
    public Hashtable<String, String> parseQueryString(String s) {

        Hashtable<String, String> result = new Hashtable<>();

        List<String> keyValuePairs = new ArrayList<>(Arrays.asList(s.split("&")));

        if (keyValuePairs.isEmpty())
            throw new IllegalArgumentException("Found no \"&\" character in argument string");

        for (String s1 : keyValuePairs) {
            String key, value;
            String[] pair = s1.split("=");
            if (pair.length < 2)
                throw new IllegalArgumentException("Found no \"=\" character in argument string");
            key = pair[0];
            value = pair[1];
            result.put(key, value);
        }
        return result;
    }

}
