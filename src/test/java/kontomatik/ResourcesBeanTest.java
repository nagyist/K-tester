package kontomatik;

import junit.framework.TestCase;

/**
 * Created by eduarddedu on 15/06/16.
 */
public class ResourcesBeanTest extends TestCase {

    public void testGetResources() {
        ResourcesBean resourcesBean = new ResourcesBean();
        assertTrue(resourcesBean.getClientName() != null);
        assertTrue(resourcesBean.getApiKey() == null); // apiKey should never been exposed on this branch !
    }

}