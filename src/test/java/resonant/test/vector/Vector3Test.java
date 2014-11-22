package resonant.test.vector;

import junit.framework.TestCase;
import org.junit.Assert;
import resonant.lib.transform.vector.Vector3;

/**
 * Created by robert on 10/27/2014.
 */
public class Vector3Test extends TestCase
{
    /**Simple addition test for Vector3 */
    public void testAddition() throws Exception
    {
        Vector3 vec = new Vector3(0, 1, 0);
        vec.add(1, 1, 1);

        Assert.assertEquals(2.0, vec.y(), 0);
        Assert.assertEquals(1.0, vec.x(), 0);
        Assert.assertEquals(1.0, vec.z(), 0);
    }

    /**Simple addition test for Vector3 */
    public void testSubtraction() throws Exception
    {
        Vector3 vec = new Vector3(0, 1, 0);
        vec.subtract(1, 1, 1);

        Assert.assertEquals(0.0, vec.y(), 0);
        Assert.assertEquals(-1.0, vec.x(), 0);
        Assert.assertEquals(-1.0, vec.z(), 0);
    }
}
