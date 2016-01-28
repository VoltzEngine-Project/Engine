package com.builtbroken.mc.testing.tile;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.tile.BlockTile;
import com.builtbroken.mc.prefab.tile.Tile;
import com.builtbroken.mc.testing.junit.ModRegistry;
import com.builtbroken.mc.testing.junit.icons.SudoIconReg;
import com.builtbroken.mc.testing.junit.server.FakeDedicatedServer;
import com.builtbroken.mc.testing.junit.testers.TestPlayer;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import com.builtbroken.mc.testing.junit.world.FakeWorldServer;
import com.mojang.authlib.GameProfile;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.util.ForgeDirection;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * prefab for testing Tile objects. Tries to
 * Created by Dark on 9/11/2015.
 */
public abstract class AbstractTileTest<T extends Tile> extends AbstractTileEntityTest<T, BlockTile>
{
    //These are only used for testing player interaction, if you only need a world create a new FakeWorld
    /** MC server instance for the entire class file to use */
    protected MinecraftServer server;
    /** World server to build tests inside, make sure to clean up as its used over all tests in this class */
    protected FakeWorldServer world;
    /** Tester that has not choice in what to test, test between tests but not deleted. Make sure to cleanup non-vanilla data between tests */
    protected TestPlayer player;
    /** Places a block at zero zero zero automatically of meta data value 0 */
    protected boolean autoPlaceBlock = false;

    public AbstractTileTest(String name, Class<T> clazz) throws IllegalAccessException, InstantiationException
    {
        name = LanguageUtility.capitalizeFirst(name);
        this.tileClazz = clazz;
        Tile tile = clazz.newInstance();
        BlockTile block = new BlockTile(tile, "Block" + name, CreativeTabs.tabAllSearch);
        tile.setBlock(block);
        this.block = ModRegistry.registerBlock(block, "Block" + name);
        TileEntity.addMapping(tileClazz, "Tile" + name);
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
        world.setBlock(0, 0, 0, block);
    }

    @Override
    public void tearDownForTest(String name)
    {
        super.tearDownForTest(name);
        world.setBlockToAir(0, 0, 0);
        world.tick();
        assertTrue("Block at [0,0,0] should be air after test is finished", world.getBlock(0, 0, 0) == Blocks.air);
    }

    @Test
    public void testCoverage()
    {
        Method[] methods = Tile.class.getMethods();
        assertTrue("There are " + methods.length + " but should be 139", methods.length == 139);
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
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.setBlockBoundsBasedOnState();
    }

    @Test
    public void testSetTextureName()
    {
        FakeWorld world = FakeWorld.newWorld("TestSetTextureName");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.setTextureName("texture");
    }

    @Test
    public void testGetBlockMetadata()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetBlockMetadata");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getBlockMetadata();
    }

    @Test
    public void testCanSilkHarvest()
    {
        FakeWorld world = FakeWorld.newWorld("TestCanSilkHarvest");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.canSilkHarvest(player, 0);
    }

    @Test
    public void testShouldSideBeRendered()
    {
        FakeWorld world = FakeWorld.newWorld("TestShouldSideBeRendered");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        for (int i = 0; i < 6; i++)
        {
            tile.shouldSideBeRendered(i);
        }
    }

    @Test
    public void testGetCollisionBounds()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetCollisionBounds");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        Cube cube = tile.getCollisionBounds();
        if (cube != null)
        {
            //TODO test too see if cube is valid
        }
    }

    @Test
    public void testGetWeakRedstonepower()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetWeakRedstonepower");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        for (int i = 0; i < 6; i++)
        {
            tile.getWeakRedstonePower(i);
        }
    }

    @Test
    public void testGetRenderBlockpass()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetRenderBlockpass");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getRenderBlockPass();
    }

    @Test
    public void testGetRenderColor()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetRenderColor");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getRenderColor(0);
    }

    @Test
    public void testDetermineOrientation()
    {
        FakeWorld world = FakeWorld.newWorld("TestDetermineOrientation");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.determineOrientation(player);
        //TODO maybe check to ensure direction is valid
    }

    @Test
    public void testGetplayersUsing()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetplayersUsing");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getPlayersUsing();
    }

    @Test
    public void testRemoveByplayer()
    {
        FakeWorld world = FakeWorld.newWorld("TestRemoveByplayer");

        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.removeByPlayer(player, true);

        world.setBlock(0, 0, 0, block);
        tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.removeByPlayer(player, false);
    }

    @Test
    public void testNotifyBlocksOfNeighborChange()
    {
        FakeWorld world = FakeWorld.newWorld("TestNotifyBlocksOfNeighborChange");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.notifyBlocksOfNeighborChange();
    }

    @Test
    public void testGetExplosionResistance()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetExplosionResistance");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getExplosionResistance(player);

        world.setBlock(0, 0, 0, block);
        tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getExplosionResistance(player, new Pos(2));
    }

    @Test
    public void testSendDescpacket()
    {
        FakeWorld world = FakeWorld.newWorld("TestSendDescpacket");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        if (tile.getDescPacket() != null)
        {
            //tile.sendDescPacket();
        }
    }

    @Test
    public void testSendpacketToServer()
    {
        FakeWorld world = FakeWorld.newWorld("TestSendpacketToServer");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.sendPacketToServer(new PacketTile(tile));
    }

    @Test
    public void testGetColorMultiplier()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetColorMultiplier");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getColorMultiplier();
    }

    @Test
    public void testGetCollisionBoxes()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetCollisionBoxes");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        Iterable<Cube> cubes = tile.getCollisionBoxes(new Cube(-2, -2, -2, 2, 2, 2), player);
        assertTrue(cubes != null);
        for (Cube cube : cubes)
        {
            assertTrue(cube != null);
            //TODO check if the cube is valid
        }
    }

    @Test
    public void testSendPacketToGuiUsers()
    {
        FakeWorld world = FakeWorld.newWorld("TestSendpacketToGuiUsers");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.sendPacketToGuiUsers(new PacketTile(tile));
    }

    @Test
    public void testDetermineForgeDirection()
    {
        FakeWorld world = FakeWorld.newWorld("TestDetermineForgeDirection");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.determineForgeDirection(player);
    }

    @Test
    public void testGetStrongRedstonePower()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetStrongRedstonepower");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        for (int i = 0; i < 6; i++)
        {
            tile.getStrongRedstonePower(i);
        }
    }

    @Test
    public void testCanPlaceBlockAt()
    {
        FakeWorld world = FakeWorld.newWorld("TestCanplaceBlockAt");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.canPlaceBlockAt();
    }

    @Test
    public void testCanPlaceBlockOnSide()
    {
        FakeWorld world = FakeWorld.newWorld("TestCanplaceBlockOnSide");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            tile.canPlaceBlockOnSide(dir);
        }
    }

    @Test
    public void testRandomDisplayTick()
    {
        FakeWorld world = FakeWorld.newWorld("TestRandomDisplayTick");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.randomDisplayTick();
    }

    @Test
    public void testRegisterSideTextureSet()
    {
        FakeWorld world = FakeWorld.newWorld("TestRegisterSideTextureSet");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        SudoIconReg reg = new SudoIconReg();
        //TODO check to see if the texture exists
        tile.registerSideTextureSet(reg);
    }

    @Test
    public void testGetOwnerProfile()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetOwnerprofile");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getOwnerProfile();
    }

    @Test
    public void testGetSpecialRenderer()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetSpecialRenderer");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getSpecialRenderer();
    }

    @Test
    public void testIsIndirectlypowered()
    {
        FakeWorld world = FakeWorld.newWorld("TestIsIndirectlypowered");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.isIndirectlyPowered();
    }

    @Test
    public void testHasSpecialRenderer()
    {
        FakeWorld world = FakeWorld.newWorld("TestHasSpecialRenderer");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.hasSpecialRenderer();
    }

    @Test
    public void testNewTile()
    {
        FakeWorld world = FakeWorld.newWorld("TestNewTile");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.newTile();
    }

    @Test
    public void testGetSubBlocks()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetSubBlocks");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        List<ItemStack> list = new ArrayList();
        tile.getSubBlocks(Item.getItemFromBlock(block), block.getCreativeTabToDisplayOn(), list);
        for (ItemStack stack : list)
        {
            assertTrue(stack != null);
            assertTrue(stack.getItem() != null);
        }
    }

    @Test
    public void testToPos()
    {
        FakeWorld world = FakeWorld.newWorld("TestTopos");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        Pos pos = tile.toPos();
        Pos pos2 = new Pos(tile.x(), tile.y(), tile.z());
        assertTrue("Pos " + pos + " does not equal " + pos2, pos.equals(pos2));
    }

    @Test
    public void testGetDrops()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetDrops");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        List<ItemStack> items = tile.getDrops(0, 0);
        for (ItemStack stack : items)
        {
            assertTrue(stack != null);
            assertTrue(stack.getItem() != null);
            //TODO check for creative mod only items
        }
    }

    @Test
    public void testOnWorldJoin()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnWorldJoin");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.onWorldJoin();
    }

    @Test
    public void testOnPlaced()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnplaced");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.onPlaced(player, new ItemStack(block));
    }

    @Test
    public void testOnPostplaced()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnpostplaced");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.onPostPlaced(0);
    }

    @Test
    public void testOnRemove()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnRemove");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.onRemove(block, 0);
    }

    @Test
    public void testGetTileBlock()
    {
        assertTrue(block.staticTile.getTileBlock() == block);
    }

    @Test
    public void testOpenGui()
    {
        FakeWorld world = FakeWorld.newWorld("TestOpenGui");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.openGui(player, 0, Engine.instance);
    }

    @Test
    public void testOnAdded()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnAdded");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.onAdded();
    }

    @Test
    public void testToItemStack()
    {
        FakeWorld world = FakeWorld.newWorld("TestToItemStack");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        ItemStack stack = tile.toItemStack();
    }

    @Test
    public void testOnCollide()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnCollide");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.onCollide(player);
    }

    @Test
    public void testGetAccess()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetAccess");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        assertTrue(tile.getAccess() == world);
    }

    @Test
    public void testSetAccess()
    {
        FakeWorld world = FakeWorld.newWorld("TestSetAccess");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.setAccess(world);
    }

    @Test
    public void testGetpickBlock()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetpickBlock");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            for (int i = 0; i < 256; i++)
            {
                tile.getPickBlock(new MovingObjectPosition(0, 0, 0, dir.ordinal(), getNextClick(dir, i).toVec3(), true));
            }
        }
    }

    @Test
    public void testToLocation()
    {
        FakeWorld world = FakeWorld.newWorld("TestToLocation");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        Location loc = tile.toLocation();
        assertTrue(loc.world == world);
        assertTrue(loc.xi() == tile.xCoord);
        assertTrue(loc.yi() == tile.yCoord);
        assertTrue(loc.zi() == tile.zCoord);
    }

    @Test
    public void testOnRegistered()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnRegistered");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.onRegistered();

    }

    @Test
    public void testBlockUpdate()
    {
        FakeWorld world = FakeWorld.newWorld("TestBlockUpdate");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.blockUpdate();
    }

    @Test
    public void testFirstTick()
    {
        FakeWorld world = FakeWorld.newWorld("TestFirstTick");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.firstTick();
    }

    @Test
    public void testGetBlockColor()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetBlockColor");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getBlockColor();
    }

    @Test
    public void testGetSideIcon()
    {

        FakeWorld world = FakeWorld.newWorld("TestGetSideIcon");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.registerIcons(new SudoIconReg());
        try
        {
            Method method = Tile.class.getDeclaredMethod("getSideIcon", Integer.TYPE, Integer.TYPE);
            method.setAccessible(true);
            for (int i = 0; i < 6; i++)
            {
                method.invoke(tile, 0, i);
            }
        } catch (NoSuchMethodException e)
        {
            fail("Could not find method getTextureName");
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
            fail("Failed to invoke method getTextureName");
        } catch (IllegalAccessException e)
        {
            fail("Couldn't access method getTextureName");
        }
    }

    @Test
    public void testGetOwnerID()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetOwnerID");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getOwnerID();
    }

    @Test
    public void testDistance()
    {
        FakeWorld world = FakeWorld.newWorld("TestDistance");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            Pos pos = new Pos(0.5).add(dir);
            Pos center = tile.toPos();
            double distance = tile.distance(pos.x(), pos.y(), pos.z());
            assertTrue("Distance = " + distance + "  Pos = " + pos + "  Center = " + center, Math.abs(distance - 1) <= 0.01);

            player.setLocationAndAngles(pos.x(), pos.y(), pos.z(), 0, 0);
            distance = tile.distance(player);
            assertTrue("Distance = " + distance + "  Pos = " + pos + "  Center = " + center, Math.abs(distance - 1) <= 0.01);

            distance = tile.distance(pos);
            assertTrue("Distance = " + distance + "  Pos = " + pos + "  Center = " + center, Math.abs(distance - 1) <= 0.01);
        }
    }

    @Test
    public void testScheduleTick()
    {
        FakeWorld world = FakeWorld.newWorld("TestScheduleTick");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        try
        {
            Method method = Tile.class.getDeclaredMethod("scheduleTick", Integer.TYPE);
            method.setAccessible(true);
            method.invoke(tile, 0);
        } catch (NoSuchMethodException e)
        {
            fail("Could not find method scheduleTick");
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
            fail("Failed to invoke method scheduleTick");
        } catch (IllegalAccessException e)
        {
            fail("Couldn't access method scheduleTick");
        }
    }

    @Test
    public void testGetSaveData()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetSaveData");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        NBTTagCompound tag = tile.getSaveData();
        assertTrue(tag != null && !tag.hasNoTags());
    }

    @Test
    public void testIsClient()
    {
        FakeWorld world = FakeWorld.newWorld("TestIsClient");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.isClient();
    }

    @Test
    public void testSetOwner()
    {
        FakeWorld world = FakeWorld.newWorld("TestSetOwner");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.setOwner(player);
    }

    @Test
    public void testSetMeta()
    {
        FakeWorld world = FakeWorld.newWorld("TestSetMeta");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.setMeta(10);
    }

    @Test
    public void testRegisterIcons()
    {
        FakeWorld world = FakeWorld.newWorld("TestRegisterIcons");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.registerIcons(new SudoIconReg());
    }

    @Test
    public void testSetOwnerID()
    {
        FakeWorld world = FakeWorld.newWorld("TestSetOwnerID");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.setOwnerID(player.getGameProfile().getId());
    }

    @Test
    public void testGetDescpacket()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetDescpacket");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getDescPacket();
    }

    @Test
    public void testUpdateLight()
    {
        FakeWorld world = FakeWorld.newWorld("TestUpdateLight");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        try
        {
            Method method = Tile.class.getDeclaredMethod("updateLight");
            method.setAccessible(true);
            method.invoke(tile);
        } catch (NoSuchMethodException e)
        {
            fail("Could not find method updateLight");
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
            fail("Failed to invoke method updateLight");
        } catch (IllegalAccessException e)
        {
            fail("Couldn't access method updateLight");
        }
    }

    @Test
    public void testGetIcon()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetIcon");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.registerIcons(new SudoIconReg());
        tile.getIcon();
        for (int i = 0; i < 6; i++)
        {
            tile.getIcon(i);
            tile.getIcon(i, 0);
        }
        tile.getIcon(tile.name);
    }

    @Test
    public void testOnFillRain()
    {
        FakeWorld world = FakeWorld.newWorld("TestOnFillRain");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.onFillRain();
    }

    @Test
    public void testGetOwnerName()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetOwnerName");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getOwnerName();
    }

    @Test
    public void testMarkUpdate()
    {
        FakeWorld world = FakeWorld.newWorld("TestMarkUpdate");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        try
        {
            Method method = Tile.class.getDeclaredMethod("markUpdate");
            method.setAccessible(true);
            method.invoke(tile);
        } catch (NoSuchMethodException e)
        {
            fail("Could not find method markUpdate");
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
            fail("Failed to invoke method markUpdate");
        } catch (IllegalAccessException e)
        {
            fail("Couldn't access method markUpdate");
        }
    }

    @Test
    public void testMarkRender()
    {
        FakeWorld world = FakeWorld.newWorld("TestMarkRender");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        try
        {
            Method method = Tile.class.getDeclaredMethod("markRender");
            method.setAccessible(true);
            method.invoke(tile);
        } catch (NoSuchMethodException e)
        {
            fail("Could not find method markRender");
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
            fail("Failed to invoke method markRender");
        } catch (IllegalAccessException e)
        {
            fail("Couldn't access method markRender");
        }
    }

    @Test
    public void testIsServer()
    {
        FakeWorld world = FakeWorld.newWorld("TestIsServer");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.isServer();
    }

    @Test
    public void testGetLightValue()
    {
        FakeWorld world = FakeWorld.newWorld("TestGetLightValue");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.getLightValue();
    }

    @Test
    public void testIsSolid()
    {
        FakeWorld world = FakeWorld.newWorld("TestIsSolid");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        for (int i = 0; i < 6; i++)
        {
            tile.isSolid(i);
        }
    }

    @Test
    public void testTickRate()
    {
        FakeWorld world = FakeWorld.newWorld("TestTickRate");
        world.setBlock(0, 0, 0, block);
        Tile tile = ((Tile) world.getTileEntity(0, 0, 0));
        tile.tickRate();
    }
}
