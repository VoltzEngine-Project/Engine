package com.builtbroken.test.transform.vector;

import com.builtbroken.mc.data.Direction;
import com.builtbroken.mc.imp.transform.vector.BlockPos;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.ForgeDirection;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/24/2017.
 */
@RunWith(VoltzTestRunner.class)
public class TestBlockPos extends AbstractTest
{
    @Test
    public void testInit3I()
    {
        BlockPos pos = new BlockPos(1, 2, 3);

        assertEquals(1, pos.x);
        assertEquals(2, pos.y);
        assertEquals(3, pos.z);
    }

    @Test
    public void testInitPos3D()
    {
        BlockPos pos = new BlockPos(new Pos(1, 2, 3));

        assertEquals(1, pos.x);
        assertEquals(2, pos.y);
        assertEquals(3, pos.z);
    }

    @Test
    public void testInitPos3DDirections()
    {
        for (Direction direction : Direction.DIRECTIONS)
        {
            BlockPos pos = new BlockPos(Pos.zero, direction);

            assertEquals(direction.offsetX, pos.x);
            assertEquals(direction.offsetY, pos.y);
            assertEquals(direction.offsetZ, pos.z);
        }
    }

    @Test
    public void testInitPos3DEnumFacing()
    {
        for (EnumFacing facing : EnumFacing.values())
        {
            BlockPos pos = new BlockPos(Pos.zero, facing);

            assertEquals(facing.getFrontOffsetX(), pos.x);
            assertEquals(facing.getFrontOffsetY(), pos.y);
            assertEquals(facing.getFrontOffsetZ(), pos.z);
        }
    }

    @Test
    public void testInitPos3DForgeDirection()
    {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            BlockPos pos = new BlockPos(Pos.zero, direction);

            assertEquals(direction.offsetX, pos.x);
            assertEquals(direction.offsetY, pos.y);
            assertEquals(direction.offsetZ, pos.z);
        }
    }

    @Test
    public void testGetPos()
    {
        BlockPos pos = new BlockPos(1, 2, 3);

        assertEquals(1, pos.xi());
        assertEquals(2, pos.yi());
        assertEquals(3, pos.zi());

        assertEquals(1.0, pos.x());
        assertEquals(2.0, pos.y());
        assertEquals(3.0, pos.z());
    }

    @Test
    public void testEquals()
    {
        BlockPos pos = new BlockPos(1, 2, 3);
        BlockPos pos2 = new BlockPos(1, 2, 3);

        assertEquals(pos, pos2);
    }

    @Test
    public void testNotEquals()
    {
        BlockPos pos = new BlockPos(1, 2, 3);
        BlockPos pos2 = new BlockPos(0, 0, 0);

        assertTrue(pos != pos2);
    }

    @Test
    public void testHashCode()
    {
        BlockPos pos = new BlockPos(1, 2, 3);
        BlockPos pos2 = new BlockPos(1, 2, 3);

        assertEquals(pos.hashCode(), pos2.hashCode());

        pos = new BlockPos(1, 2, 3);
        pos2 = new BlockPos(0, 0, 0);

        assertTrue(pos.hashCode() != pos2.hashCode());

        pos = new BlockPos(1, 0, 0);
        pos2 = new BlockPos(0, 0, 0);

        assertTrue(pos.hashCode() != pos2.hashCode());

        pos = new BlockPos(0, 1, 0);
        pos2 = new BlockPos(0, 0, 0);

        assertTrue(pos.hashCode() != pos2.hashCode());

        pos = new BlockPos(0, 0, 1);
        pos2 = new BlockPos(0, 0, 0);

        assertTrue(pos.hashCode() != pos2.hashCode());
    }

    @Test
    public void testListContains()
    {
        List<BlockPos> list = new ArrayList();
        list.add(new BlockPos(1, 2, 3));
        list.add(new BlockPos(1, 3, 3));
        list.add(new BlockPos(1, 4, 3));
        list.add(new BlockPos(1, 5, 3));

        assertTrue(list.contains(new BlockPos(1, 2, 3)));
        assertTrue(list.contains(new BlockPos(1, 3, 3)));
        assertTrue(list.contains(new BlockPos(1, 4, 3)));
        assertTrue(list.contains(new BlockPos(1, 5, 3)));

        assertFalse(list.contains(new BlockPos(0, 0, 0)));
        assertFalse(list.contains(new BlockPos(1, 0, 0)));
        assertFalse(list.contains(new BlockPos(0, 1, 0)));
        assertFalse(list.contains(new BlockPos(0, 0, 1)));
    }

    @Test
    public void testMapContains()
    {
        HashMap<BlockPos, Boolean> map = new HashMap();
        map.put(new BlockPos(1, 2, 3), false);
        map.put(new BlockPos(1, 3, 3), false);
        map.put(new BlockPos(1, 4, 3), false);
        map.put(new BlockPos(1, 5, 3), false);

        assertTrue(map.containsKey(new BlockPos(1, 2, 3)));
        assertTrue(map.containsKey(new BlockPos(1, 3, 3)));
        assertTrue(map.containsKey(new BlockPos(1, 4, 3)));
        assertTrue(map.containsKey(new BlockPos(1, 5, 3)));

        assertFalse(map.containsKey(new BlockPos(0, 0, 0)));
        assertFalse(map.containsKey(new BlockPos(1, 0, 0)));
        assertFalse(map.containsKey(new BlockPos(0, 1, 0)));
        assertFalse(map.containsKey(new BlockPos(0, 0, 1)));
    }
}
