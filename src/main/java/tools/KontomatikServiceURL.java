package tools;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 17/09/16
 */

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provides access to various Kontomatik API URLs.
 */

public class KontomatikServiceURL {
    private static final String hostname = "https://test.api.kontomatik.com"; // test API endpoint
    public static final URL GET_HEALTH_CHECK;
    public static final URL POST_IMPORT_OWNERS;
    public static final URL POST_IMPORT_ACCOUNTS;
    public static final URL POST_IMPORT_ACCOUNT_TRANSACTIONS;
    public static final URL POST_DEFAULT_IMPORT;
    public static final URL POST_SIGN_OUT;
    public static final URL GET_CATALOG;
    public static final URL GET_DATA;

    static {
        try {
            GET_HEALTH_CHECK = new URL( hostname + "/isItWorking" );
            POST_IMPORT_OWNERS = new URL( hostname + "/v1/command/import-owners.xml" );
            POST_IMPORT_ACCOUNTS = new URL( hostname + "/v1/command/import-accounts.xml" );
            POST_IMPORT_ACCOUNT_TRANSACTIONS = new URL( hostname + "/v1/command/import-account-transactions.xml" );
            POST_DEFAULT_IMPORT = new URL( hostname + "/v1/command/default-import.xml" );
            POST_SIGN_OUT = new URL( hostname + "/v1/command/sign-out.xml" );
            GET_CATALOG = new URL( hostname + "/v1/catalog.xml" );
            GET_DATA = new URL( hostname + "/v1/data.xml" );
        } catch (java.net.MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static URL newGET_STATUS(String commandId, String SIGNATURE) {
        try {
            System.out.format("commandId == %s%n", commandId);
            String spec = String.format("%s%s.xml?%s",
                    hostname + "/v1/command/", commandId, SIGNATURE);
            return new URL(spec);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static URL newGET_AGGREGATES(String params) {
        try {
            String spec = String.format("%s%s", hostname + "/v1/aggregates.xml", "?" + params);
            return new URL(spec);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
