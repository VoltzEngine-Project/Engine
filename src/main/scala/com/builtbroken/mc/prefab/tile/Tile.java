package com.builtbroken.mc.prefab.tile;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.jlib.data.vector.Pos3D;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.items.ISimpleItemRenderer;
import com.builtbroken.mc.api.tile.IPlayerUsing;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.AbstractPacket;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import com.builtbroken.mc.lib.helper.MathUtility;
import com.builtbroken.mc.lib.helper.WrenchUtility;
import com.builtbroken.mc.lib.render.block.BlockRenderHandler;
import com.builtbroken.mc.lib.render.block.RenderTileDummy;
import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.tile.entity.TileEntityBase;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.*;

/**
 * Base class for VE's tile system that combines the Block and Tile class to make implementing
 * new blocks faster. It does have some memory overhead due to the number of fields each class
 * stores. Though the amount of memory used is very little. If it becomes an issue it is recommended to just use
 * the classic Block & TileEntity system from Minecraft. As this will have less memory overhead but will take
 * more work to implement the same functionality.
 * <p/>
 * In order for this class to work it needs to be registered threw the ModManager or something similar. In
 * which a new BlockTile will be created with a static version of this class in it.
 * <p/>
 * Just as a note the system is designed in a special way in order to function. This class will act as a
 * redirect for your Block & Tile. If a method or Field has BLOCK in the java doc it is treated like
 * a static value. If it has the world TILE in the java doc the it is primary directed at the tile instance
 * at the block location. If it can't find the tile it will redirect to the static tile that is your Block.
 * <p/>
 * <p/>
 * Created by Robert(DarkGuardsman) on 1/4/2015.
 */
public abstract class Tile extends TileEntityBase implements IWorldPosition, IPlayerUsing, IRegistryInit
{
    //Static block vars, never use in your tile
    /** STATIC BLOCK, block for this tile. Will not be initialized in each tile */
    private BlockTile block = null;
    /** STATIC BLOCK, injected by the BlockTile for methods calls */
    private IBlockAccess access = null;
    /** STATIC BLOCK, Mod domain, injected when the tile is built */
    public String domain;

    //Block vars
    /** BLOCK, name of the block for this tile */
    public String name;
    /** BLOCK, creative tab to use for listing this tile */
    public CreativeTabs creativeTab;
    /** BLOCK, material type for the block to use for varies checks */
    public Material material = Material.clay;
    /** BLOCK, hardness value for mining speed */
    public float hardness = 1;
    /** BLOCK, resistance value for explosions */
    public float resistance = 1;
    /** BLOCK, can this tile emmit redstone */
    public boolean canEmmitRedstone = false;
    /** BLOCK, is the block solid (true) or can it be seen threw (false) */
    public boolean isOpaque = false;
    /** BLOCK, sound this tile makes when entities step on it */
    public Block.SoundType stepSound = Block.soundTypeStone;
    /** BLOCK, ItemBlock class to register with this tile */
    public Class<? extends ItemBlock> itemBlock = ItemBlock.class;

    /** Collision box for this tile, also used for selection and any other size value for the block */
    protected Cube bounds = new Cube(0, 0, 0, 1, 1, 1);

    //Icon vars
    /** Map of icons by name */
    @SideOnly(Side.CLIENT)
    protected HashMap<String, IIcon> icons;
    /** Should the tile use the helper functionality for registering and getting textures by sides */
    protected boolean useSidedTextures = false;
    /** Name of the main texture for the block */
    protected String textureName;

    //Renderer vars
    /** TILE, Triggered when dynamic renderer crashes to prevent more errors from spamming chat */
    public boolean dynamicRendererCrashed = false;
    /** BLOCK, Should we render a normal block */
    public boolean renderNormalBlock = true;
    /** BLOCK, Should we render a TileEntitySpecialRenderer */
    public boolean renderTileEntity = true;
    /** BLOCK, Render Type used by the block for checking how to render */
    public int renderType = BlockRenderHandler.ID; //renderNormalBlock will force this to zero

    //Tile Vars
    /** TILE, Current tick count, starts when the tile is placed */
    public long ticks = 0L;
    /** TILE, Next tick when cleanup code will be called to check the sanity of the tile */
    protected int nextCleanupTick = 200;
    /** TILE, Set of player's with this tile's interface open, mainly used for GUI packet updates */
    protected final Set<EntityPlayer> playersUsing = new HashSet();
    /** TILE, Owner of the tile as a UUID, primary method for getting the player who owns this tile */
    protected UUID owner;
    /** TILE, Owner of the tile as a String, mainly used for display or quick checks */
    protected String username;

    /** STATIC BLOCK, main method for creating a new Static Tile */
    public Tile(String name, Material material)
    {
        this.name = name;
        this.material = material;
    }

    /** TILE, use this to initialize a tile without setting block data */
    public Tile()
    {

    }

    /**
     * BLOCK
     * <p/>
     * Called to create a new tile for the block. First call will
     * be the used to see if the BlockTile is a plain Block or Tile Block
     *
     * @return Normally you want to return a new instance of this but
     * can do anything you want. Null will tell the registery system
     * that this block spawns no TileEntities
     */
    public abstract Tile newTile();

    /**
     * BLOCK
     * <p/>
     * Overloaded version of newTile that passes in world and block meta
     *
     * @param world - world the tile will be placed into
     * @param meta  - meta value the block was set will
     * @return new tile
     */
    public Tile newTile(World world, int meta)
    {
        return newTile();
    }

    /**
     * TILE, Called by the world to update the tile. Never
     * call this from your owner code. Use Update() method
     * as this is set final to ensure base functionality.
     */
    @Override
    public final void updateEntity()
    {
        if (ticks == 0)
        {
            firstTick();
        }
        else
        {
            update();
        }

        //Increase tick
        if (ticks >= Long.MAX_VALUE)
            ticks = 0;
        ticks += 1;
        if (ticks % nextCleanupTick == 0)
        {
            doCleanupCheck();
            nextCleanupTick = 100 + (int) (world().rand.nextFloat() * 2000);
        }
        if (getPlayersUsing().size() > 0)
        {
            doUpdateGuiUsers();
        }

    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey("tileOwnerMostSigBit") && nbt.hasKey("tileOwnerLeastSigBit"))
        {
            this.owner = new UUID(nbt.getLong("tileOwnerMostSigBit"), nbt.getLong("tileOwnerLeastSigBit"));
        }
        if (nbt.hasKey("tileOwnerUsername"))
        {
            this.username = nbt.getString("tileOwnerUsername");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if (owner != null)
        {
            nbt.setLong("tileOwnerMostSigBit", this.owner.getMostSignificantBits());
            nbt.setLong("tileOwnerLeastSigBit", this.owner.getLeastSignificantBits());
        }
        if (username != null && !username.isEmpty())
        {
            nbt.setString("tileOwnerUsername", this.username);
        }
    }

    /** BLOCK, called from the world when the block is updated */
    public void blockUpdate()
    {
        update();
    }

    /**
     * TILE, Called first update() call of the tile. Use
     * this to init any values that are needed right
     * after the tile has been fully placed into the
     * world.
     */
    public void firstTick()
    {
    }

    /**
     * TILE,
     * Called each tick as long as the the tile can update.
     */
    public void update()
    {

    }

    /**
     * TILE,
     * Called each tick that users
     * have the Gui open. Is called
     * both sides and should be used
     * to send packets to the client
     */
    public void doUpdateGuiUsers()
    {

    }

    /**
     * TILE,
     * Called every so many ticks to ask the tile to check
     * for errors and cleanup data. Mainly used to clear
     * out caches that are no longer needed.
     */
    public void doCleanupCheck()
    {

    }


    //=============================
    //====== Interaction ==========
    //=============================

    /**
     * Called when the player left clicks the block
     */
    public boolean onPlayerLeftClick(EntityPlayer player)
    {
        return false;
    }

    /**
     * Called when the player right clicks the block.
     * Default implementation calls onPlayerRightClick() and onPlayerRightClickWrench()
     *
     * @param player - player who clicked the block
     * @param side   - side of the block clicked as an int(0-5)
     * @param hit    - Vector3 location of the spot hit on the block
     * @return true if the click event was used
     */
    public boolean onPlayerActivated(EntityPlayer player, int side, Pos hit)
    {
        if (WrenchUtility.isUsableWrench(player, player.inventory.getCurrentItem(), xi(), yi(), zi()))
        {
            if (onPlayerRightClickWrench(player, side, hit))
            {
                WrenchUtility.damageWrench(player, player.inventory.getCurrentItem(), xi(), yi(), zi());
                return true;
            }
            return false;
        }
        return onPlayerRightClick(player, side, hit);
    }

    /**
     * Called when the player has clicked a block with something other than a wrench
     *
     * @param player - player who clicked the block, don't assume EntityPlayerMP as it can be a fake player
     * @param side   - side of the block clicked as an int(0-5)
     * @param hit    - Vector3 location of the spot hit on the block
     * @return true if the click event was used
     */
    protected boolean onPlayerRightClick(EntityPlayer player, int side, Pos hit)
    {
        return false;
    }

    /**
     * Called when the player uses a supported wrench on the block
     *
     * @param player - player who clicked the block, don't assume EntityPlayerMP as it can be a fake player
     * @param side   - side of the block clicked as an int(0-5)
     * @param hit    - Vector3 location of the spot hit on the block
     * @return true if the click event was used
     */
    protected boolean onPlayerRightClickWrench(EntityPlayer player, int side, Pos hit)
    {
        return false;
    }

    /**
     * Opens the main gui for this tile
     */
    public void openGui(EntityPlayer player, Object mod)
    {
        openGui(player, 0, mod);
    }

    /**
     * Opens a gui by the id given
     */
    public void openGui(EntityPlayer player, int gui, Object mod)
    {
        if (isServer())
            player.openGui(mod, gui, world(), xi(), yi(), zi());
    }

    //===========================
    //======= ItemStack  ========
    //===========================
    public ArrayList<ItemStack> getDrops(int metadata, int fortune)
    {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (getBlockType() != null)
        {
            drops.add(new ItemStack(getBlockType(), quantityDropped(metadata, fortune), metadataDropped(metadata, fortune)));
        }
        return drops;
    }

    /**
     * Block object that goes to this tile
     */
    public Block getBlockType()
    {
        if (access != null)
        {
            Block b = access.getBlock(xi(), yi(), zi());
            if (b == null)
            {
                return block;
            }
            return b;
        }
        return block;
    }

    public BlockTile getTileBlock()
    {
        return block;
    }

    /**
     * Number of items to drop
     */
    public int quantityDropped(int meta, int fortune)
    {
        return 1;
    }

    /**
     * Meta value to drop
     */
    public int metadataDropped(int meta, int fortune)
    {
        return 0;
    }


    /**
     * BLOCK,
     * Gets all sub versions of this block to add to the creative menu
     *
     * @param item         - Item object of the block
     * @param creativeTabs - creative tab to list on
     * @param list         - current list of items
     */
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
        list.add(new ItemStack(item, 1, 0));
    }

    /**
     * Gets the ItemStack when the player pick blocks
     *
     * @param target - block hit by a ray trace
     * @return ItemStack of your block, can be null but shouldn't unless the block can't be placed
     */
    public ItemStack getPickBlock(MovingObjectPosition target)
    {
        return toItemStack();
    }

    public ItemStack toItemStack()
    {
        return new ItemStack(getBlockType(), 1, metadataDropped(getBlockMetadata(), 0));
    }

    //==============================
    //==== Location ================
    //==============================
    @Override
    public double z()
    {
        return zCoord;
    }

    public int zi()
    {
        return zCoord;
    }

    @Override
    public double x()
    {
        return xCoord;
    }

    public int xi()
    {
        return xCoord;
    }

    @Override
    public double y()
    {
        return yCoord;
    }

    public int yi()
    {
        return yCoord;
    }

    @Override
    public World world()
    {
        return getWorldObj();
    }

    /** BLOCK, is only called for the static tile */
    public void setBlock(BlockTile block)
    {
        this.block = block;
    }

    /** STATIC BLOCk, is only called by BlockTile for the static version of this tile */
    public void setAccess(IBlockAccess access)
    {
        this.access = access;
    }

    /**
     * Can be called from both a Block or Tile version. Tile version will always return
     * World. Block version will try to return IBlockAccess but can be null
     */
    public IBlockAccess getAccess()
    {
        if (world() != null)
        {
            return world();
        }
        return access;
    }

    @Deprecated
    public Pos toVector3()
    {
        return new Pos(x(), y(), z());
    }

    /**
     * Converts the tile into a position using its coords
     *
     * @return new Pos with the Tile's location
     */
    public Pos toPos()
    {
        return new Pos(x(), y(), z());
    }

    @Deprecated
    public Location toVectorWorld()
    {
        return new Location(world(), x(), y(), z());
    }

    /**
     * Converts the tile into a position using its coords
     *
     * @return new Location with the Tile's location
     */
    public Location toLocation()
    {
        return new Location(world(), x(), y(), z());
    }

    //=========================
    //=== Events ==============
    //=========================

    @Override
    public void onRegistered()
    {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientRegistered()
    {

    }

    public void onAdded()
    {
        onWorldJoin();
    }

    public void onWorldJoin()
    {
    }

    /**
     * Called when the block is placed by a living entity
     *
     * @param entityLiving - entity who placed the block
     * @param itemStack    - ItemStack the entity used to place the block
     */
    public void onPlaced(EntityLivingBase entityLiving, ItemStack itemStack)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            setOwner((EntityPlayer) entityLiving);
        }
    }

    /**
     * Called after the block has been placed
     *
     * @param metadata - meta of the placed block
     */
    public void onPostPlaced(int metadata)
    {

    }

    /**
     * Called when the block is removed. Do all cleanup needed in this method.
     *
     * @param block - Block object being removed
     * @param par6  - meta of the block
     */
    public void onRemove(Block block, int par6)
    {
        onWorldSeparate();
    }

    public void onWorldSeparate()
    {
    }

    public void onDestroyedByExplosion(Explosion ex)
    {

    }

    /**
     * Called when a neighbor block changes
     *
     * @param block
     */
    public void onNeighborChanged(Block block)
    {
    }

    /**
     * Called when a neighbor tile changes
     *
     * @param pos
     */
    public void onNeighborChanged(Pos pos)
    {
    }

    /**
     * Called when an entity collides with this block.
     */
    public void onCollide(Entity entity)
    {
    }

    /**
     * Called when a rain particle hits this block
     */
    public void onFillRain()
    {
    }

    //==========================
    //==== Triggers ============
    //==========================
    public void notifyBlocksOfNeighborChange()
    {
        world().notifyBlocksOfNeighborChange(xi(), yi(), zi(), block);
    }

    protected void markRender()
    {
        world().func_147479_m(xi(), yi(), zi());
    }

    protected void markUpdate()
    {
        world().markBlockForUpdate(xi(), yi(), zi());
    }

    protected void updateLight()
    {
        world().func_147451_t(xi(), yi(), zi());
    }

    protected void scheduleTick(int delay)
    {
        world().scheduleBlockUpdate(xi(), yi(), zi(), getBlockType(), delay);
    }

    //=========================
    //==== Getters ============
    //=========================

    /**
     * Gets the light value of the block
     *
     * @return light value from 0 - 16;
     */
    public int getLightValue()
    {
        return 0;
    }


    /**
     * Collision Note that all bounds done in the the tile is relative to the tile's position.
     */
    public Iterable<Cube> getCollisionBoxes(Cube intersect, Entity entity)
    {
        List<Cube> boxes = new ArrayList<>();
        boxes.add(getCollisionBounds());
        return boxes;
    }

    public Cube getSelectBounds()
    {
        return bounds;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return getCollisionBounds().clone().add(x(), y(), z()).toAABB();
    }

    public Cube getCollisionBounds()
    {
        return bounds;
    }

    public int getMetadata()
    {
        if (getAccess() != null)
            return getAccess().getBlockMetadata(xi(), yi(), zi());
        else
            return 0;
    }

    @Override
    public int getBlockMetadata()
    {
        if (world() == null)
            return 0;
        else
            return super.getBlockMetadata();
    }

    @Override
    public Set<EntityPlayer> getPlayersUsing()
    {
        return playersUsing;
    }

    //TODO: Get rid of parameters
    public boolean shouldSideBeRendered(int side)
    {
        return (side == 0 && this.bounds.min().y() > 0.0D)
                || (side == 1 && this.bounds.max().y() < 1.0D)
                || (side == 2 && this.bounds.min().z() > 0.0D)
                || (side == 3 && this.bounds.max().z() < 1.0D)
                || (side == 4 && this.bounds.min().x() > 0.0D)
                || (side == 5 && this.bounds.max().x() < 1.0D)
                || !access.getBlock(xi(), yi(), zi()).isOpaqueCube();
    }


    /**
     * Is this block being indirectly being powered
     */
    public boolean isIndirectlyPowered()
    {
        return world().isBlockIndirectlyGettingPowered(xi(), yi(), zi());
    }

    /**
     * Gets the level of power provide to this block
     */
    public int getStrongestIndirectPower()
    {
        return world().getStrongestIndirectPower(xi(), yi(), zi());
    }

    /**
     * Gets the level of power being provided by this block
     */
    public int getWeakRedstonePower(int side)
    {
        return getStrongRedstonePower(side);
    }

    /**
     * Gets the level of power being provided by this block
     */
    public int getStrongRedstonePower(int side)
    {
        return 0;
    }

    public void setBlockBoundsBasedOnState()
    {

    }

    /**
     * Is this block solid on th side
     *
     * @param side - side
     * @return true if solid
     */
    public boolean isSolid(int side)
    {
        return material.isSolid();
    }

    /**
     * Render pass
     */
    public int getRenderBlockPass()
    {
        return 0;
    }

    /**
     * Tick rate of the tile in @param world
     */
    public int tickRate()
    {
        return 20;
    }

    /**
     * Can the player silk touch the block
     *
     * @param player   - player mining the block
     * @param metadata - meta value of the block
     * @return true if it can
     */
    public boolean canSilkHarvest(EntityPlayer player, int metadata)
    {
        return false;
    }

    /**
     * Called to see if the block can be placed at the location
     *
     * @return true if it can be placed
     */
    public boolean canPlaceBlockAt()
    {
        return world().getBlock(xi(), yi(), zi()).isReplaceable(world(), xi(), yi(), zi());
    }

    /**
     * Called when the player removes a block
     *
     * @param player      - user doing the action
     * @param willHarvest - did the player harvest the block, eg should it drop items
     * @return true if the action was used and the block changed
     */
    public boolean removeByPlayer(EntityPlayer player, boolean willHarvest)
    {
        return world().setBlockToAir(xi(), yi(), zi());
    }

    /**
     * Called to see if the block can be placed on the side of a block at the location
     *
     * @param side - side
     * @return
     */
    public boolean canPlaceBlockOnSide(ForgeDirection side)
    {
        return canPlaceBlockAt();
    }

    /**
     * Gets the explosive resistance of this block.
     *
     * @param entity            - The affecting entity
     * @param explosionPosition - The position in which the explosion is ocurring at
     * @return A value representing the explosive resistance
     */
    public float getExplosionResistance(Entity entity, Pos explosionPosition)
    {
        return getExplosionResistance(entity);
    }

    /**
     * Gets the explosive resistance of this block.
     * Note: Called without the world object being present.
     *
     * @param entity - The affecting entity
     * @return A value representing the explosive resistance
     */
    public float getExplosionResistance(Entity entity)
    {
        return resistance / 5f;
    }

    public boolean isClient()
    {
        return world().isRemote;
    }

    public boolean isServer()
    {
        return !world().isRemote;
    }

    //==========================
    //== Owner Helper Methods ==
    //==========================

    public UUID getOwnerID()
    {
        return owner;
    }

    public String getOwnerName()
    {
        GameProfile profile = getOwnerProfile();
        if (profile != null)
            return profile.getName();
        return null;
    }

    public GameProfile getOwnerProfile()
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            return null;
        return MinecraftServer.getServer().func_152358_ax().func_152652_a(owner);
    }

    public void setOwnerID(UUID id)
    {
        this.owner = id;
    }

    public void setOwner(EntityPlayer player)
    {
        if (player != null)
        {
            setOwnerID(player.getGameProfile().getId());
            this.username = player.getCommandSenderName();
        }
        else
        {
            setOwnerID(null);
            this.username = null;
        }
    }

    //=========================
    //==== Icons ==============
    //=========================

    /**
     * Called either by an item, or in a world.
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (useSidedTextures)
        {
            if (side == 0)
            {
                return getTopIcon(meta);
            }
            if (side == 1)
            {
                return getBottomIcon(meta);
            }
            return getSideIcon(meta, side);
        }
        return getIcon(side);
    }

    /**
     * Called in the world.
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side)
    {
        return getIcon();
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon()
    {
        return getIcon(getTextureName());
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(String name)
    {
        if (icons != null && icons.containsKey(name))
        {
            return icons.get(name);
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    protected String getTextureName()
    {
        if (textureName == null)
            return "MISSING_ICON_TILE_" + Block.getIdFromBlock(block) + "_" + name;
        else
            return block.staticTile.domain + textureName;
    }

    /**
     * Gets the icon that renders on the top
     *
     * @param meta - placement data
     * @return icon that will render on top
     */
    @SideOnly(Side.CLIENT)
    protected IIcon getTopIcon(int meta)
    {
        IIcon icon = icons.get(getTextureName() + "_top");
        if (icon == null)
            icon = icons.get(getTextureName());
        return icon;
    }

    /**
     * Gets the icon that renders on the bottom
     *
     * @param meta - placement data
     * @return icon that will render on bottom
     */
    @SideOnly(Side.CLIENT)
    protected IIcon getBottomIcon(int meta)
    {
        IIcon icon = icons.get(getTextureName() + "_bottom");
        if (icon == null)
            icon = icons.get(getTextureName());
        return icon;
    }

    /**
     * Gets the icon that renders on the sides
     *
     * @param meta - placement data
     * @return icon that will render on sides
     */
    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon(int meta)
    {
        return getSideIcon(meta, 0);
    }

    /**
     * Gets the icon that renders on the sides
     *
     * @param meta - placement data
     * @param side - side of the icon
     * @return icon that will render on sides
     */
    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon(int meta, int side)
    {
        IIcon icon = icons.get(getTextureName() + "_side");
        if (icon == null)
            icon = icons.get(getTextureName());
        return icon;
    }

    public void setTextureName(String value)
    {
        textureName = value;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        icons = new HashMap();
        if (useSidedTextures)
        {
            registerSideTextureSet(iconRegister);
        }
        else
        {
            icons.put(getTextureName(), iconRegister.registerIcon(getTextureName()));
        }
        //TODO loop threw missing textures replacing them with textures based on material
    }

    /**
     * Registers a set of 3 textures(top, sides, bottom) to be used for the block renderer
     * Uses the texture name appended with _top _side _bottom
     *
     * @param iconRegister
     */
    @SideOnly(Side.CLIENT)
    public void registerSideTextureSet(IIconRegister iconRegister)
    {
        icons.put(getTextureName(), iconRegister.registerIcon(getTextureName() + "_top"));
        icons.put(getTextureName(), iconRegister.registerIcon(getTextureName() + "_side"));
        icons.put(getTextureName(), iconRegister.registerIcon(getTextureName() + "_bottom"));
    }

    @SideOnly(Side.CLIENT)
    public int getColorMultiplier()
    {
        return 0xFFFFFF;
    }

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return 16777215;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_)
    {
        return 16777215;
    }

    //=========================
    //==== Renderers ==========
    //=========================

    /**
     * Render the static, unmoving faces of this part into the world renderer.
     * The Tessellator is already drawing.
     *
     * @return true if vertices were added to the tessellator
     */
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(RenderBlocks renderer, Pos pos, int pass)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasSpecialRenderer()
    {
        return getSpecialRenderer() != null;
    }

    @SideOnly(Side.CLIENT)
    public TileEntitySpecialRenderer getSpecialRenderer()
    {
        return TileEntityRendererDispatcher.instance.getSpecialRendererByClass(getClass());
    }

    @SideOnly(Side.CLIENT)
    public void renderInventory(ItemStack itemStack)
    {
        TileEntitySpecialRenderer tesr = getSpecialRenderer();

        if (!(tesr instanceof RenderTileDummy))
        {
            if (tesr instanceof ISimpleItemRenderer)
            {
                ((ISimpleItemRenderer) tesr).renderInventoryItem(IItemRenderer.ItemRenderType.INVENTORY, itemStack);
            }

            if (!dynamicRendererCrashed)
            {
                try
                {
                    renderDynamic(new Pos(-0.5, -0.5, -0.5), 0, 0);
                } catch (Exception e)
                {
                    dynamicRendererCrashed = true;
                    renderNormalBlock = true;
                    System.out.println("A tile has failed to render dynamically as an item. Suppressing renderer to prevent future crashes.");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Render the dynamic, changing faces of this part and other gfx as in a TESR.
     * The Tessellator will need to be started if it is to be used.
     *
     * @param pos   The position of this block space relative to the renderer, same as x, y, z passed to TESR.
     * @param frame The partial interpolation frame value for animations between ticks
     * @param pass  The render pass, 1 or 0
     */
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Pos pos, float frame, int pass)
    {
        TileEntitySpecialRenderer tesr = getSpecialRenderer();

        if (tesr != null && !(tesr instanceof RenderTileDummy))
        {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
            GL11.glPushMatrix();
            GL11.glTranslated(-0.5, -0.5, -0.5);
            tesr.renderTileEntityAt(this, 0, 0, 0, 0);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    public void randomDisplayTick()
    {

    }

    //===========================
    //==== Helpers ==============
    //===========================

    public double distance(Entity entity)
    {
        return distance(entity.posX, entity.posY, entity.posZ);
    }

    public double distance(IPos3D pos)
    {
        if (pos instanceof Pos3D)
        {
            return ((Pos3D) pos).distance(x() + 0.5, y() + 0.5, z() + 0.5);
        }
        return distance(pos.x(), pos.y(), pos.z());
    }

    public double distance(double x, double y, double z)
    {
        double xx = x() - x;
        double yy = y() - y;
        double zz = z() - z;

        return Math.sqrt(xx * xx + yy * yy + zz * zz);
    }

    public void setMeta(int meta)
    {
        world().setBlockMetadataWithNotify(xi(), yi(), zi(), meta, 3);
    }

    public NBTTagCompound getSaveData()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return tag;
    }

    @Override
    public final Packet getDescriptionPacket()
    {
        return Engine.instance.packetHandler.toMCPacket(getDescPacket());
    }

    public AbstractPacket getDescPacket()
    {
        return null;
    }

    /**
     * Sends the desc packet to all players around this tile
     */
    public void sendDescPacket()
    {
        sendPacket(getDescPacket());
    }

    /**
     * Sends the packet to all players around this tile
     *
     * @param packet - packet to send
     */
    public void sendPacket(AbstractPacket packet)
    {
        sendPacket(packet, 64);
    }

    /**
     * Sends the packet to all players around this tile
     *
     * @param packet   - packet to send
     * @param distance - distance in blocks to search for players
     */
    public void sendPacket(AbstractPacket packet, double distance)
    {
        if (isServer())
            Engine.instance.packetHandler.sendToAllAround(packet, world(), xi(), yi(), zi(), distance);
    }

    public void sendPacketToServer(AbstractPacket packet)
    {
        if (isClient())
            Engine.instance.packetHandler.sendToServer(packet);
    }

    public void sendPacketToGuiUsers(AbstractPacket packet)
    {
        for (EntityPlayer player : getPlayersUsing())
        {
            if (player instanceof EntityPlayerMP)
            {
                Engine.instance.packetHandler.sendToPlayer(packet, (EntityPlayerMP) player);
            }
        }
    }


    /**
     * gets the way this piston should face for that entity that placed it.
     */
    public byte determineOrientation(EntityLivingBase entityLiving)
    {
        if (entityLiving != null)
        {
            if (MathUtility.func_154353_e(entityLiving.posX - x()) < 2.0F && MathUtility.func_154353_e(entityLiving.posZ - z()) < 2.0F)
            {
                double var5 = entityLiving.posY + 1.82D - entityLiving.yOffset;
                if (var5 - y() > 2.0D)
                {
                    return 1;
                }
                if (y() - var5 > 0.0D)
                {
                    return 0;
                }
            }
            int rotation = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
            if (rotation == 0) return 2;
            else if (rotation == 1) return 5;
            else if (rotation == 2) return 3;
            else if (rotation == 3) return 4;
            else return 0;
        }
        return 0;
    }

    public ForgeDirection determineForgeDirection(EntityLivingBase entityLiving)
    {
        return ForgeDirection.getOrientation(determineOrientation(entityLiving));
    }
}
