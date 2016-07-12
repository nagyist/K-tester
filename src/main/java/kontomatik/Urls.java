package kontomatik;

/**
 * Created by eduarddedu on 06/07/16.
 */
enum Urls {
    DEFAULT_IMPORT(Session.API_ENDPOINT + "v1/command/default-import.xml"),
    IMPORT_OWNERS(Session.API_ENDPOINT + "v1/command/import-owners.xml"),
    IMPORT_ACCOUNTS(Session.API_ENDPOINT + "v1/command/import-accounts.xml"),
    IMPORT_ACCOUNT_TRANSACTIONS(Session.API_ENDPOINT + "v1/command/import-account-transactions.xml"),
    AGGREGATED_VALUES(Session.API_ENDPOINT + "v1/aggregates.xml"),
    POLL_STATUS(Session.API_ENDPOINT + "v1/command/");
    String value;
    private Urls(String s) {
        value = s;
    }
}

