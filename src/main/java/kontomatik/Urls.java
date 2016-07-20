package kontomatik;

/**
 * Created by eduarddedu on 06/07/16.
 */
enum Urls {
    DEFAULT_IMPORT(SessionBean.API_ENDPOINT + "v1/command/default-import.xml"),
    IMPORT_OWNERS(SessionBean.API_ENDPOINT + "v1/command/import-owners.xml"),
    IMPORT_ACCOUNTS(SessionBean.API_ENDPOINT + "v1/command/import-accounts.xml"),
    IMPORT_ACCOUNT_TRANSACTIONS(SessionBean.API_ENDPOINT + "v1/command/import-account-transactions.xml"),
    AGGREGATED_VALUES(SessionBean.API_ENDPOINT + "v1/aggregates.xml"),
    SIGN_OUT(SessionBean.API_ENDPOINT + "v1/command/sign-out.xml"),
    POLL_STATUS(SessionBean.API_ENDPOINT + "v1/command/"), //0000.xml
    CATALOG(SessionBean.API_ENDPOINT + "v1/catalog.xml"),
    DATA(SessionBean.API_ENDPOINT + "v1/data.xml");
    String value;
    private Urls(String s) {
        value = s;
    }
}

