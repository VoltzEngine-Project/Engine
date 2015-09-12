package com.builtbroken.mc.testing.tile;

import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.junit.Test;

/**
 * Prefab for testing anything that extends TileEntity. This prefab tries to test
 * the basic methods automatically to reduce testing them again.
 * Created by Dark on 9/11/2015.
 */
public class AbstractTileEntityTest<T extends TileEntity, B extends Block> extends AbstractTest
{
    protected final Class<T> tileClazz;
    protected final B block;

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
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testUpdateEntity");
        tile.setWorldObj(world);

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
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testGetMaxRenderDistanceSquared");
        tile.setWorldObj(world);
    }

    @Test
    public void testGetBlockType()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testGetBlockType");
        tile.setWorldObj(world);
    }

    @Test
    public void testGetDescriptionPacket()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testGetDescriptionPacket");
        tile.setWorldObj(world);
    }

    @Test
    public void testIsInvalid()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testIsInvalid");
        tile.setWorldObj(world);
    }

    @Test
    public void testInvalidate()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testInvalidate");
        tile.setWorldObj(world);
    }

    @Test
    public void testValidate()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testValidate");
        tile.setWorldObj(world);
    }

    @Test
    public void testReceiveClientEvent()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testReceiveClientEvent");
        tile.setWorldObj(world);
    }

    @Test
    public void testUpdateContainingBlockInfo()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testUpdateContainingBlockInfo");
        tile.setWorldObj(world);
    }

    @Test
    public void testCanUpdate()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testCanUpdate");
        tile.setWorldObj(world);
    }

    @Test
    public void testOnDataPacket()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testOnDataPacket");
        tile.setWorldObj(world);
    }

    @Test
    public void testOnChunkUnload()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testOnChunkUnload");
        tile.setWorldObj(world);
    }

    @Test
    public void testShouldRefresh()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testShouldRefresh");
        tile.setWorldObj(world);
    }

    @Test
    public void testShouldRenderInPass()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testShouldRenderInPass");
        tile.setWorldObj(world);
    }

    @Test
    public void testGetRenderBoundingBox()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testGetRenderBoundingBox");
        tile.setWorldObj(world);
    }
}
