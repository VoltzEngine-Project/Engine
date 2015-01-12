package com.builtbroken.mc.lib.test.vector

import junit.framework.TestCase
import net.minecraftforge.common.util.ForgeDirection
import org.junit.Assert
import com.builtbroken.mc.lib.transform.vector.Pos

/**
 * Created by robert on 10/27/2014.
 */
class Vector3ScalaOperatorTest extends TestCase
{

    ////////////////////////////////////////////
    /// Subtract operator tests               //
    ////////////////////////////////////////////

    /** Tests the 'vector3 - double' operator */
    def testSubtractDouble()
    {
        var vec : Pos = new Pos(1, 1, 1)
        vec = vec - 1
        Assert.assertEquals(0.0, vec.y, 0)
        Assert.assertEquals(0.0, vec.x, 0)
        Assert.assertEquals(0.0, vec.z, 0)
    }

    /** Tests the 'vector3 - vector3' operator */
    def testSubtractVector3()
    {
        var vec : Pos = new Pos(1, 1, 1)
        vec = vec - new Pos(1, 1, 1)
        Assert.assertEquals(0.0, vec.y, 0)
        Assert.assertEquals(0.0, vec.x, 0)
        Assert.assertEquals(0.0, vec.z, 0)
    }

    /** Tests the 'vector3 - (Double, Double, Double)' operator */
    def testSubtract3Doubles()
    {
        var vec : Pos = new Pos(1, 1, 1)
        vec = vec - (1, 1, 1)
        Assert.assertEquals(0.0, vec.y, 0)
        Assert.assertEquals(0.0, vec.x, 0)
        Assert.assertEquals(0.0, vec.z, 0)
    }

    /** Tests the 'vector3 -= (Double, Double, Double)' operator */
    def testSubtractEqual3Doubles()
    {
        var vec : Pos = new Pos(1, 1, 1)
        vec -= (1, 1, 1)
        Assert.assertEquals(0.0, vec.y, 0)
        Assert.assertEquals(0.0, vec.x, 0)
        Assert.assertEquals(0.0, vec.z, 0)
    }

    /** Tests the 'vector3 - ForgeDirection' operator */
    def testSubtractForgeDirection()
    {
        var vec : Pos = new Pos(ForgeDirection.NORTH)
        vec = vec - ForgeDirection.NORTH
        Assert.assertEquals(0.0, vec.y, 0)
        Assert.assertEquals(0.0, vec.x, 0)
        Assert.assertEquals(0.0, vec.z, 0)
    }

    /** Tests the 'vector3 - ForgeDirection' operator */
    def testSubtractEqualForgeDirection()
    {
        var vec : Pos = new Pos(ForgeDirection.NORTH)
        vec -= ForgeDirection.NORTH
        Assert.assertEquals(0.0, vec.y, 0)
        Assert.assertEquals(0.0, vec.x, 0)
        Assert.assertEquals(0.0, vec.z, 0)
    }

    ////////////////////////////////////////////
    /// Addition operator tests               //
    ////////////////////////////////////////////

    /** Tests the 'vector3 - double' operator */
    def testAdditionDouble()
    {
        var vec : Pos = new Pos(-1, -1, -1)
        vec = vec + 1
        Assert.assertEquals(0.0, vec.y, 0)
        Assert.assertEquals(0.0, vec.x, 0)
        Assert.assertEquals(0.0, vec.z, 0)
    }

    /** Tests the 'vector3 - vector3' operator */
    def testAdditionVector3()
    {
        var vec : Pos = new Pos(-1, -1, -1)
        vec = vec + new Pos(1, 1, 1)
        Assert.assertEquals(0.0, vec.y, 0)
        Assert.assertEquals(0.0, vec.x, 0)
        Assert.assertEquals(0.0, vec.z, 0)
    }

    /** Tests the 'vector3 - (Double, Double, Double)' operator */
    def testAddition3Doubles()
    {
        var vec : Pos = new Pos(-1, -1, -1)
        vec = vec + (1, 1, 1)
        Assert.assertEquals(0.0, vec.y, 0)
        Assert.assertEquals(0.0, vec.x, 0)
        Assert.assertEquals(0.0, vec.z, 0)
    }

    /** Tests the 'vector3 -= (Double, Double, Double)' operator */
    def testAdditionEqual3Doubles()
    {
        var vec : Pos = new Pos(-1, -1, -1)
        vec += (1, 1, 1)
        Assert.assertEquals(0.0, vec.y, 0)
        Assert.assertEquals(0.0, vec.x, 0)
        Assert.assertEquals(0.0, vec.z, 0)
    }

    /** Tests the 'vector3 - ForgeDirection' operator */
    def testAdditionForgeDirection()
    {
        var vec : Pos = new Pos(ForgeDirection.NORTH)
        vec = vec + ForgeDirection.SOUTH
        Assert.assertEquals(0.0, vec.y, 0)
        Assert.assertEquals(0.0, vec.x, 0)
        Assert.assertEquals(0.0, vec.z, 0)
    }

    /** Tests the 'vector3 - ForgeDirection' operator */
    def testAdditionEqualForgeDirection()
    {
        var vec : Pos = new Pos(ForgeDirection.NORTH)
        vec += ForgeDirection.SOUTH
        Assert.assertEquals(0.0, vec.y, 0)
        Assert.assertEquals(0.0, vec.x, 0)
        Assert.assertEquals(0.0, vec.z, 0)
    }
}
