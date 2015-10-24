package com.builtbroken.test.transform.vector;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.lib.transform.vector.AbstractLocation;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.server.FakeDedicatedServer;
import com.builtbroken.mc.testing.junit.testers.TestPlayer;
import com.builtbroken.mc.testing.junit.world.FakeWorldServer;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit test for {@link com.builtbroken.mc.lib.transform.vector.AbstractLocation}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/24/2015.
 */
@RunWith(VoltzTestRunner.class)
public class TestAbstractLocation extends AbstractTest
{
    /** MC server instance for the entire class file to use */
    protected MinecraftServer server;
    /** World server to build tests inside, make sure to clean up as its used over all tests in this class */
    protected FakeWorldServer world;
    /** Tester that has not choice in what to test, test between tests but not deleted. Make sure to cleanup non-vanilla data between tests */
    protected TestPlayer player;

    @Test
    public void testCoverage()
    {
        assertSame(TLocation.class.getConstructors().length, AbstractLocation.class.getConstructors().length);
        checkNumberOfDeclaredMethods(AbstractLocation.class, 23);
    }

    @Override
    public void setUpForEntireClass()
    {
        super.setUpForEntireClass();
        server = new FakeDedicatedServer(new File(FakeWorldServer.baseFolder, "TestAbstractLocation"));
        world = FakeWorldServer.newWorld(server, "TestAbstractLocation");
        player = new TestPlayer(server, world, new GameProfile(null, "TileTester"));
    }

    /**
     * Tests constructors
     */
    public void testInit()
    {
        List<TLocation> locations = new ArrayList();
        //Test main method
        locations.add(new TLocation(world, 10, 11, 12));
        //Test entity method
        Entity entity = new EntitySheep(world);
        entity.setPosition(10, 11, 12);
        locations.add(new TLocation(entity));
        //Test tile
        TileEntity tile = new TileEntity();
        tile.setWorldObj(world);
        tile.xCoord = 10;
        tile.yCoord = 11;
        tile.zCoord = 12;
        locations.add(new TLocation(tile));
        //Test NBT method
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("dimension", world.provider.dimensionId);
        tag.setDouble("x", 10);
        tag.setDouble("y", 11);
        tag.setDouble("z", 12);
        locations.add(new TLocation(tag));
        //Test byte buf method
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(world.provider.dimensionId);
        buf.writeDouble(10);
        buf.writeDouble(11);
        buf.writeDouble(12);
        locations.add(new TLocation(buf));
        //Test IWorldPosition
        locations.add(new TLocation(new IWorldPosition()
        {
            @Override
            public World world()
            {
                return world;
            }

            @Override
            public double x()
            {
                return 10;
            }

            @Override
            public double y()
            {
                return 11;
            }

            @Override
            public double z()
            {
                return 12;
            }
        }));
        //Test world, IPos3D
        locations.add(new TLocation(world, new IPos3D()
        {
            @Override
            public double x()
            {
                return 10;
            }

            @Override
            public double y()
            {
                return 11;
            }

            @Override
            public double z()
            {
                return 12;
            }
        }));
        //Test world, vec3
        locations.add(new TLocation(world, Vec3.createVectorHelper(10, 11, 12)));
        //Test world, moving object
        locations.add(new TLocation(world, new MovingObjectPosition(10, 11, 12, 0, Vec3.createVectorHelper(10, 11, 12))));

        for (int i = 0; i < locations.size(); i++)
        {
            TLocation location = locations.get(i);
            assertTrue("" + i, location.world == world);
            assertTrue("" + i, location.xi() == 10);
            assertTrue("" + i, location.yi() == 11);
            assertTrue("" + i, location.zi() == 12);
        }
    }


    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#equals(Object) */
    public void testEquals()
    {
        TLocation location = new TLocation(world, 1, 2, 3);
        //True
        assertTrue(location.equals(location));
        assertTrue(location.equals(new TLocation(world, 1, 2, 3)));
        //Normal checks
        assertFalse(location.equals(new TLocation(world, 1, 3, 3)));
        assertFalse(location.equals(new TLocation(world, 1, 2, 4)));
        assertFalse(location.equals(new TLocation(world, 2, 3, 3)));
        assertFalse(location.equals(new TLocation(null, 1, 3, 3)));
        //Stupid junk test... because i'm really really bored
        assertFalse(location.equals(null));
        assertFalse(location.equals(2));
        assertFalse(location.equals(2.0));
        assertFalse(location.equals(2f));
        assertFalse(location.equals("fuck it"));
        assertFalse(location.equals(new IPos3D()
        {
            @Override
            public double x()
            {
                return 10;
            }

            @Override
            public double y()
            {
                return 11;
            }

            @Override
            public double z()
            {
                return 12;
            }
        }));
        assertFalse(location.equals(new IPos3D()
        {
            @Override
            public double x()
            {
                return 1;
            }

            @Override
            public double y()
            {
                return 2;
            }

            @Override
            public double z()
            {
                return 3;
            }
        }));
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#toString() */
    public void testToString()
    {
        String s = "WorldLocation [1.0x,2.0y,3.0z," + world.provider.dimensionId + "d]";
        String s2 = new TLocation(world, 1, 2, 3).toString();
        assertTrue("Should be " + s + " but is " + s2, s.equals(s2));
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#getBlock() */
    public void testGetBlock()
    {
        world.setBlock(0, 10, 10, Blocks.ice);
        assertSame(Blocks.ice, new TLocation(world, 0, 10, 10).getBlock());
        world.setBlockToAir(0, 10, 10);
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#isBlockEqual(Block) */
    public void testIsBlockEqual()
    {
        world.setBlock(0, 10, 10, Blocks.ice);
        assertTrue(new TLocation(world, 0, 10, 10).isBlockEqual(Blocks.ice));
        world.setBlockToAir(0, 10, 10);
        assertFalse(new TLocation(world, 0, 10, 10).isBlockEqual(Blocks.ice));
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#markForUpdate() */
    public void testMarkForUpdate()
    {
        world.setBlock(0, 10, 10, Blocks.ice);
        new TLocation(world, 0, 10, 10).markForUpdate();
        world.setBlockToAir(0, 10, 10);
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#getHardness() */
    public void testGetHardness()
    {
        world.setBlock(0, 10, 10, Blocks.ice);
        assertEquals(new TLocation(world, 0, 10, 10).getHardness(), Blocks.ice.blockHardness);
        world.setBlockToAir(0, 10, 10);
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#isChunkLoaded() */
    public void testIsChunkLoaded()
    {
        //Range needs to be high due to spawn range
        //IF fails consider relocating range away from spawn
        TLocation location = new TLocation(world, 1000, 10, 10);
        assertFalse(location.isChunkLoaded());
        world.setBlock(1000, 10, 10, Blocks.ice);
        assertTrue(location.isChunkLoaded());
        world.setBlockToAir(1000, 10, 10);
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#getTileEntity() */
    public void testGetTileEntity()
    {
        TLocation location = new TLocation(world, 20, 10, 10);
        assertTrue(location.getTileEntity() == null);
        world.setBlock(20, 10, 10, Blocks.stone_brick_stairs);
        assertTrue(location.getTileEntity() == null);
        world.setBlock(20, 10, 10, Blocks.chest);
        assertTrue(location.getTileEntity() instanceof TileEntityChest);
        world.setBlockToAir(20, 10, 10);
        assertTrue(location.getTileEntity() == null);
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#getResistance(Entity, double, double, double) */
    public void testGetResistance()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#setBlock(Block, int) */
    public void testSetBlock1()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#setBlock(Block) */
    public void testSetBlock2()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#setBlock(Block, int, int) */
    public void testSetBlock3()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#isAirBlock() */
    public void testIsAirBlock()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#setBlockToAir() */
    public void testSetBlockToAir()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#getChunk() */
    public void testGetChunk()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#world() */
    public void testWorld()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#getWorld() */
    public void testGetWorld()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#writeNBT(NBTTagCompound) */
    public void testWriteNBT()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#toVector3() */
    public void testToVector3()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#toPos() */
    public void testToPos()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#writeByteBuf(ByteBuf) */
    public void testWriteByteBuf()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#getBlockMetadata() */
    public void testGetBlockMetadata()
    {
    }

    /** Tests {@link class com.builtbroken.mc.lib.transform.vector.AbstractLocation#isBlockFreezable() */
    public void testIsBlockFreezable()
    {
    }

    /** Used to test AbstractLocation since it is abstract */
    private static class TLocation extends AbstractLocation<TLocation>
    {
        public TLocation(World world, double x, double y, double z)
        {
            super(world, x, y, z);
        }

        public TLocation(NBTTagCompound nbt)
        {
            super(nbt);
        }

        public TLocation(ByteBuf data)
        {
            super(data);
        }

        public TLocation(Entity entity)
        {
            super(entity);
        }

        public TLocation(TileEntity tile)
        {
            super(tile);
        }

        public TLocation(IWorldPosition vec)
        {
            super(vec);
        }

        public TLocation(World world, IPos3D vector)
        {
            super(world, vector);
        }

        public TLocation(World world, Vec3 vec)
        {
            super(world, vec);
        }

        public TLocation(World world, MovingObjectPosition target)
        {
            super(world, target);
        }

        @Override
        public TLocation newPos(double x, double y, double z)
        {
            return new TLocation(world(), x, y, z);
        }
    }
}
