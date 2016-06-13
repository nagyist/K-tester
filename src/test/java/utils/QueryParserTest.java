package utils;

import junit.framework.TestCase;

import java.util.Hashtable;

public class QueryParserTest extends TestCase {

    /**
     * Basic test for method parseQueryString()
     */

    public void testParseQueryString() {
        QueryParser queryParser = new QueryParser();

        String query = "sessionId=2590221"
                + "&sessionIdSignature=64c188af2b11470a9f72246999e235f00739c2b65fafa9ab1597a3835893cf15"
                + "&target=pl";
        Hashtable<String, String> hashtable = queryParser.parseQueryString(query);
        assertTrue( hashtable.get("sessionId") != null );
        assertTrue( hashtable.get("sessionIdSignature") != null);
        assertTrue( hashtable.get("target") != null );
    }
}
