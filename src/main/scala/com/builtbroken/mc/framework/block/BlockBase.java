package com.builtbroken.mc.framework.block;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.api.tile.access.IGuiTile;
import com.builtbroken.mc.api.tile.listeners.*;
import com.builtbroken.mc.api.tile.listeners.client.IIconListener;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import com.builtbroken.mc.framework.logic.imp.ITileNodeHost;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.helper.WrenchUtility;
import com.builtbroken.mc.lib.json.IJsonGenMod;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.prefab.items.ItemBlockAbstract;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Block generated through a json based file format... Used to reduce dependency on code
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class BlockBase extends BlockContainer implements IRegistryInit, IJsonGenObject, ITileEntityProvider
{
    /** Data about the block */
    public final BlockPropertyData data;
    /** Mod that claimed this block */
    public IJsonGenMod mod;

    /** Has the block been registered */
    protected boolean registered = false;

    //Listeners
    public final HashMap<String, List<ITileEventListener>> listeners = new HashMap();

    public BlockBase(BlockPropertyData data)
    {
        super(data.getMaterial());
        this.data = data;
        this.setBlockName(data.localization.replace("${name}", data.name));
        this.setResistance(data.getResistance());
        this.setHardness(data.getHardness());

        //Run later, as the default is set without data working
        this.opaque = this.isOpaqueCube();
        this.lightOpacity = this.isOpaqueCube() ? 255 : 0;
    }

    @Override
    public String getLoader()
    {
        return "block";
    }

    @Override
    public String getMod()
    {
        return data != null ? data.getMod() : null;
    }

    @Override
    public String getContentID()
    {
        return data.registryKey;
    }

    @Override
    public void register(IJsonGenMod mod, ModManager manager)
    {
        if (!registered)
        {
            this.mod = mod;
            registered = true;
            manager.newBlock(data.registryKey, this, ItemBlockAbstract.class);
            if (data.tileEntityProvider != null)
            {
                data.tileEntityProvider.register(this, mod, manager);
            }
        }
    }

    @Override
    public void onRegistered()
    {
        if (data.oreName != null)
        {
            OreDictionary.registerOre(data.oreName, new ItemStack(this));
        }
    }

    @Override
    public void onClientRegistered()
    {

    }

    @Override
    public String toString()
    {
        return "Block[" + data.name + "]";
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        if (data.tileEntityProvider != null)
        {
            return data.tileEntityProvider.createNewTileEntity(this, world, meta);
        }
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta)
    {
        return createNewTileEntity(world, meta);
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z)
    {
        //TODO implement
        return data.getHardness();
    }

    @Override
    public void fillWithRain(World world, int x, int y, int z)
    {
        if (listeners.containsKey("rain"))
        {
            for (ITileEventListener listener : listeners.get("rain"))
            {
                if (listener instanceof IFillRainListener)
                {
                    if (listener instanceof IBlockListener)
                    {
                        ((IBlockListener) listener).inject(world, x, y, z);
                        ((IFillRainListener) listener).onFilledWithRain();
                        ((IBlockListener) listener).eject();
                    }
                    else
                    {
                        ((IFillRainListener) listener).onFilledWithRain();
                    }
                }
            }
        }
    }

    @Override
    public float getExplosionResistance(Entity entity)
    {
        if (listeners.containsKey("resistance"))
        {
            float re = -1;
            for (ITileEventListener listener : listeners.get("resistance"))
            {
                if (listener instanceof IResistanceListener)
                {
                    float value = ((IResistanceListener) listener).getExplosionResistance(entity);
                    if (value >= 0 && (value < re || re < 0))
                    {
                        re = value;
                    }
                }
            }
            if (re >= 0)
            {
                return re;
            }
        }
        return data.getResistance() / 5.0F;
    }

    @Override
    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        if (listeners.containsKey("resistance"))
        {
            float re = -1;
            for (ITileEventListener listener : listeners.get("resistance"))
            {
                if (listener instanceof IResistanceListener)
                {
                    float value;
                    if (listener instanceof IBlockListener)
                    {
                        ((IBlockListener) listener).inject(world, x, y, z);
                        value = ((IResistanceListener) listener).getExplosionResistance(entity, explosionX, explosionY, explosionZ);
                        ((IBlockListener) listener).eject();
                    }
                    else
                    {
                        value = ((IResistanceListener) listener).getExplosionResistance(entity, explosionX, explosionY, explosionZ);
                    }

                    if (value >= 0 && (value < re || re < 0))
                    {
                        re = value;
                    }
                }
            }
            if (re >= 0)
            {
                return re;
            }
        }
        return getExplosionResistance(entity);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        if (listeners.containsKey("placement"))
        {
            for (ITileEventListener listener : listeners.get("placement"))
            {
                if (listener instanceof IPlacementListener)
                {
                    if (listener instanceof IBlockListener)
                    {
                        ((IBlockListener) listener).inject(world, x, y, z);
                        ((IPlacementListener) listener).onAdded();
                        ((IBlockListener) listener).eject();
                    }
                    else
                    {
                        ((IPlacementListener) listener).onAdded();
                    }
                }
            }
        }
        //Check tile
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IPlacementListener)
        {
            ((IPlacementListener) tile).onAdded();
        }
        //Do calls for tile listeners
        if (tile instanceof ITileWithListeners)
        {
            for (ITileEventListener listener : ((ITileWithListeners) tile).getListeners("placement"))
            {
                if (listener instanceof IPlacementListener)
                {
                    ((IPlacementListener) listener).onAdded();
                }
            }
        }
        //Check node
        if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() instanceof IDestroyedListener)
        {
            ((IPlacementListener) ((ITileNodeHost) tile).getTileNode()).onAdded();
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        if (listeners.containsKey("placement"))
        {
            for (ITileEventListener listener : listeners.get("placement"))
            {
                if (listener instanceof IPlacementListener)
                {
                    if (listener instanceof IBlockListener)
                    {
                        ((IBlockListener) listener).inject(world, x, y, z);
                        ((IPlacementListener) listener).onPlacedBy(entityLiving, itemStack);
                        ((IBlockListener) listener).eject();
                    }
                    else
                    {
                        ((IPlacementListener) listener).onPlacedBy(entityLiving, itemStack);
                    }
                }
            }
        }
        //Check tile
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IPlacementListener)
        {
            ((IPlacementListener) tile).onPlacedBy(entityLiving, itemStack);
        }
        //Do calls for tile listeners
        if (tile instanceof ITileWithListeners)
        {
            for (ITileEventListener listener : ((ITileWithListeners) tile).getListeners("placement"))
            {
                if (listener instanceof IPlacementListener)
                {
                    ((IPlacementListener) listener).onPlacedBy(entityLiving, itemStack);
                }
            }
        }
        //Check node
        if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() instanceof IDestroyedListener)
        {
            ((IPlacementListener) ((ITileNodeHost) tile).getTileNode()).onPlacedBy(entityLiving, itemStack);
        }
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int metadata)
    {
        if (listeners.containsKey("placement"))
        {
            for (ITileEventListener listener : listeners.get("placement"))
            {
                if (listener instanceof IPlacementListener)
                {
                    if (listener instanceof IBlockListener)
                    {
                        ((IBlockListener) listener).inject(world, x, y, z);
                        ((IPlacementListener) listener).onPostPlaced(metadata);
                        ((IBlockListener) listener).eject();
                    }
                    else
                    {
                        ((IPlacementListener) listener).onPostPlaced(metadata);
                    }
                }
            }
        }
        //Check tile
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IPlacementListener)
        {
            ((IPlacementListener) tile).onPostPlaced(metadata);
        }
        //Do calls for tile listeners
        if (tile instanceof ITileWithListeners)
        {
            for (ITileEventListener listener : ((ITileWithListeners) tile).getListeners("placement"))
            {
                if (listener instanceof IPlacementListener)
                {
                    ((IPlacementListener) listener).onPostPlaced(metadata);
                }
            }
        }
        //Check node
        if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() instanceof IDestroyedListener)
        {
            ((IPlacementListener) ((ITileNodeHost) tile).getTileNode()).onPostPlaced(metadata);
        }
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion ex)
    {
        if (listeners.containsKey("break"))
        {
            for (ITileEventListener listener : listeners.get("break"))
            {
                if (listener instanceof IDestroyedListener)
                {
                    if (listener instanceof IBlockListener)
                    {
                        ((IBlockListener) listener).inject(world, x, y, z);
                        ((IDestroyedListener) listener).onDestroyedByExplosion(ex);
                        ((IBlockListener) listener).eject();
                    }
                    else
                    {
                        ((IDestroyedListener) listener).onDestroyedByExplosion(ex);
                    }
                }
            }
        }
        //Check tile
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IDestroyedListener)
        {
            ((IDestroyedListener) tile).onDestroyedByExplosion(ex);
        }
        //Do calls for tile listeners
        if (tile instanceof ITileWithListeners)
        {
            for (ITileEventListener listener : ((ITileWithListeners) tile).getListeners("break"))
            {
                if (listener instanceof IDestroyedListener)
                {
                    ((IDestroyedListener) listener).onDestroyedByExplosion(ex);
                }
            }
        }
        //Check node
        if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() instanceof IDestroyedListener)
        {
            ((IDestroyedListener) ((ITileNodeHost) tile).getTileNode()).onDestroyedByExplosion(ex);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6)
    {
        if (listeners.containsKey("break"))
        {
            for (ITileEventListener listener : listeners.get("break"))
            {
                if (listener instanceof IDestroyedListener)
                {
                    if (listener instanceof IBlockListener)
                    {
                        ((IBlockListener) listener).inject(world, x, y, z);
                        ((IDestroyedListener) listener).breakBlock(block, par6);
                        ((IBlockListener) listener).eject();
                    }
                    else
                    {
                        ((IDestroyedListener) listener).breakBlock(block, par6);
                    }
                }
            }
        }
        //Check tile
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IDestroyedListener)
        {
            ((IDestroyedListener) tile).breakBlock(block, par6);
        }
        //Do calls for tile listeners
        if (tile instanceof ITileWithListeners)
        {
            for (ITileEventListener listener : ((ITileWithListeners) tile).getListeners("break"))
            {
                if (listener instanceof IDestroyedListener)
                {
                    ((IDestroyedListener) listener).breakBlock(block, par6);
                }
            }
        }
        //Check node
        if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() instanceof IDestroyedListener)
        {
            ((IDestroyedListener) ((ITileNodeHost) tile).getTileNode()).breakBlock(block, par6);
        }
        super.breakBlock(world, x, y, z, block, par6);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        if (listeners.containsKey("break"))
        {
            for (ITileEventListener listener : listeners.get("break"))
            {
                if (listener instanceof IDestroyedListener)
                {
                    if (listener instanceof IBlockListener)
                    {
                        ((IBlockListener) listener).inject(world, x, y, z);
                        ((IDestroyedListener) listener).removedByPlayer(player, willHarvest);
                        ((IBlockListener) listener).eject();
                    }
                    else
                    {
                        ((IDestroyedListener) listener).removedByPlayer(player, willHarvest);
                    }
                }
            }
        }
        //Check tile
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IDestroyedListener)
        {
            ((IDestroyedListener) tile).removedByPlayer(player, willHarvest);
        }
        //Do calls for tile listeners
        if (tile instanceof ITileWithListeners)
        {
            for (ITileEventListener listener : ((ITileWithListeners) tile).getListeners("break"))
            {
                if (listener instanceof IDestroyedListener)
                {
                    ((IDestroyedListener) listener).removedByPlayer(player, willHarvest);
                }
            }
        }
        //Check node
        if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() instanceof IDestroyedListener)
        {
            ((IDestroyedListener) ((ITileNodeHost) tile).getTileNode()).removedByPlayer(player, willHarvest);
        }
        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
        //TODO implement
        return 1;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        //TODO implement
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        //TODO implement
        return super.canPlaceBlockOnSide(world, x, y, z, side);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        //TODO implement
        return super.canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
        //TODO implement
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
        Object tile = getTile(world, x, y, z);
        if (tile instanceof IActivationListener)
        {
            ((IActivationListener) tile).onPlayerClicked(player);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        try
        {
            Object tile = getTile(world, x, y, z);
            if (WrenchUtility.isUsableWrench(player, player.inventory.getCurrentItem(), x, y, z)
                    && tile instanceof IWrenchListener && ((IWrenchListener) tile).handlesWrenchRightClick())
            {
                if (((IWrenchListener) tile).onPlayerRightClickWrench(player, side, hitX, hitY, hitZ))
                {
                    WrenchUtility.damageWrench(player, player.inventory.getCurrentItem(), x, y, z);
                    return true;
                }
                return false;
            }
            else if (tile instanceof IGuiTile && ((IGuiTile) tile).shouldOpenOnRightClick(player))
            {
                player.openGui(mod, ((IGuiTile) tile).getDefaultGuiID(player), world, x, y, z);
                return true;
            }
            else if (tile instanceof IActivationListener)
            {
                return ((IActivationListener) tile).onPlayerActivated(player, side, hitX, hitY, hitZ);
            }
            return false;
        }
        catch (Exception e)
        {
            outputError(world, x, y, z, "while right click block on side " + side, e);
            player.addChatComponentMessage(new ChatComponentText(Colors.RED.code + LanguageUtility.getLocal("blockTile.error.onBlockActivated")));
        }
        return false;
    }

    protected Object getTile(World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof ITileNodeHost)
        {
            return ((ITileNodeHost) tile).getTileNode();
        }
        return tile;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random par5Random)
    {
        //TODO implement
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random par5Random)
    {
        //TODO implement
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        //TODO implement
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
    {
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        //TODO implement
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        //TODO implement
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
    {
        //TODO implement
        return super.shouldSideBeRendered(access, x, y, z, side);
    }

    @Override
    public boolean isBlockSolid(IBlockAccess access, int x, int y, int z, int side)
    {
        //TODO implement
        return super.isBlockSolid(access, x, y, z, side);
    }

    @Override
    public int getLightValue(IBlockAccess access, int x, int y, int z)
    {
        //TODO implement listeners
        if (data != null && data.getLightValue() > 0)
        {
            return data.getLightValue();
        }
        return 0;
    }

    @Override
    public boolean hasComparatorInputOverride()
    {
        //TODO implement
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return data != null ? data.isOpaqueCube() : false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        //TODO implement
        return super.renderAsNormalBlock();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType()
    {
        return data != null ? data.getRenderType() : 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        final int meta = world.getBlockMetadata(x, y, z);
        if (listeners.containsKey("icon"))
        {
            for (ITileEventListener listener : listeners.get("icon"))
            {
                if (listener instanceof IIconListener && listener instanceof IBlockListener)
                {
                    ((IBlockListener) listener).inject(world, x, y, z);
                    IIcon icon = ((IIconListener) listener).getTileIcon(side, meta);
                    if (icon != null)
                    {
                        ((IBlockListener) listener).eject();
                        return icon;
                    }
                    ((IBlockListener) listener).eject();
                }
            }
        }
        //Check tile
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IIconListener)
        {
            ((IIconListener) tile).getTileIcon(side, meta);
        }
        //Do calls for tile listeners
        if (tile instanceof ITileWithListeners)
        {
            for (ITileEventListener listener : ((ITileWithListeners) tile).getListeners("break"))
            {
                if (listener instanceof IIconListener)
                {
                    IIcon icon = ((IIconListener) listener).getTileIcon(side, meta);
                    if (icon != null)
                    {
                        return icon;
                    }
                }
            }
        }
        //Check node
        if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() instanceof IIconListener)
        {
            IIcon icon = ((IIconListener) ((ITileNodeHost) tile).getTileNode()).getTileIcon(side, meta);
            if (icon != null)
            {
                return icon;
            }

        }
        return getIcon(side, meta);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta)
    {
        //Handle icon listeners
        if (listeners.containsKey("icon"))
        {
            for (ITileEventListener listener : listeners.get("icon"))
            {
                if (listener instanceof IIconListener && !(listener instanceof IBlockListener))
                {
                    IIcon icon = ((IIconListener) listener).getTileIcon(side, meta);
                    if (icon != null)
                    {
                        return icon;
                    }
                }
            }
        }
        return getIconFromJson(side, meta);
    }

    public IIcon getIconFromJson(int side, int meta)
    {
        //handle json data
        String contentID = getContentID(meta);
        RenderData data = ClientDataHandler.INSTANCE.getRenderData(contentID);
        if (data != null)
        {
            for (String key : new String[]{"block." + meta, "tile." + meta, "block", "tile"})
            {
                IRenderState state = data.getState(key);
                if (state != null && state.getIcon(side) != null)
                {
                    return state.getIcon(side);
                }
            }
        }
        return Blocks.wool.getIcon(0, side);
    }

    public String getContentID(int meta)
    {
        if (data == null)
        {
            return getClass().getName();
        }
        return data.getMod() + ":" + data.registryKey;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        //Texture registration is handled by ClientDataHandler
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
        //TODO implement
        return super.colorMultiplier(access, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBlockColor()
    {
        if (data != null && data.getColor() >= 0)
        {
            return data.getColor();
        }
        return super.getBlockColor();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderColor(int meta)
    {
        //TODO implement metadata values
        if (data != null && data.getColor() >= 0)
        {
            return data.getColor();
        }
        return getBlockColor();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        //TODO implement
        return super.getPickBlock(target, world, x, y, z, player);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        //TODO implement
        return super.getDrops(world, x, y, z, metadata, fortune);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
        super.getSubBlocks(item, creativeTabs, list);
        //TODO implement
    }

    /**
     * Redstone interaction
     */
    @Override
    public boolean canProvidePower()
    {
        return data != null && data.isSupportsRedstone();
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess access, int x, int y, int z, int side)
    {
        //TODO implement
        return 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess access, int x, int y, int z, int side)
    {
        //TODO implement
        return 0;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z)
    {
        //TODO implement
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        //TODO implement
    }

    @Override
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack itemStack)
    {
        super.dropBlockAsItem(world, x, y, z, itemStack);
        //TODO implement
    }

    @Override
    public int getRenderBlockPass()
    {
        return data != null && data.isAlpha() ? 1 : 0;
    }

    @Override
    public int tickRate(World world)
    {
        //TODO implement
        return super.tickRate(world);
    }

    /**
     * Outputs an error to console with location data
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param msg
     * @param e
     */
    protected void outputError(World world, int x, int y, int z, String msg, Throwable e)
    {
        String dim = "null";
        if (world != null && world.provider != null)
        {
            dim = "" + world.provider.dimensionId;
        }
        Engine.logger().error("Error: " + msg + " \nLocation[" + dim + "w " + x + "x " + y + "y " + z + "z" + "]", e);
    }

    /**
     * Called to add a listener to the block
     *
     * @param listener
     */
    public void addListener(ITileEventListener listener)
    {
        if (listener != null)
        {
            List<String> keys = listener.getListenerKeys();
            if (keys != null && !keys.isEmpty())
            {
                for (String key : keys)
                {
                    if (key != null)
                    {
                        List<ITileEventListener> listeners = this.listeners.get(key);
                        if (listeners == null)
                        {
                            listeners = new ArrayList();
                        }
                        listeners.add(listener);
                        this.listeners.put(key, listeners);
                    }
                }
            }
        }
    }
}
