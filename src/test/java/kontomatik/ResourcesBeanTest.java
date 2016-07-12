package kontomatik;

import junit.framework.TestCase;

/**
 * Created by eduarddedu on 15/06/16.
 */
public class ResourcesBeanTest extends TestCase {

    public void testGetName() {
        ResourcesBean resourcesBean = new ResourcesBean();
        assertTrue(resourcesBean.getClientName() != null);
    }

}