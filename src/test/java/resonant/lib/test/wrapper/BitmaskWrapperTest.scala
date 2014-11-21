package resonant.lib.test.wrapper

import junit.framework.TestCase
import org.junit.Assert
import resonant.lib.wrapper.BitmaskWrapper._

/**
 * Tests the Bitmask wrapper functions
 * @author Calclavia
 */
class BitmaskWrapperTest extends TestCase
{
  def testOpenMask()
  {
    //Equivalent 100000 (32)
    val mask = 1 << 6
    //Opens the mask 000100 (4)
    val resultMask = mask.openMask(2)

    //Expect the mask 100100 (or 36)
    Assert.assertEquals(36, resultMask, 0)
  }

  def testCloseMask()
  {
    //Equivalent 100100 (36)
    val mask = 36
    //Close the mask 000100 (4)
    val resultMask = mask.closeMask(2)

    //Expect the mask 100000 (or 32)
    Assert.assertEquals(32, resultMask, 0)
  }

  def testCheckMask()
  {
    //Equivalent 100100 (36)
    val mask = 36

    Assert.assertTrue(mask.mask(5))
    Assert.assertTrue(mask.mask(2))
  }

  def testInvertMask()
  {
    //Equivalent 100100 (36)
    val mask = 36

    //Inverted 011011 (27)
    Assert.assertEquals(27, mask.invert(), 0)
  }
}
