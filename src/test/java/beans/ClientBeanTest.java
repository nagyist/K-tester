package beans;

import junit.framework.TestCase;

/**
 * Created by eduarddedu on 15/06/16.
 */
public class ClientBeanTest extends TestCase {

    public void testGetName() {
        ClientBean clientBean = new ClientBean();
        assertTrue(clientBean.getName() != null);
    }

}