package resonant.lib.test.wrapper

import junit.framework.TestCase
import net.minecraftforge.common.util.ForgeDirection
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
    val mask = 32
    //Opens the mask 000100 (4)
    val resultMask = mask.openMask(2)

    //Expect the mask 100100 (or 36)
    Assert.assertEquals("Open mask test failed.", 36, resultMask, 0)
  }

  def testCloseMask()
  {
    //Equivalent 100100 (36)
    val mask = 36
    //Close the mask 000100 (4)
    val resultMask = mask.closeMask(2)

    //Expect the mask 100000 (or 32)
    Assert.assertEquals("Close mask test failed.",32, resultMask, 0)
  }

  def testCheckMask()
  {
    //Equivalent 100100 (36)
    val mask = 36

    Assert.assertTrue("Mask doesn't contain 5",mask.mask(5))
    Assert.assertTrue("Mask doesn't contain 2",mask.mask(2))
  }

  def testInvertMask()
  {
    //Equivalent 100100 (36)
    val mask = 36

    //Inverted 011011 (27)
    Assert.assertEquals("Mask failed to invert correctly", 27, mask.invert(), 0)
  }

  def testForgeDirectionMask()
  {
    var mask = 0
    for(dir <- ForgeDirection.VALID_DIRECTIONS)
    {
      mask = mask.openMask(dir);
      Assert.assertTrue("Mask should contain " + dir, mask.mask(dir))
      mask = mask.closeMask(dir);
      Assert.assertTrue("Mask should not contain " + dir, !mask.mask(dir))
    }
  }
}
