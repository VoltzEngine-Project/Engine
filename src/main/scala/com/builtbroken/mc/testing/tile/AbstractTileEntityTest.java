package com.builtbroken.mc.testing.tile;

import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Prefab for testing anything that extends TileEntity. This prefab tries to test
 * the basic methods automatically to reduce testing them again.
 * Created by Dark on 9/11/2015.
 */
public class AbstractTileEntityTest<T extends TileEntity, B extends Block> extends AbstractTest
{
    protected Class<T> tileClazz;
    protected B block;

    public AbstractTileEntityTest(B block, String name, Class<T> tileClazz)
    {
        this.tileClazz = tileClazz;
        try
        {
            TileEntity.addMapping(tileClazz, name);
        } catch (IllegalArgumentException e)
        {
            //Already registered
        }
        this.block = block;
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

    /**
     * Tests {@link TileEntity#setWorldObj(World)} and {@link TileEntity#getWorldObj()}
     */
    public void testWorldObj()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testGetWorldObj");
        assertNull(tile.getWorldObj());
        tile.setWorldObj(world);
        assertNotNull(tile.getWorldObj());
    }

    public void testReadFromNBT()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testReadFromNBT");
    }

    public void testWriteToNBT()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testWriteToNBT");
    }

    public void testUpdateEntity()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testUpdateEntity");
    }


    public void testGetBlockMetadata()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testGetBlockMetadata");
    }

    public void testMarkDirty()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testMarkDirty");
    }

    public void testGetDistanceFrom()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testGetDistanceFrom");
    }

    public void testGetMaxRenderDistanceSquared()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testGetMaxRenderDistanceSquared");
    }

    public void testGetBlockType()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testGetBlockType");
    }

    public void testGetDescriptionPacket()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testGetDescriptionPacket");
    }

    public void testIsInvalid()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testIsInvalid");
    }

    public void testInvalidate()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testInvalidate");
    }

    public void testValidate()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testValidate");
    }

    public void testReceiveClientEvent()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testReceiveClientEvent");
    }

    public void testUpdateContainingBlockInfo()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testUpdateContainingBlockInfo");
    }

    public void testCanUpdate()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testCanUpdate");
    }

    public void testOnDataPacket()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testOnDataPacket");
    }

    public void testOnChunkUnload()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testOnChunkUnload");
    }

    public void testShouldRefresh()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testShouldRefresh");
    }

    public void testShouldRenderInPass()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testShouldRenderInPass");
    }

    public void testGetRenderBoundingBox()
    {
        T tile = newTile();
        FakeWorld world = FakeWorld.newWorld("testGetRenderBoundingBox");
    }
}
