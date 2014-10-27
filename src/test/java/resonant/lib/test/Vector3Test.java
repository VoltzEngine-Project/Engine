package resonant.lib.test;

import junit.framework.TestCase;
import resonant.lib.transform.vector.Vector3;

/**
 * Created by robert on 10/27/2014.
 */
public class Vector3Test extends TestCase
{
    /**Simple addition test for Vector3 */
    public void testBasicAddition() throws Exception
    {
        Vector3 vec = new Vector3(0, 1, 0);
        vec.add(1, 1, 1);

        assert(vec.y() == 2);
        assert(vec.x() == 1);
        assert(vec.z() == 1);
    }
}
