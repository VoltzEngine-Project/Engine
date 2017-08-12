package com.builtbroken.mc.testing.tile;

import com.builtbroken.mc.abstraction.imp.EngineLoader;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import org.junit.Test;

/**
 * Prefab for testing anything that extends TileEntity. This prefab tries to test
 * the basic methods automatically to reduce testing them again.
 * Created by Dark on 9/11/2015.
 */
public abstract class AbstractTileEntityTest<T extends TileEntity, B extends Block> extends AbstractTest
{
    protected Class<T> tileClazz;
    protected B block;

    public AbstractTileEntityTest()
    {

    }

    public AbstractTileEntityTest(B block, Class<T> tileClazz)
    {
        this.block = block;
        this.tileClazz = tileClazz;
    }

    public AbstractTileEntityTest(B block, String name, Class<T> tileClazz)
    {
        this(block, tileClazz);
        try
        {
            TileEntity.addMapping(tileClazz, name);
        } catch (IllegalArgumentException e)
        {
            //Already registered
        }

    }

    @Override
    public void setUpForEntireClass()
    {
        super.setUpForEntireClass();
        if(tileClazz == null)
        {
            throw new IllegalArgumentException("TileClass can not be null");
        }
        if(block == null)
        {
            throw new IllegalArgumentException("Block can not be null");
        }
        if(Engine.loaderInstance == null)
        {
            Engine.loaderInstance = new EngineLoader()
            {
                @Override
                public Configuration getConfig()
                {
                    return null;
                }
            };
            Engine.packetHandler.init();
        }
    }

    protected T newTile()
    {
        try
        {
            return tileClazz.newInstance();
        } catch (InstantiationException e)
        {
            fail("Could create a new instance, need a constructor with zero arguments for default newTile method");
        } catch (IllegalAccessException e)
        {
            fail("Couldn't access constructor for tile, need a public constructor for default newTile method");
        }
        return null;
    }

    @Test
    public void testPlacement()
    {
        FakeWorld world = FakeWorld.newWorld("testGetBlockMetadata");
        for (int i = 0; i < 16; i++)
        {
            int placement_meta = block.damageDropped(i);
            world.setBlock(10, 10, 10, block, placement_meta, 3);
            assertTrue(world.getBlock(10, 10, 10) == block);
            assertTrue(world.getTileEntity(10, 10, 10).getClass() == tileClazz);
            world.getTileEntity(10, 10, 10).markDirty();

            //Test placement meta
            int meta = world.getBlockMetadata(10, 10, 10);
            int placed_meta = world.getTileEntity(10, 10, 10).getBlockMetadata();
            assertTrue("World meta = " + meta + " Tile meta = " + placed_meta, placed_meta == meta);
        }
    }

    /**
     * Tests {@link TileEntity#setWorldObj(World)} and {@link TileEntity#getWorldObj()}
     */
    @Test
    public void testWorldObj()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testGetWorldObj");
        assertNull(tile.getWorldObj());
        tile.setWorldObj(world);
        assertNotNull(tile.getWorldObj());
    }

    /** Tests {@link TileEntity#writeToNBT(NBTTagCompound)} */
    @Test
    public void testWriteToNBT()
    {
        T tile = newTile();
        tile.xCoord = 10;
        tile.yCoord = 11;
        tile.zCoord = 3;
        FakeWorld world = FakeWorld.newWorld("testWriteToNBT");
        tile.setWorldObj(world);

        NBTTagCompound nbt = new NBTTagCompound();
        assertTrue("NBT should have not tags on init", nbt.hasNoTags());
        tile.writeToNBT(nbt);
        assertTrue("NBT should have saved something", !nbt.hasNoTags());
        assertTrue("X coord should have saved", nbt.hasKey("x"));
        assertTrue("X should equal tile xCoord", nbt.getInteger("x") == tile.xCoord);
        assertTrue("Y coord should have saved", nbt.hasKey("y"));
        assertTrue("Y should equal tile yCoord", nbt.getInteger("y") == tile.yCoord);
        assertTrue("Z coord should have saved", nbt.hasKey("z"));
        assertTrue("Z should equal tile zCoord", nbt.getInteger("z") == tile.zCoord);
        assertTrue("ID tag should have saved", nbt.hasKey("id"));
    }

    /** Tests {@link TileEntity#readFromNBT(NBTTagCompound)} */
    @Test
    public void testReadFromNBT()
    {
        T tile = newTile();
        tile.xCoord = 10;
        tile.yCoord = 11;
        tile.zCoord = 3;
        FakeWorld world = FakeWorld.newWorld("testReadFromNBT");
        tile.setWorldObj(world);

        NBTTagCompound nbt = new NBTTagCompound();
        assertTrue("NBT should have not tags on init", nbt.hasNoTags());
        tile.writeToNBT(nbt);
        assertTrue("NBT should have saved something", !nbt.hasNoTags());

        tile = newTile();
        world = FakeWorld.newWorld("testReadFromNBT2");
        tile.setWorldObj(world);
        tile.readFromNBT(nbt);
        assertTrue(tile.xCoord == 10);
        assertTrue(tile.yCoord == 11);
        assertTrue(tile.zCoord == 3);
    }

    /** Tests {@link TileEntity#readFromNBT(NBTTagCompound)} */
    @Test
    public void testUpdateEntity()
    {
        FakeWorld world = FakeWorld.newWorld("testUpdateEntity");
        world.setBlock(10, 11, 12, block);
        TileEntity tile = world.getTileEntity(10, 11, 12);

        for (int i = 0; i < 1000; i++)
        {
            tile.updateEntity();
        }
    }

    @Test
    public void testMarkDirty()
    {
        FakeWorld world = FakeWorld.newWorld("testMarkDirty");
        world.setBlock(10, 11, 12, block);
        world.getTileEntity(10, 11, 12).markDirty();
    }

    @Test
    public void testGetBlockMetadata()
    {
        FakeWorld world = FakeWorld.newWorld("testGetBlockMetadata");
        world.setBlock(10, 11, 12, block);
        world.getTileEntity(10, 11, 12).markDirty();

        int meta = world.getBlockMetadata(10, 11, 12);
        assertTrue(world.getTileEntity(10, 11, 12).getBlockMetadata() == meta);
    }

    @Test
    public void testGetDistanceFrom()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testGetDistanceFrom");
        tile.setWorldObj(world);
        double distance = tile.getDistanceFrom(10, 14, 15);
        double distance2 = new Pos(0.5).sub(10, 14, 15).magnitudeSquared();
        assertTrue("Distance = " + distance + "  Distance2 = " + distance2, Math.abs(distance - distance2) < .01);
    }

    @Test
    public void testGetMaxRenderDistanceSquared()
    {

        FakeWorld world = FakeWorld.newWorld("testGetMaxRenderDistanceSquared");
        world.setBlock(10, 11, 12, block);
        double d = world.getTileEntity(10, 11, 12).getMaxRenderDistanceSquared();
        assertTrue(d >= 0);
    }

    @Test
    public void testGetBlockType()
    {
        FakeWorld world = FakeWorld.newWorld("testGetBlockType");
        world.setBlock(10, 11, 12, block);
        assertTrue(world.getTileEntity(10, 11, 12).getBlockType() == block);
    }

    @Test
    public void testGetDescriptionPacket()
    {
        //FakeWorld world = FakeWorld.newWorld("testGetDescriptionPacket");
        //world.setBlock(10, 11, 12, block);
        //world.getTileEntity(10, 11, 12).getDescriptionPacket();
    }

    @Test
    public void testIsInvalid()
    {
        FakeWorld world = FakeWorld.newWorld("testIsInvalid");
        world.setBlock(10, 11, 12, block);
        world.getTileEntity(10, 11, 12).isInvalid();
    }

    @Test
    public void testInvalidate()
    {
        FakeWorld world = FakeWorld.newWorld("testInvalidate");
        world.setBlock(10, 11, 12, block);
        TileEntity tile = world.getTileEntity(10, 11, 12);
        tile.invalidate();
        assertTrue(tile.isInvalid());
    }

    @Test
    public void testValidate()
    {
        FakeWorld world = FakeWorld.newWorld("testValidate");
        world.setBlock(10, 11, 12, block);
        TileEntity tile = world.getTileEntity(10, 11, 12);
        tile.validate();
        assertTrue(!tile.isInvalid());
    }

    @Test
    public void testReceiveClientEvent()
    {
        //Test for no errors/crash plus enforce unit test on this method
        FakeWorld world = FakeWorld.newWorld("testReceiveClientEvent");
        world.setBlock(10, 11, 12, block);
        assertFalse("Might want to implement a test for this if you return true", world.getTileEntity(10, 11, 12).receiveClientEvent(0, 0));
    }

    @Test
    public void testUpdateContainingBlockInfo()
    {
        //Test for no errors/crash
        FakeWorld world = FakeWorld.newWorld("testUpdateContainingBlockInfo");
        world.setBlock(10, 11, 12, block);
        world.getTileEntity(10, 11, 12).updateContainingBlockInfo();
    }

    @Test
    public void testCanUpdate()
    {
        //Test for no errors/crash
        FakeWorld world = FakeWorld.newWorld("testCanUpdate");
        world.setBlock(10, 11, 12, block);
        world.getTileEntity(10, 11, 12).canUpdate();
    }

    @Test
    public void testOnDataPacket()
    {
        //Test for no errors/crash
        FakeWorld world = FakeWorld.newWorld("testOnDataPacket");
        world.setBlock(10, 11, 12, block);
        world.getTileEntity(10, 11, 12).onDataPacket(null, null);
    }

    @Test
    public void testOnChunkUnload()
    {
        //Test for no errors/crash
        FakeWorld world = FakeWorld.newWorld("testOnChunkUnload");
        world.setBlock(10, 11, 12, block);
        world.getTileEntity(10, 11, 12).onChunkUnload();
    }

    @Test
    public void testShouldRefresh()
    {
        //Test for no errors/crash
        FakeWorld world = FakeWorld.newWorld("testShouldRefresh");
        world.setBlock(10, 11, 12, block);
        world.getTileEntity(10, 11, 12).shouldRefresh(block, block, world.getBlockMetadata(10, 11, 12), world.getBlockMetadata(10, 11, 12), world, 10, 11, 12);
    }

    @Test
    public void testShouldRenderInPass()
    {
        FakeWorld world = FakeWorld.newWorld("testShouldRenderInPass");
        world.setBlock(10, 11, 12, block);
        world.getTileEntity(10, 11, 12).shouldRenderInPass(0);
    }

    @Test
    public void testGetRenderBoundingBox()
    {
        FakeWorld world = FakeWorld.newWorld("testGetRenderBoundingBox");
        world.setBlock(10, 11, 12, block);
        AxisAlignedBB bounds = world.getTileEntity(10, 11, 12).getRenderBoundingBox();
        if(bounds != null)
        {
            //Check to ensure bounds are not inverted
            assertFalse(bounds.minX > bounds.maxX);
            assertFalse(bounds.minY > bounds.maxY);
            assertFalse(bounds.minZ > bounds.maxZ);

            //Check to ensure that bound are not all the same
            assertFalse(bounds.minZ == bounds.maxZ && bounds.minY == bounds.maxY && bounds.minZ == bounds.maxZ);
        }
    }
}
