package com.builtbroken.mc.seven.framework.block;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.api.data.ActionResponse;
import com.builtbroken.mc.api.tile.access.IGuiTile;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import com.builtbroken.mc.framework.block.imp.*;
import com.builtbroken.mc.framework.json.IJsonGenMod;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.helper.WrenchUtility;
import com.builtbroken.mc.seven.abstraction.MinecraftWrapper;
import com.builtbroken.mc.seven.framework.block.listeners.ListenerIterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
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
public class BlockBase extends BlockContainer implements IRegistryInit, IJsonGenObject<Block>, ITileEntityProvider
{
    public static final PropertyInteger META = PropertyInteger.create("meta", 0, 15);
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
        this.setUnlocalizedName(data.localization.replace("${name}", data.name).replace("${mod}", data.getMod()));
        this.setResistance(data.getResistance());
        this.setHardness(data.getHardness());

        //setBlockBounds(data.getBlockBounds().min().xf(), data.getBlockBounds().min().yf(), data.getBlockBounds().min().zf(), data.getBlockBounds().max().xf(), data.getBlockBounds().max().yf(), data.getBlockBounds().max().zf());

        //Run later, as the default is set without data working
        //this.opaque = this.isOpaqueCube();
        //this.lightOpacity = this.isOpaqueCube() ? 255 : 0;
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
    public void register(IJsonGenMod mod, RegistryEvent.Register<Block> manager)
    {
        if (!registered)
        {
            this.mod = mod;
            this.setRegistryName(mod.getDomain(), data.registryKey);
            registered = true;
            manager.getRegistry().register(this);
            if (data.tileEntityProvider != null)
            {
                data.tileEntityProvider.register(this, mod);
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
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        //TODO add creation listener to inject listeners for tiles
        return createNewTileEntity(world, 0);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
    {
        //TODO implement
        return data.getHardness();
    }

    @Override
    public void fillWithRain(World world, BlockPos pos)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "rain");
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
    public float getExplosionResistance(World world, BlockPos pos, Entity entity, Explosion ex)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "resistance");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IResistanceListener)
            {
                ((IResistanceListener) next).getExplosionResistance(entity, ex);
            }
        }
        return getExplosionResistance(entity);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "placement");
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
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "placement");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IPlacementListener)
            {
                ((IPlacementListener) next).onPlacedBy(placer, stack);
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "break");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IBlockListener)
            {
                ((IDestroyedListener) next).breakBlock(state);
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "break");
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
        return super.removedByPlayer(state, world, pos, player, willHarvest) || removed;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        //TODO implement
        return 1;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "change");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IChangeListener)
            {
                ((IChangeListener) next).onNeighborBlockChange(neighbor);
            }
        }
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "placement");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IPlacementListener)
            {
                if (((IPlacementListener) next).canPlaceOnSide(side) == ActionResponse.CANCEL)
                {
                    return false;
                }
            }
        }
        return canPlaceBlockAt(world, pos);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "placement");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IPlacementListener)
            {
                if (((IPlacementListener) next).canPlaceAt() == ActionResponse.CANCEL)
                {
                    return false;
                }
            }
        }
        return super.canPlaceBlockAt(world, pos);
    }

    public boolean canPlaceBlockAt(Entity entity, World world, BlockPos pos)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "placement");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IPlacementListener)
            {
                if (((IPlacementListener) next).canPlaceAt(MinecraftWrapper.INSTANCE.get(entity)) == ActionResponse.CANCEL)
                {
                    return false;
                }
            }
        }
        return canPlaceBlockAt(world, pos);
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "activation");
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        try
        {
            boolean activated = false;

            Object tile = getTile(world, pos);
            if (WrenchUtility.isUsableWrench(player, player.inventory.getCurrentItem(), pos.getX(), pos.getY(), pos.getZ()))
            {
                ListenerIterator it = new ListenerIterator(world, pos, this, "wrench");
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
                    WrenchUtility.damageWrench(player, player.inventory.getCurrentItem(), pos.getX(), pos.getY(), pos.getZ());
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
                        player.openGui(mod, id, world, pos.getX(), pos.getY(), pos.getZ());
                        return true;
                    }
                }
            }

            ListenerIterator it = new ListenerIterator(world, pos, this, "activation");
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
            outputError(world, pos.getX(), pos.getY(), pos.getZ(), "while right click block on side " + side, e);
            player.sendMessage(new TextComponentString(Colors.RED.code + LanguageUtility.getLocal("blockTile.error.onBlockActivated")));
        }
        return false;
    }

    protected Object getTile(World world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof ITileNodeHost)
        {
            return ((ITileNodeHost) tile).getTileNode();
        }
        return tile;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "update");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IUpdateListener)
            {
                ((IUpdateListener) next).updateTick(rand);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "update");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IUpdateListener)
            {
                ((IUpdateListener) next).randomDisplayTick(rand);
            }
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "update");
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
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean p_185477_7_)
    {
        super.addCollisionBoxToList(state, world, pos, aabb, list, entity, p_185477_7_);

        ListenerIterator it = new ListenerIterator(world, pos, this, "bounds");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IBoundListener)
            {
                List<AxisAlignedBB> collect = new ArrayList();
                ((IBoundListener) next).addCollisionBoxesToList(aabb, collect, entity);
                for (AxisAlignedBB object : list)
                {
                    if (object != null && aabb.intersects(object))
                    {
                        list.add(object);
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "bounds");
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
        return data.getSelectionBounds().clone().add(pos.getX(), pos.getY(), pos.getZ()).toAABB();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "bounds");
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
        return data.getBlockBounds().clone().add(pos.getX(), pos.getY(), pos.getZ()).toAABB();
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

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        ListenerIterator it = new ListenerIterator(world, pos, this, "blockStack");
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
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        super.getDrops(drops, world, pos, state, fortune);

        ListenerIterator it = new ListenerIterator(world, pos, this, "blockStack");
        while (it.hasNext())
        {
            ITileEventListener next = it.next();
            if (next instanceof IBlockStackListener)
            {
                ((IBlockStackListener) next).collectDrops(drops, state, fortune);
            }
        }
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        super.getSubBlocks(itemIn, items);
        if (listeners.containsKey("blockStack"))
        {
            for (ITileEventListener listener : listeners.get("blockStack"))
            {
                if (listener instanceof IBlockStackListener)
                {
                    ((IBlockStackListener) listener).getSubBlocks(itemIn, items);
                }
            }
        }
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
            dim = "" + world.provider.getDimension();
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
