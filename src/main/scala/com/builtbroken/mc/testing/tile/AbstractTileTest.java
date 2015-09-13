package com.builtbroken.mc.testing.tile;

import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.tile.BlockTile;
import com.builtbroken.mc.prefab.tile.Tile;
import com.builtbroken.mc.testing.junit.server.FakeDedicatedServer;
import com.builtbroken.mc.testing.junit.testers.TestPlayer;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import com.builtbroken.mc.testing.junit.world.FakeWorldServer;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.util.ForgeDirection;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * prefab for testing Tile objects. Tries to
 * Created by Dark on 9/11/2015.
 */
public class AbstractTileTest<T extends Tile, B extends BlockTile> extends AbstractTileEntityTest<T, B>
{
    //These are only used for testing player interaction, if you only need a world create a new FakeWorld
    /** MC server instance for the entire class file to use */
    protected MinecraftServer server;
    /** World server to build tests inside, make sure to clean up as its used over all tests in this class */
    protected FakeWorldServer world;
    /** Tester that has not choice in what to test, test between tests but not deleted. Make sure to cleanup non-vanilla data between tests */
    protected TestPlayer player;

    public AbstractTileTest()
    {

    }

    public AbstractTileTest(B block, String name, Class<T> tileClazz)
    {
        super(block, name, tileClazz);
    }

    @Override
    public void setUpForEntireClass()
    {
        super.setUpForEntireClass();
        server = new FakeDedicatedServer(new File(FakeWorldServer.baseFolder, tileClazz.getName()));
        world = FakeWorldServer.newWorld(server, tileClazz.getName());
        player = new TestPlayer(server, world, new GameProfile(null, "TileTester"));
    }

    @Override
    public void setUpForTest(String name)
    {
        super.setUpForTest(name);
        player.reset();
    }

    @Test
    public void testCoverage()
    {
        Method[] methods = Tile.class.getMethods();
        assertTrue("There are " + methods.length + " but should be ", methods.length == 139);
    }

    @Test
    public void testX()
    {
        FakeWorld world = FakeWorld.newWorld("TestX");
        world.setBlock(0, 0, 0, block);
        TileEntity tile = world.getTileEntity(0, 0, 0);
        assertTrue(tile.getClass() == tileClazz);
        assertTrue(tile.xCoord == (int) ((T) tile).x());
    }

    @Test
    public void testY()
    {
        FakeWorld world = FakeWorld.newWorld("TestY");
        world.setBlock(0, 0, 0, block);
        TileEntity tile = world.getTileEntity(0, 0, 0);
        assertTrue(tile.getClass() == tileClazz);
        assertTrue(tile.yCoord == (int) ((T) tile).y());
    }

    @Test
    public void testZ()
    {
        FakeWorld world = FakeWorld.newWorld("TestZ");
        world.setBlock(0, 0, 0, block);
        TileEntity tile = world.getTileEntity(0, 0, 0);
        assertTrue(tile.getClass() == tileClazz);
        assertTrue(tile.zCoord == (int) ((T) tile).z());
    }

    @Test
    public void testXi()
    {
        FakeWorld world = FakeWorld.newWorld("TestXi");
        world.setBlock(0, 0, 0, block);
        TileEntity tile = world.getTileEntity(0, 0, 0);
        assertTrue(tile.getClass() == tileClazz);
        assertTrue(tile.xCoord == ((T) tile).xi());
    }

    @Test
    public void testYi()
    {
        FakeWorld world = FakeWorld.newWorld("TestYi");
        world.setBlock(0, 0, 0, block);
        TileEntity tile = world.getTileEntity(0, 0, 0);
        assertTrue(tile.getClass() == tileClazz);
        assertTrue(tile.yCoord == ((T) tile).yi());
    }

    @Test
    public void testZi()
    {
        FakeWorld world = FakeWorld.newWorld("TestZi");
        world.setBlock(0, 0, 0, block);
        TileEntity tile = world.getTileEntity(0, 0, 0);
        assertTrue(tile.getClass() == tileClazz);
        assertTrue(tile.zCoord == ((T) tile).zi());
    }

    @Test
    public void testUpdate()
    {
        FakeWorld world = FakeWorld.newWorld("TestUpdate");
        world.setBlock(0, 0, 0, block);
        Tile tile = (Tile) world.getTileEntity(0, 0, 0);
        //5 Seconds of run time
        for (int i = 0; i < 100; i++)
        {
            tile.ticks = i;
            tile.update();
        }
    }

    @Test
    @Override
    public void testUpdateEntity()
    {
        FakeWorld world = FakeWorld.newWorld("testUpdateEntity");
        world.setBlock(10, 11, 12, block);
        Tile tile = (Tile) world.getTileEntity(10, 11, 12);

        //50 seconds of run time
        for (int i = 0; i < 1000; i++)
        {
            assertTrue(tile.ticks == i);
            tile.updateEntity();
        }
    }

    @Test
    public void testWorld()
    {
        FakeWorld world = FakeWorld.newWorld("TestWorld");
        world.setBlock(0, 0, 0, block);
        Tile tile = (Tile) world.getTileEntity(0, 0, 0);
        assertTrue(tile.world() == world);
    }

    @Test
    public void testOnPlayerLeftClick()
    {
        //Testing for no crashes during method call
        world.setBlock(0, 0, 0, block);
        Tile tile = (Tile) world.getTileEntity(0, 0, 0);
        tile.onPlayerLeftClick(player);
    }

    @Test
    public void testDoUpdateGuiUsers()
    {
        //Testing for no crashes during method call
        FakeWorld world = FakeWorld.newWorld("TestDoUpdateGuiUsers");
        world.setBlock(0, 0, 0, block);
        Tile tile = (Tile) world.getTileEntity(0, 0, 0);
        tile.doUpdateGuiUsers();
    }

    @Test
    public void testDoCleanupCheck()
    {
        FakeWorld world = FakeWorld.newWorld("TestDoCleanupCheck");
        world.setBlock(0, 0, 0, block);
        Tile tile = (Tile) world.getTileEntity(0, 0, 0);
        tile.doCleanupCheck();
    }

    @Test
    public void testOnPlayerActivated()
    {
        world.setBlock(0, 0, 0, block);
        Tile tile = (Tile) world.getTileEntity(0, 0, 0);
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            //We divide the face of the tile into 16 sub sections
            for (int i = 0; i <= 256; i++)
            {
                tile.onPlayerActivated(player, dir.ordinal(), getNextClick(dir, i));
            }
        }
    }

    protected Pos getNextClick(ForgeDirection dir, int index)
    {
        double i = (double) index / 256.0;
        double k = (double) index % 256.0;
        switch (dir)
        {
            case NORTH:
            case SOUTH:
                return new Pos(i, k, 0);
            case EAST:
            case WEST:
                return new Pos(0, i, k);
            case UP:
            case DOWN:
                return new Pos(i, 0, k);
        }
        return null;
    }

    @Test
    public void testOnPlayerRightClick()
    {
        world.setBlock(0, 0, 0, block);
        Tile tile = (Tile) world.getTileEntity(0, 0, 0);

        try
        {
            Method method = Tile.class.getDeclaredMethod("onPlayerRightClick", EntityPlayer.class, Integer.TYPE, Pos.class);
            method.setAccessible(true);

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
            {
                //We divide the face of the tile into 256 sub sections
                for (int i = 0; i <= 256; i++)
                {
                    method.invoke(tile, player, dir.ordinal(), getNextClick(dir, i));
                }
            }
        } catch (NoSuchMethodException e)
        {
            fail("Could not find method onPlayerRightClick");
        } catch (InvocationTargetException e)
        {
            fail("Failed to invoke method onPlayerRightClick");
        } catch (IllegalAccessException e)
        {
            fail("Couldn't access method onPlayerRightClick");
        }
    }


    @Test
    public void testOnPlayerRightClickWrench()
    {
        world.setBlock(0, 0, 0, block);
        Tile tile = (Tile) world.getTileEntity(0, 0, 0);

        try
        {
            Method method = Tile.class.getDeclaredMethod("onPlayerRightClickWrench", EntityPlayer.class, Integer.TYPE, Pos.class);
            method.setAccessible(true);

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
            {
                //We divide the face of the tile into 256 sub sections
                for (int i = 0; i <= 256; i++)
                {
                    method.invoke(tile, player, dir.ordinal(), getNextClick(dir, i));
                }
            }
        } catch (NoSuchMethodException e)
        {
            fail("Could not find method onPlayerRightClickWrench");
        } catch (InvocationTargetException e)
        {
            fail("Failed to invoke method onPlayerRightClickWrench");
        } catch (IllegalAccessException e)
        {
            fail("Couldn't access method onPlayerRightClickWrench");
        }
    }


    @Test
    public void testQuantityDropped()
    {
        FakeWorld world = FakeWorld.newWorld("TestQuantityDropped");
        world.setBlock(0, 0, 0, block);
        ((Tile) world.getTileEntity(0, 0, 0)).quantityDropped(0, 0);
    }

    @Test
    public void testMetadataDropped()
    {
        FakeWorld world = FakeWorld.newWorld("TestMetadataDropped");
        world.setBlock(0, 0, 0, block);
        ((Tile) world.getTileEntity(0, 0, 0)).metadataDropped(0, 0);
    }

    @Test
    public void testOnClientRegistered()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnClientRegistered");
        world.setBlock(0, 0, 0, block);
        ((Tile) world.getTileEntity(0, 0, 0)).onClientRegistered();
    }

    @Test
    public void testOnWorldSeparate()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnWorldSeparate");
        world.setBlock(0, 0, 0, block);
        ((Tile) world.getTileEntity(0, 0, 0)).onWorldSeparate();
    }

    @Test
    public void testOnDestroyedByExplosion()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnDestroyedByExplosion");
        world.setBlock(0, 0, 0, block);
        ((Tile) world.getTileEntity(0, 0, 0)).onDestroyedByExplosion(new Explosion(world, null, 0, 0, 0, 10));
    }

    @Test
    public void testOnNeighborChanged()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnNeighborChanged");
        world.setBlock(0, 0, 0, block);
        ((Tile) world.getTileEntity(0, 0, 0)).onNeighborChanged(block);
    }

    @Test
    public void testGetTextureName()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetTextureName");
        world.setBlock(0, 0, 0, block);
        try
        {
            Method method = Tile.class.getDeclaredMethod("getTextureName");
            method.setAccessible(true);
            Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
            assertTrue(method.invoke(tile) != null);

        } catch (NoSuchMethodException e)
        {
            fail("Could not find method getTextureName");
        } catch (InvocationTargetException e)
        {
            fail("Failed to invoke method getTextureName");
        } catch (IllegalAccessException e)
        {
            fail("Couldn't access method getTextureName");
        }
    }

    @Test
    public void testGetStrongestIndirectpower()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetStrongestIndirectpower");
        world.setBlock(0, 0, 0, block);
        ((Tile) world.getTileEntity(0, 0, 0)).getStrongestIndirectPower();
    }

    @Test
    public void testGetSelectBounds()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetSelectBounds");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        Cube cube = tile.getSelectBounds();
        assertTrue(cube != null);
        //TODO add check to ensure cube size and values are good
    }

    @Test
    public void testSetBlockBoundsBasedOnState()
    {
        FakeWorld world = FakeWorld.newWorld("TestSetBlockBoundsBasedOnState");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testSetTextureName()
    {
        FakeWorld world = FakeWorld.newWorld("TestSetTextureName");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetBlockMetadata()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetBlockMetadata");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testCanSilkHarvest()
    {
        FakeWorld world = FakeWorld.newWorld("TestCanSilkHarvest");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetDescriptionpacket()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetDescriptionpacket");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testShouldSideBeRendered()
    {
        FakeWorld world = FakeWorld.newWorld("TestShouldSideBeRendered");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetCollisionBounds()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetCollisionBounds");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetRenderBoundingBox()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetRenderBoundingBox");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetWeakRedstonepower()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetWeakRedstonepower");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetRenderBlockpass()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetRenderBlockpass");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetRenderColor()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetRenderColor");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testDetermineOrientation()
    {
        FakeWorld world = FakeWorld.newWorld("TestDetermineOrientation");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetplayersUsing()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetplayersUsing");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testRemoveByplayer()
    {
        FakeWorld world = FakeWorld.newWorld("TestRemoveByplayer");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testNotifyBlocksOfNeighborChange()
    {
        FakeWorld world = FakeWorld.newWorld("TestNotifyBlocksOfNeighborChange");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetExplosionResistance()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetExplosionResistance");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testRenderInventory()
    {
        FakeWorld world = FakeWorld.newWorld("TestRenderInventory");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testSendDescpacket()
    {
        FakeWorld world = FakeWorld.newWorld("TestSendDescpacket");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testSendpacketToServer()
    {
        FakeWorld world = FakeWorld.newWorld("TestSendpacketToServer");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetColorMultiplier()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetColorMultiplier");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetCollisionBoxes()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetCollisionBoxes");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testSendpacketToGuiUsers()
    {
        FakeWorld world = FakeWorld.newWorld("TestSendpacketToGuiUsers");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testDetermineForgeDirection()
    {
        FakeWorld world = FakeWorld.newWorld("TestDetermineForgeDirection");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetStrongRedstonepower()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetStrongRedstonepower");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testCanplaceBlockAt()
    {
        FakeWorld world = FakeWorld.newWorld("TestCanplaceBlockAt");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testCanplaceBlockOnSide()
    {
        FakeWorld world = FakeWorld.newWorld("TestCanplaceBlockOnSide");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testRandomDisplayTick()
    {
        FakeWorld world = FakeWorld.newWorld("TestRandomDisplayTick");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testRegisterSideTextureSet()
    {
        FakeWorld world = FakeWorld.newWorld("TestRegisterSideTextureSet");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetOwnerprofile()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetOwnerprofile");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetSpecialRenderer()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetSpecialRenderer");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testIsIndirectlypowered()
    {
        FakeWorld world = FakeWorld.newWorld("TestIsIndirectlypowered");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testHasSpecialRenderer()
    {
        FakeWorld world = FakeWorld.newWorld("TestHasSpecialRenderer");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testNewTile()
    {
        FakeWorld world = FakeWorld.newWorld("TestNewTile");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetBlockType()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetBlockType");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetSubBlocks()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetSubBlocks");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testToPos()
    {
        FakeWorld world = FakeWorld.newWorld("TestTopos");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testToVectorWorld()
    {
        FakeWorld world = FakeWorld.newWorld("TestToVectorWorld");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetDrops()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetDrops");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testOnWorldJoin()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnWorldJoin");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testOnplaced()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnplaced");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testOnpostplaced()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnpostplaced");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testOnRemove()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnRemove");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetTileBlock()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetTileBlock");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testWriteToNBT()
    {
        FakeWorld world = FakeWorld.newWorld("TestWriteToNBT");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testOpenGui()
    {
        FakeWorld world = FakeWorld.newWorld("TestOpenGui");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testOnAdded()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnAdded");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testToItemStack()
    {
        FakeWorld world = FakeWorld.newWorld("TestToItemStack");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testOnCollide()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnCollide");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetAccess()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetAccess");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testToVector3()
    {
        FakeWorld world = FakeWorld.newWorld("TestToVector3");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testReadFromNBT()
    {
        FakeWorld world = FakeWorld.newWorld("TestReadFromNBT");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testSetAccess()
    {
        FakeWorld world = FakeWorld.newWorld("TestSetAccess");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetpickBlock()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetpickBlock");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testToLocation()
    {
        FakeWorld world = FakeWorld.newWorld("TestToLocation");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testOnRegistered()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnRegistered");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testBlockUpdate()
    {
        FakeWorld world = FakeWorld.newWorld("TestBlockUpdate");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testFirstTick()
    {
        FakeWorld world = FakeWorld.newWorld("TestFirstTick");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetBlockColor()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetBlockColor");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetSideIcon()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetSideIcon");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetOwnerID()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetOwnerID");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testDistance()
    {
        FakeWorld world = FakeWorld.newWorld("TestDistance");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testScheduleTick()
    {
        FakeWorld world = FakeWorld.newWorld("TestScheduleTick");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetSaveData()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetSaveData");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testRenderDynamic()
    {
        FakeWorld world = FakeWorld.newWorld("TestRenderDynamic");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testIsClient()
    {
        FakeWorld world = FakeWorld.newWorld("TestIsClient");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testSetOwner()
    {
        FakeWorld world = FakeWorld.newWorld("TestSetOwner");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testSetMeta()
    {
        FakeWorld world = FakeWorld.newWorld("TestSetMeta");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testRegisterIcons()
    {
        FakeWorld world = FakeWorld.newWorld("TestRegisterIcons");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testSetOwnerID()
    {
        FakeWorld world = FakeWorld.newWorld("TestSetOwnerID");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetDescpacket()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetDescpacket");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testUpdateLight()
    {
        FakeWorld world = FakeWorld.newWorld("TestUpdateLight");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetIcon()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetIcon");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testOnFillRain()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnFillRain");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetOwnerName()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetOwnerName");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testMarkUpdate()
    {
        FakeWorld world = FakeWorld.newWorld("TestMarkUpdate");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetTopIcon()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetTopIcon");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testMarkRender()
    {
        FakeWorld world = FakeWorld.newWorld("TestMarkRender");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testIsServer()
    {
        FakeWorld world = FakeWorld.newWorld("TestIsServer");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetLightValue()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetLightValue");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetBottomIcon()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetBottomIcon");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testIsSolid()
    {
        FakeWorld world = FakeWorld.newWorld("TestIsSolid");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testRenderStatic()
    {
        FakeWorld world = FakeWorld.newWorld("TestRenderStatic");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testTickRate()
    {
        FakeWorld world = FakeWorld.newWorld("TestTickRate");
        world.setBlock(0, 0, 0, block);
    }

    @Test
    public void testGetMetadata()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetMetadata");
        world.setBlock(0, 0, 0, block);
    }
}
