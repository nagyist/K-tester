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
    private static final String PROTOCOL = "https";
    private static final String HOST = "test.api.kontomatik.com"; // test API endpoint
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
            GET_HEALTH_CHECK = new URL( PROTOCOL, HOST, "/isItWorking" );
            POST_IMPORT_OWNERS = new URL( PROTOCOL, HOST, "/v1/command/import-owners.xml");
            POST_IMPORT_ACCOUNTS = new URL( PROTOCOL, HOST, "/v1/command/import-accounts.xml" );
            POST_IMPORT_ACCOUNT_TRANSACTIONS = new URL( PROTOCOL, HOST, "/v1/command/import-account-transactions.xml" );
            POST_DEFAULT_IMPORT = new URL( PROTOCOL, HOST, "/v1/command/default-import.xml" );
            POST_SIGN_OUT = new URL( PROTOCOL, HOST, "/v1/command/sign-out.xml" );
            GET_CATALOG = new URL( PROTOCOL, HOST, "/v1/catalog.xml" );
            GET_DATA = new URL( PROTOCOL, HOST, "/v1/data.xml" );
        } catch (java.net.MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static URL newGET_STATUS(String commandId, String signature) {
        try {
            System.out.format("commandId == %s%n", commandId);
            String file = String.format("%s%s.xml?%s", "/v1/command/", commandId, signature);
            return new URL(PROTOCOL, HOST, file);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static URL newGET_AGGREGATES(String signature) {
        try {
            String file = String.format("%s?%s", "/v1/aggregates.xml", signature);
            return new URL(PROTOCOL, HOST, file);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
