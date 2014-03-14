package calclavia.test.utility;

import calclavia.lib.utility.MathUtility;
import junit.framework.Assert;
import org.junit.Test;

/**
 * @since 14/03/14
 * @author tgame14
 */
public class TestMathUtility
{
    /**
     * An example of how this is planned to be, ofcourse will test Much more complex code bits.
     * and will be Ran by the Jenkins
     */
    @Test
    public void testClampAngle()
    {
        Assert.assertEquals(125.0, MathUtility.clampAngleTo360(485));
        Assert.assertEquals(75.0, MathUtility.clampAngleTo180(255));
    }

    @Test
    public void testRandomIntArrayGenerator()
    {

    }
}
