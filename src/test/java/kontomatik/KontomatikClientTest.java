package kontomatik;

import junit.framework.TestCase;

/**
 * Created by eduarddedu on 15/06/16.
 */
public class KontomatikClientTest extends TestCase {

    public void testGetName() {
        KontomatikClient kontomatikClient = new KontomatikClient();
        assertTrue(kontomatikClient.getName() != null);
    }

}