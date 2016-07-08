package kontomatik;

/**
 * Created by eduarddedu on 06/07/16.
 */
enum Urls {
    DEFAULT_IMPORT(KontomatikSession.API_ENDPOINT + "v1/command/default-import.xml"),
    IMPORT_OWNERS(KontomatikSession.API_ENDPOINT + "v1/command/import-owners.xml"),
    IMPORT_ACCOUNTS(KontomatikSession.API_ENDPOINT + "v1/command/import-accounts.xml"),
    IMPORT_ACCOUNT_TRANSACTIONS(KontomatikSession.API_ENDPOINT + "v1/command/import-account-transactions.xml"),
    POLL_STATUS(KontomatikSession.API_ENDPOINT + "v1/command/");
    String value;
    private Urls(String s) {
        value = s;
    }
}

