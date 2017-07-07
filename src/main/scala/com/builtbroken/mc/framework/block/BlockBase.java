package com.builtbroken.mc.framework.block;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.api.tile.access.IGuiTile;
import com.builtbroken.mc.api.tile.listeners.*;
import com.builtbroken.mc.api.tile.listeners.client.IIconListener;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.helper.WrenchUtility;
import com.builtbroken.mc.lib.json.IJsonGenMod;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import com.builtbroken.mc.prefab.tile.listeners.ListenerIterator;
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
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
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
        this.data.block = this;
        this.setBlockName(data.localization.replace("${name}", data.name).replace("${mod}", data.getMod()));
        this.setResistance(data.getResistance());
        this.setHardness(data.getHardness());

        setBlockBounds(data.getBlockBounds().min().xf(), data.getBlockBounds().min().yf(), data.getBlockBounds().min().zf(), data.getBlockBounds().max().xf(), data.getBlockBounds().max().yf(), data.getBlockBounds().max().zf());

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
            manager.newBlock(data.registryKey, this, getItemBlockClass());
            if (data.tileEntityProvider != null)
            {
                data.tileEntityProvider.register(this, mod, manager);
            }
        }
    }

    /**
     * Gets the item block class to use with this block,
     * only used during registration.
     *
     * @return
     */
    protected Class<? extends ItemBlock> getItemBlockClass()
    {
        return ItemBlockBase.class;
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
        //TODO add creation listener to inject listeners for tiles
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
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "rain");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IFillRainListener)
            {
                ((IFillRainListener) next).onFilledWithRain();
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
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "resistance");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IResistanceListener)
            {
                ((IResistanceListener) next).getExplosionResistance(entity, explosionX, explosionY, explosionZ);
            }
        }
        return getExplosionResistance(entity);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "placement");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IPlacementListener)
            {
                ((IPlacementListener) next).onAdded();
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "placement");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IPlacementListener)
            {
                ((IPlacementListener) next).onPlacedBy(entityLiving, itemStack);
            }
        }
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int metadata)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "placement");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IPlacementListener)
            {
                ((IPlacementListener) next).onPostPlaced(metadata);
            }
        }
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion ex)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "break");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IBlockListener)
            {
                ((IDestroyedListener) next).onDestroyedByExplosion(ex);
            }
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "break");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IBlockListener)
            {
                ((IDestroyedListener) next).breakBlock(block, par6);
            }
        }
        super.breakBlock(world, x, y, z, block, par6);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "break");
        boolean removed = false;
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IBlockListener)
            {
                if (((IDestroyedListener) next).removedByPlayer(player, willHarvest))
                {
                    removed = true;
                }
            }
        }
        return super.removedByPlayer(world, player, x, y, z, willHarvest) || removed;
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
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "change");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IChangeListener)
            {
                ((IChangeListener) next).onNeighborBlockChange(block);
            }
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "change");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IChangeListener)
            {
                ((IChangeListener) next).onNeighborChange(tileX, tileY, tileZ);
            }
        }
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "placement");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IPlacementListener)
            {
                if (!((IPlacementListener) next).canPlaceOnSide(side))
                {
                    return false;
                }
            }
        }
        return canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "placement");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IPlacementListener)
            {
                if (!((IPlacementListener) next).canPlaceAt())
                {
                    return false;
                }
            }
        }
        return super.canPlaceBlockAt(world, x, y, z);
    }

    public boolean canPlaceBlockAt(Entity entity, World world, int x, int y, int z)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "placement");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IPlacementListener)
            {
                if (!((IPlacementListener) next).canPlaceAt(entity))
                {
                    return false;
                }
            }
        }
        return canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public boolean canReplace(World world, int x, int y, int z, int side, ItemStack stack)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "placement");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IPlacementListener)
            {
                if (!((IPlacementListener) next).canReplace(world, x, y, z, side, stack))
                {
                    return false;
                }
            }
        }
        return this.canPlaceBlockOnSide(world, x, y, z, side);
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "activation");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IActivationListener)
            {
                ((IActivationListener) next).onPlayerClicked(player);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        try
        {
            boolean activated = false;

            Object tile = getTile(world, x, y, z);
            if (WrenchUtility.isUsableWrench(player, player.inventory.getCurrentItem(), x, y, z))
            {
                ListenerIterator it = new ListenerIterator(world, x, y, z, this, "wrench");
                while (it.hasNext())
                {
                    ITileEventListener next = it.next();
                    if (next instanceof IWrenchListener && ((IWrenchListener) next).handlesWrenchRightClick() && ((IWrenchListener) next).onPlayerRightClickWrench(player, side, hitX, hitY, hitZ))
                    {
                        activated = true;
                    }
                }
                if (activated)
                {
                    WrenchUtility.damageWrench(player, player.inventory.getCurrentItem(), x, y, z);
                }
                if (activated)
                {
                    return true;
                }
            }

            //TODO move to listener to prevent usage of IGuiTile in special cases
            if (tile instanceof IGuiTile && ((IGuiTile) tile).shouldOpenOnRightClick(player))
            {
                int id = ((IGuiTile) tile).getDefaultGuiID(player);
                if (id >= 0)
                {
                    Object o = ((IGuiTile) tile).getServerGuiElement(id, player);
                    if (o != null)
                    {
                        player.openGui(mod, id, world, x, y, z);
                        return true;
                    }
                }
            }

            ListenerIterator it = new ListenerIterator(world, x, y, z, this, "activation");
            while (it.hasNext())
            {
                ITileEventListener next = it.next();
                if (next instanceof IActivationListener)
                {
                    if (((IActivationListener) next).onPlayerActivated(player, side, hitX, hitY, hitZ))
                    {
                        activated = true;
                    }
                }
            }
            return activated;
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
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "update");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IUpdateListener)
            {
                ((IUpdateListener) next).updateTick(par5Random);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random par5Random)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "update");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IUpdateListener)
            {
                ((IUpdateListener) next).randomDisplayTick(par5Random);
            }
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "update");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof ICollisionListener)
            {
                ((ICollisionListener) next).onEntityCollidedWithBlock(entity);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
    {
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);

        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "bounds");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IBoundListener)
            {
                List collect = new ArrayList();
                ((IBoundListener) next).addCollisionBoxesToList(aabb, collect, entity);
                for (Object object : list)
                {
                    if (object instanceof AxisAlignedBB && aabb.intersectsWith((AxisAlignedBB) object))
                    {
                        list.add(object);
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "bounds");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IBoundListener)
            {
                AxisAlignedBB bound = ((IBoundListener) next).getSelectedBounds();
                if (bound != null)
                {
                    return bound; //TODO change to largest box wins
                }
            }
        }
        return data.getSelectionBounds().clone().add(x, y, z).toAABB();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "bounds");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IBoundListener)
            {
                AxisAlignedBB bound = ((IBoundListener) next).getCollisionBounds();
                if (bound != null)
                {
                    return bound; //TODO change to largest box wins
                }
            }
        }
        return data.getBlockBounds().clone().add(x, y, z).toAABB();
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
        return data != null ? data.hasComparatorInputOverride() : false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return data != null ? data.isOpaqueCube() : false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return data != null ? data.renderAsNormalBlock() : true;
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

        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "icon");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IIconListener)
            {
                IIcon icon = ((IIconListener) next).getTileIcon(side, meta);
                if (icon != null)
                {
                    return icon;
                }
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
        RenderData data = getRenderData(meta);
        if (data != null)
        {
            for (String key : new String[]{
                    "block." + meta,
                    "block." + ForgeDirection.getOrientation(meta).name().toLowerCase(),
                    "tile." + meta,
                    "tile." + ForgeDirection.getOrientation(meta).name().toLowerCase(),
                    "block",
                    "tile"})
            {
                IRenderState state = data.getState(key);
                if (state != null)
                {
                    IIcon icon = state.getIcon(side);
                    if(icon != null)
                    {
                        return icon;
                    }
                }
            }
        }
        return Blocks.wool.getIcon(0, side);
    }

    public RenderData getRenderData(int meta)
    {
        return ClientDataHandler.INSTANCE.getRenderData(getContentID(meta));
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
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "blockStack");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IBlockStackListener)
            {
                ItemStack stack = ((IBlockStackListener) next).getPickBlock(target, player);
                if (stack != null && stack.getItem() != null)
                {
                    return stack;
                }
            }
        }
        return super.getPickBlock(target, world, x, y, z, player);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> items = super.getDrops(world, x, y, z, metadata, fortune);

        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "blockStack");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IBlockStackListener)
            {
                ((IBlockStackListener) next).collectDrops(items, metadata, fortune);
            }
        }
        return items;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
        super.getSubBlocks(item, creativeTabs, list);
        if (listeners.containsKey("blockStack"))
        {
            for (ITileEventListener listener : listeners.get("blockStack"))
            {
                if (listener instanceof IBlockStackListener)
                {
                    ((IBlockStackListener) listener).getSubBlocks(item, creativeTabs, list);
                }
            }
        }
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
        ListenerIterator it = new ListenerIterator(access, x, y, z, this, "bounds");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IBoundListener)
            {
                ((IBoundListener) next).setBlockBoundsBasedOnState();
            }
        }
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        if (listeners.containsKey("bounds"))
        {
            for (ITileEventListener listener : listeners.get("bounds"))
            {
                if (listener instanceof IBoundListener)
                {
                    ((IBoundListener) listener).setBlockBoundsForItemRender();
                }
            }
        }
    }

    @Override
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack itemStack)
    {
        ListenerIterator it = new ListenerIterator(world, x, y, z, this, "blockStack");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IBlockStackListener)
            {
                ((IBlockStackListener) next).dropBlockAsItem(itemStack);
            }
        }
        if (itemStack != null && itemStack.getItem() != null)
        {
            InventoryUtility.dropItemStack(world, x, y, z, itemStack, 0, 0);
        }
    }

    @Override
    public int getRenderBlockPass()
    {
        return data != null && data.isAlpha() ? 1 : 0;
    }

    @Override
    public int tickRate(World world)
    {
        int tickRate = super.tickRate(world);
        for (ITileEventListener next : listeners.get("update"))
        {
            if (next instanceof IUpdateListener)
            {
                int r = ((IUpdateListener) next).tickRate(world);
                if (r > 0 && r < tickRate)
                {
                    tickRate = r;
                }
            }
        }
        return tickRate;

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
