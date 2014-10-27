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

        assertEquals(2.0, vec.y());
        assertEquals(1.0, vec.x());
        assertEquals(1.0, vec.z());
    }

    /**Simple addition test for Vector3 */
    public void testBasicSubtraction() throws Exception
    {
        Vector3 vec = new Vector3(0, 1, 0);
        vec.subtract(1, 1, 1);

        assertEquals(0.0, vec.y());
        assertEquals(-1.0, vec.x());
        assertEquals(-1.0, vec.z());
    }
}
